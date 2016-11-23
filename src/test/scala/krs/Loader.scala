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
    val user = User("Test", 500, 30000.00)
    Loader.getOffersByUser(user).length should be(2)
  }

  "Offers count filtered by a user with total debt out of range" should "be 1" in {
    val user = User("Test", 500, 50000.00)
    Loader.getOffersByUser(user).length should be(1)
  }

  "Offers count filtered by a user completely out of range" should "be 0" in {
    val user = User("Test", 300, 50000.00)
    Loader.getOffersByUser(user).length should be(0)
  }
}
