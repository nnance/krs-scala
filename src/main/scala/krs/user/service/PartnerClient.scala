package krs.user.service

import com.twitter.finagle.Thrift
import com.twitter.util.Future
import krs.partner.PartnerRepository
import krs.partner.PartnerSystem.OffersRepo
import krs.thriftscala.{PartnerOffer, PartnerService}

case class PartnerClient() extends PartnerRepository {
  import krs.partner.PartnerDomain._

  private val conf = com.typesafe.config.ConfigFactory.load()
  private val partnerHost = conf.getString("krs.partner.host")

  private val client: PartnerService.FutureIface =
    Thrift.client.newIface[PartnerService.FutureIface](partnerHost, classOf[PartnerService.FutureIface])

  private def convertOffer(o: PartnerOffer) =
    CreditCard(o.provider, Range(o.minimumCreditScore.getOrElse(0), o.maximumCreditScore.getOrElse(0)))

  override def getOffers: CreditScore => Future[Seq[Offer]] = creditScore =>
    client.getOffers(creditScore).map(_.offers.map(convertOffer))

  def loadOffers(): OffersRepo =
    client.getOffers(100).map(_.offers.map(convertOffer))

}
