package krs.partner

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import krs.common.FileSystem

trait PartnerFileRepositoryComponent extends PartnerRepositoryComponent {

  val partnerRepository: PartnerRepository

  case class JsonOffer(
                        id: Int,
                        provider: String,
                        minimumCreditScore: Int,
                        maximumCreditScore: Int,
                        maximumAmount: Option[Double],
                        term: Option[Int]
                      )

  case class OfferType(value: String)

  case class PartnerFileRepository(fileName: String) extends FileSystem with PartnerRepository {

    import PartnerDomain._

    private implicit val offerKeyDecoder = new KeyDecoder[OfferType] {
      override def apply(key: String): Option[OfferType] = Some(OfferType(key))
    }

    private def readJsonOffer(source: String): List[Map[OfferType, JsonOffer]] =
      decode[List[Map[OfferType, JsonOffer]]](source).getOrElse(List())

    def loadOffers(): List[Offer] = {
      val json = readFile(fileName)
      readJsonOffer(json).flatMap(m => m.keySet.map(k =>
        k.value match {
          case "creditCard" =>
            m.get(k).map(o =>
              CreditCard(
                o.provider,
                Range(o.minimumCreditScore, o.maximumCreditScore)
              ))
          case "personalLoan" =>
            m.get(k).map(o =>
              PersonalLoan(
                o.provider,
                Range(o.minimumCreditScore, o.maximumCreditScore),
                o.maximumAmount.getOrElse(0.0),
                o.term.getOrElse(0).toLong
              ))
          case _ =>
            None
        })).flatten
    }

  }

}

// scalastyle:off magic.number
trait PartnerMemoryRepositoryComponent extends PartnerRepositoryComponent {

  val partnerRepository: PartnerRepository

  case class PartnerMemoryRepository() extends PartnerRepository {

    import PartnerDomain._

    def loadOffers(): List[Offer] =
      List(
        CreditCard("Offer01", Range(500, 700)),
        CreditCard("Offer02", Range(550, 700)),
        CreditCard("Offer03", Range(600, 700)),
        CreditCard("Offer04", Range(650, 700)),
        CreditCard("Offer05", Range(700, 770)),
        CreditCard("Offer06", Range(750, 770)),
        PersonalLoan("Offer07", Range(500, 700), 0.00, 12),
        PersonalLoan("Offer08", Range(550, 700), 0.00, 12),
        PersonalLoan("Offer09", Range(500, 700), 100.00, 12),
        PersonalLoan("Offer10", Range(750, 770), 100.00, 12)
      )
  }

}
