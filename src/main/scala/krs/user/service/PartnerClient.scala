package krs.user.service

import com.twitter.finagle.Thrift
import krs.thriftscala.{PartnerOffer, PartnerService}

case class PartnerClient() {
  import krs.partner.PartnerDomain._

  private val conf = com.typesafe.config.ConfigFactory.load()
  private val partnerHost = conf.getString("krs.partner.host")

  private val client: PartnerService.FutureIface =
    Thrift.client.newIface[PartnerService.FutureIface](partnerHost, classOf[PartnerService.FutureIface])

  private def convertOffer(o: PartnerOffer) =
    CreditCard(o.provider, Range(o.minimumCreditScore.getOrElse(0), o.maximumCreditScore.getOrElse(0)))

  def getOffers: GetOffers = creditScore =>
    client.getOffers(creditScore).map(_.offers.map(convertOffer))
}
