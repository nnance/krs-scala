package krs.partner

import org.scalatest._

class PartnerFileRepositorySpec extends FlatSpec with Matchers with PartnerFileRepositoryComponent {
  val fixtureData = "./fixtures/offers.json"
  val partnerRepository = PartnerFileRepository(fixtureData)

  "Read file" should "be 2147 characters for fixture data file" in {
    partnerRepository.readFile(fixtureData).length should be(2147)
  }

  "loadOffers" should "have 7 items from CapitalOne" in {
    val offers = partnerRepository.loadOffers()
    offers.length should be(7)
    offers(0).provider should be("CapitalOne")
  }
}
