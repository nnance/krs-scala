package krs

trait OfferTrait {
  val creditScoreRange: Range

  def isEligable(user: User): Boolean = {
    user.creditScore >= creditScoreRange.min &&
    user.creditScore <= creditScoreRange.max
  }
}

case class CreditCardOffer(
  provider: String,
  scoreRange: Range)
    extends OfferTrait {
      val creditScoreRange = scoreRange
}
