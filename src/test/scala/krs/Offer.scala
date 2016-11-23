import org.scalatest._
import krs.User
import krs.CreditCardOffer

class OfferSpec extends FlatSpec with Matchers {
  val offer = new CreditCardOffer("Chase", 500, 700)

  "Offer provider" should "be Chase for new offer" in {
    offer.provider should be("Chase")
  }

  "Offer isEligable" should "be true for credit score within range" in {
    import krs._
    import krs.Eligibility._

    // Original
    offer.isEligable(new User("Test", 500, 0.0)) should be(true)

    // Added for OfferT
    val offerT = CCOffer("Chase", Range(500, 700))
    isEligable(offerT, new User("Test", 500, 0.0)) should be(true)
  }

  "Offer isEligable" should "be false for credit score outside of range" in {
    offer.isEligable(new User("Test", 701, 0.0)) should be(false)
  }

  /*
   * This test is a lot bigger than tests that I would normally do, but it tests
   * a few things and is just here to show a usage of how you would filter
   * a list to satisfy the requirement that you only return offers that the user
   * is eligable for
   */
  "Eligibility Checks" should "return all offers that the user is eligible for" in {
    import krs._
    import krs.Eligibility._

    // Offer01 should be the only thing that user01 is eligible for
    val user01 = new User("TestUser01", 500, 0.0)

    // Offer06 and Offer07 should be the only thing that user02 is eligible for
    val user02 = new User("TestUser02", 765, 0.0)


    val offers = List(
      CCOffer("Offer01", Range(500, 700)),
      CCOffer("Offer02", Range(550, 700)),
      CCOffer("Offer03", Range(600, 700)),
      CCOffer("Offer04", Range(650, 700)),
      CCOffer("Offer05", Range(700, 770)),
      CCOffer("Offer06", Range(750, 770)))

    val eligibleOffers = offers.filter(offer => isEligable(offer, user01))
    eligibleOffers.size should be(1)
    eligibleOffers.head.provider should be("Offer01")

    val eligibleOffers2 = offers.filter(offer => isEligable(offer, user02))
    eligibleOffers2.size should be(2)
  }
}
