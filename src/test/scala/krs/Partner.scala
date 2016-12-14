import org.scalatest._
import krs.PartnerSystem._

class LoaderSpec extends FlatSpec with Matchers {
  "Read file" should "be 2147 characters for fixture data file" in {
    readFile("./fixtures/offers.json").length should be (2147)
  }

  "loadOffers" should "have 7 items from CapitalOne" in {
    val offers = loadOffers(readFile("./fixtures/data.json"))
    offers.length should be (7)
    offers(0).provider should be ("CapitalOne")
  }
}
