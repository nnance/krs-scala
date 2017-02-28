package krs.user.service

import krs.thriftscala.{PartnerOffer, PartnerService}

trait PartnerClient {
  import krs.partner.PartnerDomain._

  def client: PartnerService.FutureIface

  private def convertOffer(o: PartnerOffer) =
    CreditCard(o.provider, Range(o.minimumCreditScore.getOrElse(0), o.maximumCreditScore.getOrElse(0)))

  def getOffers: GetOffers = creditScore =>
    client.getOffers(creditScore).map(_.offers.map(convertOffer))
}

case class PartnerFinagleClient(partnerHost: String) extends PartnerClient {
  import com.twitter.finagle.Thrift
  import krs.thriftscala.PartnerService

  val client = Thrift.client.newIface[PartnerService.FutureIface](partnerHost, classOf[PartnerService.FutureIface])
}
