package krs

import org.scalatest._
import krs.domain.PartnerSystem._

class LoaderSpec extends FlatSpec with Matchers {
  val fixtureData = "./fixtures/offers.json"

  "Read file" should "be 2147 characters for fixture data file" in {
    readFile(fixtureData).length should be(2147)
  }

  "loadOffers" should "have 7 items from CapitalOne" in {
    val offers = loadOffers(readFile(fixtureData))
    offers.length should be(7)
    offers(0).provider should be("CapitalOne")
  }
}
