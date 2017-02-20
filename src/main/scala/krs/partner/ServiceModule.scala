package krs.partner

import com.twitter.finagle.Thrift
import com.twitter.server.TwitterServer
import com.twitter.util.{Await, Future}

object PartnerServer extends
  PartnerServerComponent with
  PartnerFileRepositoryComponent with
  TwitterServer {

  import krs.common.PartnerUtil
  import krs.thriftscala.PartnerResponse

  private val conf = com.typesafe.config.ConfigFactory.load()
  private val partnerData = conf.getString("krs.partner.data")
  val partnerRepository = PartnerFileRepository(partnerData)
  val partnerSystem = PartnerServer()

  val partnerStats = statsReceiver.counter("partnerService")

  val partnerService = new krs.thriftscala.PartnerService[Future] {
    def getOffers(creditScore: Int) =
      partnerSystem.getOffers(creditScore).map(offers =>
        PartnerResponse(offers.map(PartnerUtil.convertOffer)))
  }


  def main(): Unit = {

    val conf = com.typesafe.config.ConfigFactory.load()
    val host = conf.getString("krs.partner.host")

    val server = Thrift.server
      .withStatsReceiver(statsReceiver)
      .serveIface("localhost:8081", partnerService)

    onExit { server.close() }
    Await.ready(server)
  }
}

trait PartnerServerComponent extends PartnerSystemComponent {
  this: PartnerRepositoryComponent =>

  case class PartnerServer() extends PartnerSystem {

    import PartnerDomain._

    def filterOffers(offers: List[Offer], creditScore: Int): List[Offer] =
      offers.filter(o => creditScore >= o.creditScoreRange.min && creditScore <= o.creditScoreRange.max)

    def getOffers(creditScore: Int): Future[Seq[Offer]] = {
      val offers = partnerRepository.loadOffers
      val filteredOffers = filterOffers(offers, creditScore)
      Future.value(filteredOffers)
    }
  }

}
