package krs.partner

import com.twitter.util.Future

object PartnerDomain {
  type CreditScore = Int

  sealed trait Offer {
    val provider: String
    val creditScoreRange: Range
  }

  case class CreditCard(
    provider: String,
    creditScoreRange: Range
  ) extends Offer

  case class PersonalLoan(
    provider: String,
    creditScoreRange: Range,
    maxLoanAmount: Double,
    term: Long
  ) extends Offer

  type GetAll = () => Future[Seq[Offer]]
  type GetOffers = CreditScore => Future[Seq[Offer]]
}

trait OfferRepository {
  import PartnerDomain._

  def getAll: GetAll
}

trait PartnerSystem {
  import PartnerDomain._

  private def isWithinRange(o: Offer, score: CreditScore): Boolean =
    score >= o.creditScoreRange.min && score <= o.creditScoreRange.max


  def getOffers: GetAll => GetOffers = getAll => creditScore =>
    getAll().map(_.filter(isWithinRange(_, creditScore)))
}
