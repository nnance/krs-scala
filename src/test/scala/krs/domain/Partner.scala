package krs.domain

import org.scalatest._
import krs.TestModule

class PartnerSystemSpec extends FlatSpec with Matchers {
  val fixtureData = "./fixtures/users.json"

  "getPartners" should "have 4 items" in new TestModule {
    val api: PartnerSystem = PartnerSystem(partnerRepository)
    val offers = api.getOffers()
    offers.length should be(7)
  }
}
