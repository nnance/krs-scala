package krs.domain

import org.scalatest._
import krs.TestModule

import com.twitter.util.{ Await }

class PartnerSystemSpec extends FlatSpec with Matchers {
  val fixtureData = "./fixtures/users.json"

  "getOffers" should "have 10 items" in new TestModule {
    val api: PartnerSystem = PartnerSystem(partnerRepository)
    val offers = Await.result(api.getOffers())
    offers.length should be(10)
  }
}
