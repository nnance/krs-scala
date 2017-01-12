package fps

object PartnerDomain {
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
}

trait PartnerSystem {
  import PartnerDomain._

  def filterOffers(offers: List[Offer], creditScore: Int): List[Offer] =
    offers.filter(o => creditScore >= o.creditScoreRange.min && creditScore <= o.creditScoreRange.max)
}

case class PartnerSystem() extends PartnerDPartnerSystemomain {
  def filterOffers(offers: List[Offer], creditScore: Int): List[Offer] =
    offers.filter(o => creditScore >= o.creditScoreRange.min && creditScore <= o.creditScoreRange.max)
}
