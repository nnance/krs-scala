package krs.partner

import com.twitter.finagle.Thrift
import com.twitter.server.TwitterServer
import com.twitter.util.{Await, Future}
import krs.partner.PartnerDomain.{CreditScore, Offer}

trait PartnerServiceImpl extends PartnerSystem {

  import krs.common.PartnerUtil
  import krs.thriftscala.{PartnerResponse, PartnerService}

  def repo: OfferRepository
  def getOffersFromRepo: CreditScore => Future[Seq[Offer]] = score =>
    getOffers(repo.getAll)(score)

  def service: PartnerService[Future] =
    new PartnerService[Future] {
      def getOffers(creditScore: Int) =
        getOffersFromRepo(creditScore).map(offers =>
          PartnerResponse(offers.map(PartnerUtil.convertOffer)))
    }

}

trait ServiceInfrastructure {
  private val conf = com.typesafe.config.ConfigFactory.load();
  private val partnerData = conf.getString("krs.partner.data")

  val repo = PartnerFileRepository(partnerData)
}

object PartnerServer extends
  TwitterServer with
  PartnerServiceImpl with
  ServiceInfrastructure {

  val partnerService = statsReceiver.counter("partnerService")

  def main(): Unit = {

    val conf = com.typesafe.config.ConfigFactory.load()
    val host = conf.getString("krs.partner.host")

    val server = Thrift.server
      .withStatsReceiver(statsReceiver)
      .serveIface(host, service)

    onExit { server.close() }
    Await.ready(server)
  }
}
