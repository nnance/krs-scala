import org.scalatest._
import krs.Offer

class OfferSpec extends FlatSpec with Matchers {
  val offer = new Offer("Chase", 500, 700)

  "Offer name" should "be Chase for new offer" in {
    offer.provider should be("Chase")
  }

  "Offer isEligable" should "be true for credit score within range" in {
    offer.isEligable(500) should be(true)
  }

  "Offer isEligable" should "be false for credit score outside of range" in {
    offer.isEligable(701) should be(false)
  }
}
