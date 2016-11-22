package krs

trait OfferTrait {
  var minimumCreditScore: Short
  var maximumCreditScore: Short

  def isEligable(creditScore: Short): Boolean = {
    creditScore >= minimumCreditScore && creditScore <= maximumCreditScore
  }
}

class CreditCardOffer(val provider: String, minScore: Short, maxScore: Short) extends OfferTrait {
  var minimumCreditScore = minScore
  var maximumCreditScore = maxScore
}
