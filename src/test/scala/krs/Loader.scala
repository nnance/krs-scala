import org.scalatest._
import krs.User
import krs.OfferTrait
import krs.Loader

class LoaderSpec extends FlatSpec with Matchers {
  val offers = Loader.loadOffers()

  "Offers count" should "be 2" in {
    offers.length should be(2)
  }

  "Offers count filtered by a user" should "be 2" in {
    val user = new User("Test", 500, 30000.00)
    offers.filter(Loader.filterOfferByUser(user)).length should be(2)
  }
}
