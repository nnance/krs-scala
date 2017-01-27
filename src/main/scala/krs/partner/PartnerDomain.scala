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
}

object PartnerSystem {
  import PartnerDomain._

  type OffersRepo = Future[Seq[Offer]]
  type GetOffers = CreditScore => Future[Seq[Offer]]

  def filterOffers(offers: Seq[Offer], creditScore: CreditScore): Seq[Offer] =
    offers.filter(o => creditScore >= o.creditScoreRange.min && creditScore <= o.creditScoreRange.max)

  def getOffersFromRepo(offers: OffersRepo, creditScore: CreditScore): Future[Seq[Offer]] =
    offers.map(filterOffers(_, creditScore))
}
