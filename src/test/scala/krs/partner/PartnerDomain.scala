package krs.partner

import com.twitter.util.Await
import org.scalatest._

trait TestModule extends InfrastructureModule with DomainModule

class PartnerSystemSpec extends FlatSpec with Matchers {
  import PartnerDomain._

  def offers = List(
    CreditCard("Offer01", Range(500, 700)),
    CreditCard("Offer02", Range(550, 700)),
    CreditCard("Offer03", Range(600, 700)),
    CreditCard("Offer04", Range(650, 700)),
    PersonalLoan("Offer05", Range(500, 700), 0.00, 12),
    PersonalLoan("Offer06", Range(550, 700), 0.00, 12),
    PersonalLoan("Offer07", Range(500, 700), 100.00, 12),
    PersonalLoan("Offer08", Range(750, 770), 100.00, 12)
  )

  "filterOffers" should "have 5 items for 550 score" in new TestModule {
    val eligable = partnerApi.filterOffers(offers, 550)
    eligable.length should be(5)
  }

  "filterOffers" should "have 1 item for 750 score"  in new TestModule {
    val eligable = partnerApi.filterOffers(offers, 750)
    eligable.length should be(1)
  }

  "getOffers" should "have 5 items for 550 score" in new TestModule {
    val eligable = Await.result(partnerApi.getOffers(550))
    eligable.length should be(5)
  }
}

class DecoratorPatternSpec extends FlatSpec with Matchers {
  import DecoratorPattern._

  "isEven" should "return true for an even number" in {
    isEven(2) should be(true)
  }

  "isEven" should "return false for an odd number" in {
    isEven(1) should be(false)
  }

  "getCustomer for id 1" should "return a user" in {
    getCustomer(1).getOrElse(None) should not be(None)
  }

  "getCustomer for id 10" should "return not user" in {
    getCustomer(10).getOrElse(None) should be(None)
  }
}
