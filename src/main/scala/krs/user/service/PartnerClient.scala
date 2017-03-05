package krs.user.service

import com.twitter.finagle.Thrift
import com.twitter.util.Future
import com.typesafe.config.Config
import krs.partner.PartnerServiceImpl
import krs.thriftscala.{PartnerOffer, PartnerService}

trait PartnerClient {
  import krs.partner.PartnerDomain._

  def client: PartnerService[Future]

  private def convertOffer(o: PartnerOffer) =
    CreditCard(o.provider, Range(o.minimumCreditScore.getOrElse(0), o.maximumCreditScore.getOrElse(0)))

  def getOffers: GetOffers = creditScore =>
    client.getOffers(creditScore).map(_.offers.map(convertOffer))
}

case class PartnerRemoteClient() extends PartnerClient {
  val conf: Config = com.typesafe.config.ConfigFactory.load()
  val partnerHost: String = conf.getString("krs.partner.host")

  val client: PartnerService.FutureIface =
    Thrift.client.newIface[PartnerService.FutureIface](partnerHost, classOf[PartnerService.FutureIface])
}

case class PartnerLocalClient() extends PartnerClient {
  val client = PartnerServiceImpl()
}