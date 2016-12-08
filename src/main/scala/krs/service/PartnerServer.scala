package krs.service

import com.twitter.util.{ Await, Future }
import com.twitter.finagle.Thrift
import com.twitter.server.TwitterServer

import krs.thriftscala.{ PartnerService, PartnerOffer, OfferResponse }
import krs.PartnerSystem._

object PartnerServer extends TwitterServer {
  val offers = loadOffers(readFile("./fixtures/data.json"))

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

  def main(): Unit = {
    val service = buildServer()
    val server = Thrift.server.serveIface("localhost:8081", service)
    onExit {
      server.close()
    }
    Await.ready(server)
  }
}
