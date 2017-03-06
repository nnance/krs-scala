package krs.user

import com.twitter.server.TwitterServer
import com.twitter.util.{Await, Future}
import krs.partner.PartnerDomain.GetOffers

trait ServiceInfrastructure {
  import UserDomain._
  import com.typesafe.config.Config
  import krs.user.service.{PartnerClient, PartnerRemoteClient}

  val conf: Config = com.typesafe.config.ConfigFactory.load()
  val userData: String = conf.getString("krs.user.data")
  val partnerHost: String = conf.getString("krs.partner.host")

  val repo = UserFileRepository(userData)
  val partnerClient: PartnerClient = PartnerRemoteClient()

  def getUserFromRepo: GetUser = repo.getUser
  def getOffersFromRepo: GetOffers = partnerClient.getOffers
}

trait UserServiceImpl extends UserSystem {
  import krs.common.PartnerUtil.convertOffer
  import krs.eligibility.EligibilitySystem.filterEligible
  import krs.thriftscala.{User, UserService}
  import krs.user.UserDomain.{GetUser, UserNotFound, UserWithOffers}

  def getUserFromRepo: GetUser
  def getOffersFromRepo: GetOffers

  def getUserWithOffersFromRepo: Int => Future[Option[UserWithOffers]] =
    super.getUserWithOffers(getUserFromRepo, getOffersFromRepo, filterEligible, _)

  def service: UserService[Future] = {

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

object UserServer extends
  TwitterServer with
  UserServiceImpl with
  ServiceInfrastructure {

  import com.twitter.finagle.Thrift
  import com.twitter.finagle.stats.Counter

  val userService: Counter = statsReceiver.counter("userService")

  def main(): Unit = {
    val conf = com.typesafe.config.ConfigFactory.load()
    val host = conf.getString("krs.user.host")

    val server = Thrift.server
      .withStatsReceiver(statsReceiver)
      .serveIface(host, service)

    onExit { server.close() }
    Await.ready(server)
  }
}

