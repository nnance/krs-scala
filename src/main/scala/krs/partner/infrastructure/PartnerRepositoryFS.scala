package krs.partner.infrastructure

import com.twitter.util.{ Future, FuturePool }

import io.circe._
import io.circe.syntax._
import io.circe.generic.auto._
import io.circe.parser._

import krs.common.{ FileSystem }
import krs.partner.domain._

case class JsonOffer(
  id: Int,
  provider: String,
  minimumCreditScore: Int,
  maximumCreditScore: Int,
  maximumAmount: Option[Double],
  term: Option[Int])

case class OfferType(value: String)

case class PartnerRepositoryFS(val fileName: String) extends FileSystem with PartnerRepository {

  implicit val fooKeyDecoder = new KeyDecoder[OfferType] {
    override def apply(key: String): Option[OfferType] = Some(OfferType(key))
  }

  def readJsonOffer(source: String): List[Map[OfferType, JsonOffer]] = {
    decode[List[Map[OfferType, JsonOffer]]](source).getOrElse(List())
  }

  def loadOffers(): Future[List[Offer]] = {
    readFile(fileName).map(json => {
      readJsonOffer(json).flatMap(m => m.keySet.map(k => {
        k.value match {
          case "creditCard" =>
            m.get(k).map(o => CreditCard(o.provider, Range(o.minimumCreditScore, o.maximumCreditScore)))
          case "personalLoan" =>
            m.get(k).map(o => PersonalLoan(o.provider, Range(o.minimumCreditScore, o.maximumCreditScore), o.maximumAmount.getOrElse(0.0), o.term.getOrElse(0).toLong))
          case _ =>
            None
        }
      })).flatten
    })
  }

}
