import org.scalatest._
import krs.Loader._

class LoaderSpec extends FlatSpec with Matchers {
  "Read file" should "be 2147 characters for fixture data file" in {
    readFile("./fixtures/data.json").length should be (2147)
  }

  "loadOffers" should "have 3 items from CapitalOne" in {
    val offers = loadOffers(readFile("./fixtures/data.json"))
    offers.length should be (3)
    offers(0).provider should be ("CapitalOne")
  }
}
