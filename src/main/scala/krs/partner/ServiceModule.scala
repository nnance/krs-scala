package krs.partner

import com.twitter.util.{Await}
import com.twitter.finagle.Thrift
import com.twitter.finagle.stats.Counter
import com.twitter.server.TwitterServer

object PartnerServer
    extends TwitterServer
    with ServiceModule
    with ApiModule
    with DomainModule {

  val serviceImpl = PartnerServiceImpl(partnerApi)

  val partnerService = statsReceiver.counter("partnerService")

  def main(): Unit = {

    val conf = com.typesafe.config.ConfigFactory.load()
    val host = conf.getString("krs.partner.host")

    val server = Thrift.server
      .withStatsReceiver(statsReceiver)
      .serveIface("localhost:8081", serviceImpl)

    onExit { server.close() }
    Await.ready(server)
  }
}

object PartnerServiceImpl {
  import com.twitter.util.{Future}
  import krs.common.{PartnerUtil}
  import krs.thriftscala.{PartnerService, PartnerResponse}

  def apply(api: PartnerApi): PartnerService[Future] =
    new PartnerService[Future] {
      def getOffers(creditScore: Int) =
        api.getOffers(creditScore).map(offers =>
          PartnerResponse(offers.map(PartnerUtil.convertOffer)))
    }
}

trait ServiceModule { this: ApiModule =>
  private val conf = com.typesafe.config.ConfigFactory.load();
  private val partnerData = conf.getString("krs.partner.data")

  val partnerRepository = PartnerRepositoryFS(partnerData)
}
