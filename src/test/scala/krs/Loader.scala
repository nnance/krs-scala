import org.scalatest._
import krs.Loader

class LoaderSpec extends FlatSpec with Matchers {
  val offers = Loader.loadOffers()

  "Offers count" should "be 2" in {
    offers.length should be(2)
  }
}
