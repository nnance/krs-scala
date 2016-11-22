package krs

trait LoanOfferTrait extends OfferTrait {
  var maximumDebt: Double

  override def isEligable(user: User): Boolean = {
    super.isEligable(user) && user.outstandingLoanAmount <= maximumDebt
  }
}

class PersonalLoanOffer(provider: String, minimumCreditScore: Short, maximumCreditScore: Short, val term: Short, maxDebt: Double)
extends CreditCardOffer(provider, minimumCreditScore, maximumCreditScore)
with LoanOfferTrait {
  var maximumDebt = maxDebt
}
