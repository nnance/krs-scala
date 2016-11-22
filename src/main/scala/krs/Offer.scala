package krs

trait OfferTrait {
  var minimumCreditScore: Short
  var maximumCreditScore: Short

  def isEligable(user: User): Boolean = {
    user.creditScore >= minimumCreditScore && user.creditScore <= maximumCreditScore
  }
}

class CreditCardOffer(val provider: String, minScore: Short, maxScore: Short) extends OfferTrait {
  var minimumCreditScore = minScore
  var maximumCreditScore = maxScore
}
