package krs.user

import com.twitter.finagle.Thrift
import com.twitter.server.TwitterServer
import com.twitter.util.{Await, Future}
import krs.user.service.PartnerClient

trait ServiceInfrastructure {
  val conf = com.typesafe.config.ConfigFactory.load()
  val userData = conf.getString("krs.user.data")

  val repo = UserFileRepository.Repository(userData)
  val getOffers = PartnerClient().getOffers

}

object UserServer
    extends TwitterServer {

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
  import krs.common.PartnerUtil
  import krs.thriftscala.{User, UserService}
  import UserDomain._
  import krs.user.UserSystem.{UserNotFound, getUserWithOffers}
  import krs.eligibility.EligibilitySystem.filterEligible

  val getUserWithOfferFromServices: GetUserOffers = getUserWithOffers(getOffers, filterEligible, _, _)

  type GetUserOffers = (List[UserDomain.User], Int) => Future[Option[UserWithOffers]]

  def apply(): UserService[Future] = {

    new UserService[Future] {
      def getUser(id: Int) = {
        val user = repo.getUser(id) match {
          case Some(u) => User(u.id, u.name, u.creditScore, Option(u.outstandingLoanAmount))
          case None => throw UserNotFound(id)
        }
        Future.value(user)
      }

      def getUserWithOffers(id: Int) =
        getUserWithOfferFromServices(repo.loadUsers(), id).map(_ match {
          case Some(u) =>
            User(u.user.id, u.user.name, u.user.creditScore, Option(u.user.outstandingLoanAmount), Option(u.offers.map(PartnerUtil.convertOffer)))
          case None =>
            throw UserNotFound(id)
        })
    }
  }
}
