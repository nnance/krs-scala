package krs.partner.api

import org.scalatest._
import krs.partner.TestModule

class PartnerApiSpec extends FlatSpec with Matchers {
  "filterOffers" should "have 5 items for 550 score" in new TestModule {
    val eligable = partnerApi.getOffers(550)
    eligable.length should be(5)
  }
}
