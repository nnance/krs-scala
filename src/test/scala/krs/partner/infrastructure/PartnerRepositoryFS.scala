package krs.partner.infrastructure

import org.scalatest._
import com.twitter.util.{ Await }

class PartnerFSSpec extends FlatSpec with Matchers {
  val fixtureData = "./fixtures/offers.json"

  "Read file" should "be 2147 characters for fixture data file" in {
    val repo = new PartnerRepositoryFS(fixtureData)
    val offers = Await.result(repo.readFile(fixtureData))
    offers.length should be(2147)
  }

  "loadOffers" should "have 7 items from CapitalOne" in {
    val repo = new PartnerRepositoryFS(fixtureData)
    val offers = Await.result(repo.loadOffers())
    offers.length should be(7)
    offers(0).provider should be("CapitalOne")
  }
}
