package krs.user

import com.twitter.finagle.Thrift
import com.twitter.finagle.stats.Counter
import com.twitter.server.TwitterServer
import com.twitter.util.Await
import com.typesafe.config.Config
import krs.partner.PartnerDomain.GetOffers
import krs.user.service.{PartnerClient, PartnerRemoteClient, UserServiceImpl}

trait ServiceInfrastructure extends UserServiceImpl {
  import UserDomain._

  val conf: Config = com.typesafe.config.ConfigFactory.load()
  val userData: String = conf.getString("krs.user.data")
  val partnerHost: String = conf.getString("krs.partner.host")

  val repo = UserFileRepository(userData)
  val partnerClient: PartnerClient = PartnerRemoteClient()

  def getUserFromRepo: GetUser = repo.getUser
  def getOffersFromRepo: GetOffers = partnerClient.getOffers
}

object UserServer extends
  TwitterServer with
  ServiceInfrastructure {

  val userService: Counter = statsReceiver.counter("userService")

  def main(): Unit = {
    val conf = com.typesafe.config.ConfigFactory.load()
    val host = conf.getString("krs.user.host")

    val server = Thrift.server
      .withStatsReceiver(statsReceiver)
      .serveIface(host, apply())

    onExit { server.close() }
    Await.ready(server)
  }
}

