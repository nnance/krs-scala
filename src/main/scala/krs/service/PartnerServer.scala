package krs.service

import com.twitter.util.{ Await, Future }
import com.twitter.finagle.Thrift
import com.twitter.finagle.stats.Counter
import com.twitter.server.TwitterServer

import krs.thriftscala.{ PartnerService, PartnerOffer, OfferResponse }
import krs.infrastructure.PartnerRepositoryFS

object PartnerServer extends TwitterServer {
  val repo = new PartnerRepositoryFS("./fixtures/offers.json")
  val offers = repo.loadOffers()

  def buildServer(): PartnerService[Future] = {
    new PartnerService[Future] {
      def getOffers() = {
        val partnerOffers = offers.map((offer) => {
          PartnerOffer(
            offer.provider,
            Option(offer.creditScoreRange.min),
            Option(offer.creditScoreRange.max))
        })
        Future.value(OfferResponse(partnerOffers))
      }
    }
  }

  val partnerService: Counter = statsReceiver.counter("partnerService")

  def main(): Unit = {
    val service = buildServer()

    val server = Thrift.server
      .withStatsReceiver(statsReceiver)
      .serveIface("localhost:8081", service)

    onExit { server.close() }
    Await.ready(server)
  }
}
