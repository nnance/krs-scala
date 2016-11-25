import org.scalatest._
import krs._

class OfferSpec extends FlatSpec with Matchers {
  val offer = CreditCard("Chase", Range(500, 700))

  "Offer provider" should "be Chase for new offer" in {
    offer.provider should be("Chase")
  }

  "Offer eligibilityRules length" should "be 1 for CreditCard offer" in {
    OfferSystem.eligibilityRules(offer).length should be(1)
  }

  "Offer eligibilityRules length" should "be 2 for PersonalLoan offer" in {
    OfferSystem.eligibilityRules(PersonalLoan("Quicken", Range(500, 700), 12)).length should be(2)
  }
}
