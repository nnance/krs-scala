import org.scalatest._
import krs._

class OfferSpec extends FlatSpec with Matchers {
  val offer = OfferTypes.CreditCard("Chase", Range(500, 700))

  "Offer provider" should "be Chase for new offer" in {
    offer.provider should be("Chase")
  }

  // "Offer isEligable" should "be true for credit score within range" in {
  //   offer.isEligable(User("Test", 500, 0.0)) should be(true)
  // }
  //
  // "Offer isEligable" should "be false for credit score outside of range" in {
  //   offer.isEligable(User("Test", 701, 0.0)) should be(false)
  // }
}
