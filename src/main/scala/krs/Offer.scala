package krs

class Offer(val provider: String, val minimumCreditScore: Short, val maximumCreditScore: Short) {
  def isEligable(creditScore: Short): Boolean = {
    creditScore >= minimumCreditScore && creditScore <= maximumCreditScore
  }
}
