package krs.rest

import com.twitter.finagle.Thrift

import io.finch._
import krs.thriftscala.{PartnerService, PartnerOffer}

case class Offer(provider: String, minScore: Int, maxScore: Int)

object PartnerAPI {
  private val conf = com.typesafe.config.ConfigFactory.load()
  private val host = conf.getString("krs.partner.host")

  private val client: PartnerService.FutureIface =
    Thrift.client.newIface[PartnerService.FutureIface](host, classOf[PartnerService.FutureIface])

  private def convertOffer(o: PartnerOffer) =
    Offer(o.provider, o.minimumCreditScore.getOrElse(0), o.maximumCreditScore.getOrElse(0))

  def getOffers: Endpoint[Seq[Offer]] = get("offers" :: int) { creditScore: Int =>
    client.getOffers(creditScore).map(resp => Ok(resp.offers.map(convertOffer)))
  }
}
