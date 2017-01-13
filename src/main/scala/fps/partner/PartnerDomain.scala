package fps.partner

object PartnerDomain {
  sealed trait Offer

  case class CreditCard(
    val provider: String,
    val creditScoreRange: Range
  ) extends Offer

  case class PersonalLoan(
    val provider: String,
    val creditScoreRange: Range,
    val maxLoanAmount: Double,
    val term: Long
  ) extends Offer
}

trait PartnerSystem {
  import PartnerDomain._

  def getOffers(): List[Offer]
}
