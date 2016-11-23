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
}
