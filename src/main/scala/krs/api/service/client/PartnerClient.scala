package krs.api.service.client

import com.twitter.util.{ Future }
import com.twitter.finagle.Thrift

import krs.thriftscala.{ PartnerService, PartnerOffer }
import krs.domain.{ PartnerDomain, PartnerRepository, Offer, CreditCard }

case class PartnerClient() extends PartnerDomain {
  val client: PartnerService.FutureIface =
    Thrift.client.newIface[PartnerService.FutureIface]("localhost:8081", classOf[PartnerService.FutureIface])

  def convertOffer(o: PartnerOffer): CreditCard =
    CreditCard(o.provider, Range(o.minimumCreditScore.getOrElse(0), o.maximumCreditScore.getOrElse(0)))

  def getOffers: Future[List[Offer]] =
    client.getOffers().map(resp => resp.offers.toList.map(convertOffer))
}
