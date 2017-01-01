package krs.eligibility.domain

import org.scalatest._
import krs.user.domain.{ User }
import krs.partner.domain.{ CreditCard, PersonalLoan }

import OfferSystem._

class CreditScoreRangeSpec extends FlatSpec with Matchers {
  val ccRule = CreditScoreRange(Range(500, 700))

  "CreditScoreRange Rule isEligable" should "be true for credit score within range" in {
    isEligible(User(1, "Test", 500, 0.0), ccRule) should be(true)
  }

  "CreditScoreRange Rule isEligable" should "be false for score out of range" in {
    isEligible(User(1, "Test", 499, 30000.00), ccRule) should be(false)
  }
}

class MaxLoanAmountSpec extends FlatSpec with Matchers {
  val loanRule = MaxLoanAmount(40000.00)

  "MaxLoanAmount Rule isEligable" should "be true for max debt within range" in {
    isEligible(User(1, "Test", 500, 39999.99), loanRule) should be(true)
  }

  "MaxLoanAmount Rule isEligable" should "be false for max debt out of range" in {
    isEligible(User(1, "Test", 500, 40000.01), loanRule) should be(false)
  }
}

class CreditCardOfferSpec extends FlatSpec with Matchers {
  val ccOffer = CreditCard("Chase", Range(500, 700))

  "isEligable" should "be true for user with score within range" in {
    isEligible(User(1, "Test", 500, 0.0), ccOffer) should be(true)
  }

  "isEligable" should "be false for user with score outside of range" in {
    isEligible(User(1, "Test", 499, 0.0), ccOffer) should be(false)
  }
}

class PersonalLoanOfferSpec extends FlatSpec with Matchers {
  val plOffer = PersonalLoan("Chase", Range(500, 700), 400.00, 12)

  "isEligable" should "be true for user with score within range" in {
    isEligible(User(1, "Test", 500, 300.0), plOffer) should be(true)
  }

  "isEligable" should "be false for user with score outside of range" in {
    isEligible(User(1, "Test", 499, 300.0), plOffer) should be(false)
  }

  "isEligable" should "be false for user with loan amount outside of range" in {
    isEligible(User(1, "Test", 500, 400.0), plOffer) should be(false)
  }
}
