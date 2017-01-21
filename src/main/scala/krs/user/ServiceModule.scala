package krs.user

import com.twitter.util.{Future, Await}
import com.twitter.finagle.Thrift
import com.twitter.finagle.stats.Counter
import com.twitter.server.TwitterServer

import krs.user.service.{PartnerClient}

object UserServer
    extends TwitterServer
    with ServiceModule
    with ApiModule
    with DomainModule {

  val userImpl = UserServiceImpl(userApi)

  val userService = statsReceiver.counter("userService")

  def main(): Unit = {
    val conf = com.typesafe.config.ConfigFactory.load()
    val host = conf.getString("krs.user.host")

    val server = Thrift.server
      .withStatsReceiver(statsReceiver)
      .serveIface(host, userImpl)

    onExit { server.close() }
    Await.ready(server)
  }
}

object UserServiceImpl {
  import krs.common.{PartnerUtil}
  import krs.thriftscala.{User, UserService}
  import krs.user.UserDomain.{UserNotFound}

  def apply(api: UserApi): UserService[Future] =
    new UserService[Future] {
      def getUser(id: Int) = {
        val user = api.getUser(id) match {
          case Some(u) => User(u.id, u.name, u.creditScore, Option(u.outstandingLoanAmount))
          case None => throw UserNotFound(id)
        }
        Future.value(user)
      }

      def getUserWithOffers(id: Int) =
        api.getUserWithOffers(id).map(_ match {
          case Some(u) =>
            User(u.user.id, u.user.name, u.user.creditScore, Option(u.user.outstandingLoanAmount), Option(u.offers.map(PartnerUtil.convertOffer)))
          case None =>
            throw UserNotFound(id)
        })
    }
}

trait ServiceModule { this: ApiModule =>
  val conf = com.typesafe.config.ConfigFactory.load()
  val userData = conf.getString("krs.user.data")

  val repository = UserRepositoryFS(userData)
  val partnerRepository = PartnerClient()
  val eligibilityApi = krs.eligibility.EligibilityApi
}
