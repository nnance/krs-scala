package krs.partner

import com.twitter.finagle.Thrift
import com.twitter.server.TwitterServer
import com.twitter.util.{Await, Future}
import krs.partner.PartnerDomain.{CreditScore, GetAll, Offer}

object PartnerServer extends TwitterServer {

  val partnerService = statsReceiver.counter("partnerService")

  def main(): Unit = {

    val conf = com.typesafe.config.ConfigFactory.load()
    val host = conf.getString("krs.partner.host")

    val server = Thrift.server
      .withStatsReceiver(statsReceiver)
      .serveIface(host, PartnerServiceImpl())

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
        getOffersFromRepo(creditScore).map(offers =>
          PartnerResponse(offers.map(PartnerUtil.convertOffer)))
    }

}
