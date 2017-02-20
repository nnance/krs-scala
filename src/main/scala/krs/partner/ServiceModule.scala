package krs.partner

import com.twitter.finagle.Thrift
import com.twitter.server.TwitterServer
import com.twitter.util.Await

object PartnerServer
    extends TwitterServer {

  val partnerService = statsReceiver.counter("partnerService")

  def main(): Unit = {

    val conf = com.typesafe.config.ConfigFactory.load()
    val host = conf.getString("krs.partner.host")

    val server = Thrift.server
      .withStatsReceiver(statsReceiver)
      .serveIface("localhost:8081", PartnerServerComponent())

    onExit { server.close() }
    Await.ready(server)
  }
}

object PartnerServerComponent extends
  PartnerFileRepositoryComponent with
  PartnerServiceComponent {

  import com.twitter.util.Future
  import krs.common.PartnerUtil
  import krs.thriftscala.PartnerResponse

  private val conf = com.typesafe.config.ConfigFactory.load();
  private val partnerData = conf.getString("krs.partner.data")

  val partnerRepository = PartnerFileRepository(partnerData)
  val partnerSystem = PartnerService()

  def apply(): krs.thriftscala.PartnerService[Future] =
    new krs.thriftscala.PartnerService[Future] {
      def getOffers(creditScore: Int) =
        partnerSystem.getOffers(creditScore).map(offers =>
          PartnerResponse(offers.map(PartnerUtil.convertOffer)))
    }
}
