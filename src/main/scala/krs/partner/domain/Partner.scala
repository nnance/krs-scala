package krs.partner.domain

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
  val maxLoanAmount: Double,
  val term: Long
) extends Offer

trait PartnerRepository {
  def loadOffers: List[Offer]
}

trait PartnerDomain {
  def filterOffers(offers: List[Offer], creditScore: Int): List[Offer]
}

case class PartnerSystem() extends PartnerDomain {
  def filterOffers(offers: List[Offer], creditScore: Int): List[Offer] =
    offers.filter(o => creditScore >= o.creditScoreRange.min && creditScore <= o.creditScoreRange.max)
}
