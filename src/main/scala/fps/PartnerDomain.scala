package fps

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

  sealed trait OfferFilter[T] {
    def isEligible(offer: T, creditScore: Int): Boolean
  }
}

trait PartnerSystem {
  import PartnerDomain._

  protected implicit object CreditCardFilter extends OfferFilter[CreditCard] {
    def isEligible(o: CreditCard, creditScore: Int): Boolean =
      creditScore >= o.creditScoreRange.min && creditScore <= o.creditScoreRange.max
  }

  protected implicit object PersonalLoanFilter extends OfferFilter[PersonalLoan] {
    def isEligible(o: PersonalLoan, creditScore: Int): Boolean =
      creditScore >= o.creditScoreRange.min && creditScore <= o.creditScoreRange.max
  }

  protected def isEligible[T](t: T, creditScore: Int)(implicit filter: OfferFilter[T]) =
    filter.isEligible(t, creditScore)

  def getOffers: Int => List[Offer]
}

object PartnerInMemory extends PartnerSystem {
  import PartnerDomain._

  def getOffers(n: Int): List[Offer] =
    InMemoryPartnerRepository.getOffers.filter(isEligible(_, n))
}
