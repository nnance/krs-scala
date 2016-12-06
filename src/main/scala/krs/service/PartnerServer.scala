package krs.service

import com.twitter.util.{ Await, Future }
import com.twitter.finagle.Thrift

import krs.thriftscala.{ PartnerService, PartnerOffer, OfferResponse }
import krs.PartnerSystem._

object PartnerServer extends App {
  def buildServer(): PartnerService[Future] = {
    new PartnerService[Future] {
      val offers = loadOffers(readFile("./fixtures/data.json"))
      def getOffers() = {
        val partnerOffers = offers.map((offer) => {
          PartnerOffer(
            offer.provider,
            Option(offer.creditScoreRange.min),
            Option(offer.creditScoreRange.max))
        })
        Future(OfferResponse(partnerOffers))
      }
    }
  }

  val service = buildServer()
  val server = Thrift.server.serveIface("localhost:8081", service)
  Await.ready(server)
}
