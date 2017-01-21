package krs.partner

import com.twitter.finagle.Thrift
import com.twitter.server.TwitterServer
import com.twitter.util.Await

object PartnerServer
    extends TwitterServer
    with ServiceModule
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
  import com.twitter.util.Future
  import krs.common.PartnerUtil
  import krs.thriftscala.{PartnerResponse, PartnerService}

  def apply(api: PartnerSystem): PartnerService[Future] =
    new PartnerService[Future] {
      def getOffers(creditScore: Int) =
        api.getOffers(creditScore).map(offers =>
          PartnerResponse(offers.map(PartnerUtil.convertOffer)))
    }
}

trait ServiceModule { this: DomainModule =>
  private val conf = com.typesafe.config.ConfigFactory.load();
  private val partnerData = conf.getString("krs.partner.data")

  val partnerRepository = PartnerRepositoryFS(partnerData)
}
