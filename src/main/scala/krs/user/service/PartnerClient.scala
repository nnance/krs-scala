package krs.user.service

import com.twitter.finagle.Thrift
import com.twitter.util.Future
import krs.partner.{PartnerService, PartnerServiceComponent}
import krs.thriftscala.PartnerOffer


trait PartnerClientComponent {

  val partnerService: PartnerService

  case class PartnerClient() extends PartnerService {

    import krs.partner.PartnerDomain._

    private val conf = com.typesafe.config.ConfigFactory.load()
    private val partnerHost = conf.getString("krs.partner.host")

    private val client: krs.thriftscala.PartnerService.FutureIface =
      Thrift.client.newIface[krs.thriftscala.PartnerService.FutureIface](partnerHost, classOf[krs.thriftscala.PartnerService.FutureIface])

    private def convertOffer(o: PartnerOffer) =
      CreditCard(o.provider, Range(o.minimumCreditScore.getOrElse(0), o.maximumCreditScore.getOrElse(0)))

    override def getOffers(creditScore: Int): Future[Seq[Offer]] = {
      client.getOffers(creditScore).map(_.offers.map(convertOffer))
    }
  }

}