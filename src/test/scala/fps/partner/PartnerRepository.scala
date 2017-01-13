package fps.partner

import org.scalatest._

class PartnerRepositorySpec extends FlatSpec with Matchers {

  val fixtureData = "./fixtures/offers.json"

  "Read file" should "be 2147 characters for fixture data file" in {
    val repo = new FilePartnerRepository(fixtureData)
    repo.readFile(fixtureData).length should be(2147)
  }

  "loadOffers" should "have 7 items" in {
    val offers = FilePartnerRepository(fixtureData).getOffers
    offers.length should be(7)
  }

}