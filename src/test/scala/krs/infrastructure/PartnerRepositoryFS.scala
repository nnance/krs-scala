package krs.infrastructure

import org.scalatest._

class PartnerFSSpec extends FlatSpec with Matchers {
  val fixtureData = "./fixtures/offers.json"

  "Read file" should "be 2147 characters for fixture data file" in {
    val repo = new PartnerRepositoryFS(fixtureData)
    repo.readFile(fixtureData).length should be(2147)
  }

  "loadOffers" should "have 7 items from CapitalOne" in {
    val repo = new PartnerRepositoryFS(fixtureData)
    val offers = repo.loadOffers()
    offers.length should be(7)
    offers(0).provider should be("CapitalOne")
  }
}
