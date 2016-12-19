package krs.service.client

import com.twitter.finagle.Thrift

import io.finch._
import krs.thriftscala.{ PartnerService, PartnerOffer }

case class Offer(provider: String, minScore: Int, maxScore: Int)

object OfferAPI {
  val client: PartnerService.FutureIface =
    Thrift.client.newIface[PartnerService.FutureIface]("localhost:8081", classOf[PartnerService.FutureIface])

  def convertOffer(o: PartnerOffer) =
    Offer(o.provider, o.minimumCreditScore.getOrElse(0), o.maximumCreditScore.getOrElse(0))

  def getOffers: Endpoint[Seq[Offer]] = get("offers") {
    client.getOffers().map(resp => Ok(resp.offers.map(convertOffer)))
  }
}
