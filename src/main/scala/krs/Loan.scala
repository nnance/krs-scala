package krs

trait LoanOfferTrait extends OfferTrait {
  var maximumDebt: Double

  def isEligable(creditScore: Short, currentDebt: Double): Boolean = {
    super.isEligable(creditScore) && currentDebt <= maximumDebt
  }
}

class PersonalLoanOffer(provider: String, minimumCreditScore: Short, maximumCreditScore: Short, val term: Short, maxDebt: Double)
extends CreditCardOffer(provider, minimumCreditScore, maximumCreditScore)
with LoanOfferTrait {
  var maximumDebt = maxDebt
}
