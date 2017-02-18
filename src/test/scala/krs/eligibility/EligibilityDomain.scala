package krs.eligibility

import com.twitter.util.Await
import krs.partner.PartnerDomain.{CreditCard, PersonalLoan}
import krs.user.UserDomain.User
import org.scalatest._

class CreditCardOfferSpec extends FlatSpec with Matchers with EligibilitySystemComponent {
  val ccOffer = CreditCard("Chase", Range(500, 700))

  "isEligable" should "be true for user with score within range" in {
    eligibilitySystem.isEligible(User(1, "Test", 500, 0.0), ccOffer) should be(true)
  }

  "isEligable" should "be false for user with score outside of range" in {
    eligibilitySystem.isEligible(User(1, "Test", 499, 0.0), ccOffer) should be(false)
  }
}

class PersonalLoanOfferSpec extends FlatSpec with Matchers with EligibilitySystemComponent {
  val plOffer = PersonalLoan("Chase", Range(500, 700), 400.00, 12)

  "isEligable" should "be true for user with score within range" in {
    eligibilitySystem.isEligible(User(1, "Test", 500, 300.0), plOffer) should be(true)
  }

  "isEligable" should "be false for user with score outside of range" in {
    eligibilitySystem.isEligible(User(1, "Test", 499, 300.0), plOffer) should be(false)
  }

  "isEligable" should "be false for user with loan amount outside of range" in {
    eligibilitySystem.isEligible(User(1, "Test", 500, 400.0), plOffer) should be(false)
  }
}

class FilterEligibleOffersSpec extends FlatSpec with Matchers with EligibilitySystemComponent {
  // Offer01 should be the only thing that user01 is eligible for
  val user01 = new User(1, "TestUser01", 500, 100.00)
  // Offer05 and Offer06 should be the only thing that user02 is eligible for
  val user02 = new User(1, "TestUser02", 765, 100.00)
  // Offer01 and Offer09 should be the only thing that user03 is eligible for
  val user03 = new User(1, "TestUser03", 500, 0.00)
  // Offer05, Offer06, offer10 should be the only thing that user04 is eligible for
  val user04 = new User(1, "TestUser04", 765, 0.00)

  val offers = List(
    CreditCard("Offer01", Range(500, 700)),
    CreditCard("Offer02", Range(550, 700)),
    CreditCard("Offer03", Range(600, 700)),
    CreditCard("Offer04", Range(650, 700)),
    CreditCard("Offer05", Range(700, 770)),
    CreditCard("Offer06", Range(750, 770)),
    PersonalLoan("Offer07", Range(500, 700), 0.00, 12),
    PersonalLoan("Offer08", Range(550, 700), 0.00, 12),
    PersonalLoan("Offer09", Range(500, 700), 100.00, 12),
    PersonalLoan("Offer10", Range(750, 770), 100.00, 12)
  )

  "Offer eligibilityRules length" should "be 1 for low scoreband user" in {
    val eligibleOffers = Await.result(eligibilitySystem.filterEligible(user01, offers))
    eligibleOffers.size should be(1)
    eligibleOffers.head.provider should be("Offer01")
  }

  "Offer eligibilityRules length" should "be 2 for high scoreband user" in {
    val eligibleOffers = Await.result(eligibilitySystem.filterEligible(user02, offers))
    eligibleOffers.size should be(2)
    eligibleOffers.head.provider should be("Offer05")
  }

  "Offer eligibilityRules length" should "be 2 for no outstanding loans" in {
    val eligibleOffers = Await.result(eligibilitySystem.filterEligible(user03, offers))
    eligibleOffers.size should be(2)
    eligibleOffers.head.provider should be("Offer01")
  }

  "Offer eligibilityRules length" should "be 3 for no outstanding loans" in {
    val eligibleOffers = Await.result(eligibilitySystem.filterEligible(user04, offers))
    eligibleOffers.size should be(3)
    eligibleOffers.head.provider should be("Offer05")
  }
}