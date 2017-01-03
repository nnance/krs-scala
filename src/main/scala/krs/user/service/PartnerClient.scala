package krs.user.service

import com.twitter.util.{ Future }
import com.twitter.finagle.Thrift

import krs.partner.domain.{ Offer, CreditCard }
import krs.partner.api.{ PartnerApi }
import krs.thriftscala.{ PartnerService, PartnerOffer }

case class PartnerClient() extends PartnerApi {
  val conf = com.typesafe.config.ConfigFactory.load()
  val partnerHost = conf.getString("krs.partner.host")

  val client: PartnerService.FutureIface =
    Thrift.client.newIface[PartnerService.FutureIface](partnerHost, classOf[PartnerService.FutureIface])

  def convertOffer(o: PartnerOffer) =
    CreditCard(o.provider, Range(o.minimumCreditScore.getOrElse(0), o.maximumCreditScore.getOrElse(0)))

  def getOffers(creditScore: Int): Future[Seq[Offer]] = {
    client.getOffers(creditScore).map(resp => {
      resp.offers.map(convertOffer)
    })
  }
}
