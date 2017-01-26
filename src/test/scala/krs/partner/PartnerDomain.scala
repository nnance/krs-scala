package krs.partner

import com.twitter.util.{Await, Future}
import org.scalatest._

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

  "filterOffers" should "have 5 items for 550 score" in {
    val eligable = PartnerSystem.filterOffers(offers, 550)
    eligable.length should be(5)
  }

  "filterOffers" should "have 1 item for 750 score" in {
    val eligable = PartnerSystem.filterOffers(offers, 750)
    eligable.length should be(1)
  }

  "getOffers" should "have 5 items for 550 score" in {
    val eligable = Await.result(PartnerSystem.getOffersFromRepo(Future.value(offers), 550))
    eligable.length should be(5)
  }
}
