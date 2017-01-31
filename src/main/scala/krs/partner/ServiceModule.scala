package krs.partner

import com.twitter.finagle.Thrift
import com.twitter.server.TwitterServer
import com.twitter.util.Await

trait ServiceInfrastructure {
  private val conf = com.typesafe.config.ConfigFactory.load();
  private val partnerData = conf.getString("krs.partner.data")

  val partnerRepository = PartnerFileRepository.Repository(partnerData)
}

object PartnerServer extends TwitterServer {

  val partnerService = statsReceiver.counter("partnerService")

  def main(): Unit = {

    val conf = com.typesafe.config.ConfigFactory.load()
    val host = conf.getString("krs.partner.host")

    val server = Thrift.server
      .withStatsReceiver(statsReceiver)
      .serveIface("localhost:8081", PartnerServiceImpl())

    onExit { server.close() }
    Await.ready(server)
  }
}

object PartnerServiceImpl extends ServiceInfrastructure {

  import com.twitter.util.Future
  import krs.common.PartnerUtil
  import krs.thriftscala.{PartnerResponse, PartnerService}

  def apply(): PartnerService[Future] =
    new PartnerService[Future] {

      def getOffers(creditScore: Int) =
        partnerRepository.getOffers(creditScore).map(offers =>
          PartnerResponse(offers.map(PartnerUtil.convertOffer)))
    }

}
