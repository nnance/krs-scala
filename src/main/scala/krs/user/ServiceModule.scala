package krs.user

import com.twitter.finagle.Thrift
import com.twitter.server.TwitterServer
import com.twitter.util.{Await, Future}
import krs.eligibility.EligibilitySystemComponent
import krs.user.service.PartnerClientComponent

object UserServer extends TwitterServer {

  val userService = statsReceiver.counter("userService")

  def main(): Unit = {
    val conf = com.typesafe.config.ConfigFactory.load()
    val host = conf.getString("krs.user.host")

    val server = Thrift.server
      .withStatsReceiver(statsReceiver)
      .serveIface(host, UserServerComponent)

    onExit { server.close() }
    Await.ready(server)
  }
}

object UserServerComponent extends
  UserServiceComponent with
  UserFileRepositoryComponent with
  PartnerClientComponent with
  EligibilitySystemComponent {

  import krs.common.PartnerUtil
  import krs.thriftscala.{User}
  import krs.user.UserDomain.UserNotFound

  val conf = com.typesafe.config.ConfigFactory.load()
  val userData = conf.getString("krs.user.data")

  val userRepository = UserFileRepository(userData)
  val userService = UserService()

  def apply(): krs.thriftscala.UserService[Future] =
    new krs.thriftscala.UserService[Future] {
      def getUser(id: Int) = {
        val user = userService.getUser(id) match {
          case Some(u) => User(u.id, u.name, u.creditScore, Option(u.outstandingLoanAmount))
          case None => throw UserNotFound(id)
        }
        Future.value(user)
      }

      def getUserWithOffers(id: Int) =
        userService.getUserWithOffers(id).map(_ match {
          case Some(u) =>
            User(u.user.id, u.user.name, u.user.creditScore, Option(u.user.outstandingLoanAmount), Option(u.offers.map(PartnerUtil.convertOffer)))
          case None =>
            throw UserNotFound(id)
        })
    }
}