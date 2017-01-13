package krs.eligibility.domain

import org.scalatest._
import krs.user.{User}
import krs.partner.domain.{CreditCard, PersonalLoan}

import OfferSystem._

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
