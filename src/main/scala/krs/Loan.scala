package krs

trait LoanOfferTrait extends OfferTrait {
  val maximumDebt: Double

  override def isEligable(user: User): Boolean = {
    super.isEligable(user) && user.outstandingLoanAmount <= maximumDebt
  }
}

class PersonalLoanOffer(
  provider: String,
  creditScoreRange: Range,
  val term: Short,
  maxDebt: Double)
    extends CreditCardOffer(provider, creditScoreRange)
    with LoanOfferTrait {
      val maximumDebt = maxDebt
}
