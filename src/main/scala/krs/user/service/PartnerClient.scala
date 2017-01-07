package krs.user.service

import com.twitter.util.{Future}
import com.twitter.finagle.Thrift

import krs.partner.domain.{Offer, CreditCard}
import krs.partner.api.{PartnerApi}
import krs.thriftscala.{PartnerService, PartnerOffer}

case class PartnerClient() extends PartnerApi {
  private val conf = com.typesafe.config.ConfigFactory.load()
  private val partnerHost = conf.getString("krs.partner.host")

  private val client: PartnerService.FutureIface =
    Thrift.client.newIface[PartnerService.FutureIface](partnerHost, classOf[PartnerService.FutureIface])

  private def convertOffer(o: PartnerOffer) =
    CreditCard(o.provider, Range(o.minimumCreditScore.getOrElse(0), o.maximumCreditScore.getOrElse(0)))

  def getOffers(creditScore: Int): Future[Seq[Offer]] = {
    client.getOffers(creditScore).map(_.offers.map(convertOffer))
  }
}
