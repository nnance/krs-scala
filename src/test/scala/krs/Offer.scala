import org.scalatest._
import krs.CreditCardOffer

class OfferSpec extends FlatSpec with Matchers {
  val offer = new CreditCardOffer("Chase", 500, 700)

  "Offer provider" should "be Chase for new offer" in {
    offer.provider should be("Chase")
  }

  "Offer isEligable" should "be true for credit score within range" in {
    offer.isEligable(500) should be(true)
  }

  "Offer isEligable" should "be false for credit score outside of range" in {
    offer.isEligable(701) should be(false)
  }
}
