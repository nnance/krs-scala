package krs.user

import com.twitter.finagle.Thrift
import com.twitter.server.TwitterServer
import com.twitter.util.{Await, Future}

trait ServiceInjector {
  import krs.user.service.PartnerFinagleClient

  private val conf = com.typesafe.config.ConfigFactory.load()
  private val userData = conf.getString("krs.user.data")
  private val partnerHost = conf.getString("krs.partner.host")

  val repo = UserFileRepository(userData)
  val partnerService = PartnerFinagleClient(partnerHost)
}

trait ServiceInfrastructure extends UserSystem {
  import UserDomain._
  import krs.user.service.PartnerClient
  import krs.eligibility.EligibilitySystem.filterEligible

  def repo: UserRepository
  def partnerService: PartnerClient

  case class UserNotFound(id: Int) extends Exception {
    override def getMessage: String = s"User(${id.toString}) not found."
  }

  def getUserFromRepo: GetUser = repo.getUser
  def getUserWithOffersFromRepo: Int => Future[Option[UserWithOffers]] =
    super.getUserWithOffers(repo.getUser, partnerService.getOffers, filterEligible, _)
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

object UserServiceImpl extends ServiceInfrastructure with ServiceInjector {
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
