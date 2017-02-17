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
      .serveIface("localhost:8081", PartnerServiceImpl)

    onExit { server.close() }
    Await.ready(server)
  }
}

object PartnerServiceImpl extends PartnerServerComponent {
  import com.twitter.util.Future
  import krs.common.PartnerUtil
  import krs.thriftscala.{PartnerResponse}

  def apply(): krs.thriftscala.PartnerService[Future] =
    new krs.thriftscala.PartnerService[Future] {
      def getOffers(creditScore: Int) =
        partnerService.getOffers(creditScore).map(offers =>
          PartnerResponse(offers.map(PartnerUtil.convertOffer)))
    }
}

trait PartnerServerComponent extends
  PartnerFileRepositoryComponent with
  PartnerServiceComponent {

  private val conf = com.typesafe.config.ConfigFactory.load();
  private val partnerData = conf.getString("krs.partner.data")

  val partnerRepository = PartnerFileRepository(partnerData)
  val partnerService = PartnerServiceImpl()
}
