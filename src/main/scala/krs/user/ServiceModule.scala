package krs.user

import com.twitter.finagle.Thrift
import com.twitter.server.TwitterServer
import com.twitter.util.{Await, Future}
import krs.user.service.PartnerClient

trait ServiceInfrastructure extends UserSystem {
  import UserDomain._
  import krs.eligibility.EligibilitySystem.filterEligible

  val conf = com.typesafe.config.ConfigFactory.load()
  val userData = conf.getString("krs.user.data")

  val repo = UserFileRepository(userData)
  val getOffers = PartnerClient().getOffers


  case class UserNotFound(id: Int) extends Exception {
    override def getMessage: String = s"User(${id.toString}) not found."
  }

  def getUserFromRepo: GetUser = repo.getUser
  def getUserWithOffersFromRepo: Int => Future[Option[UserWithOffers]] =
    super.getUserWithOffers(repo.getUser, getOffers, filterEligible, _)
}

object UserServer extends TwitterServer {

  val userService = statsReceiver.counter("userService")

  def main(): Unit = {
    val conf = com.typesafe.config.ConfigFactory.load()
    val host = conf.getString("krs.user.host")

    val server = Thrift.server
      .withStatsReceiver(statsReceiver)
      .serveIface(host, UserServiceImpl())

    onExit { server.close() }
    Await.ready(server)
  }
}

object UserServiceImpl extends ServiceInfrastructure {
  import krs.common.PartnerUtil.convertOffer
  import krs.thriftscala.{User, UserService}


  def apply(): UserService[Future] = {

    new UserService[Future] {
      def getUser(id: Int) = {
        val user = getUserFromRepo(id) match {
          case Some(u) => User(u.id, u.name, u.creditScore, Option(u.outstandingLoanAmount))
          case None => throw UserNotFound(id)
        }
        Future.value(user)
      }

      def getUserWithOffers(id: Int) =
        getUserWithOffersFromRepo(id).map(_ match {
          case Some(u) =>
            User(
              u.user.id,
              u.user.name,
              u.user.creditScore,
              Option(u.user.outstandingLoanAmount),
              Option(u.offers.map(convertOffer)))
          case None => throw UserNotFound(id)
        })
    }
  }
}
