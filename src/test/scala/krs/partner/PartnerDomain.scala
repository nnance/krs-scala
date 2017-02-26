package krs.partner

import com.twitter.util.{Await, Future}
import org.scalatest._

trait TestInfrastructure extends PartnerSystem {
  import PartnerDomain._

  val repo = PartnerMemoryRepository()

  def getOffersFromRepo: GetOffers = getOffers(repo.getAll)
}

class PartnerSystemSpec extends FlatSpec with Matchers with TestInfrastructure {

  "filterOffers" should "have 1 item for 750 score" in {
    val eligable = Await.result(getOffersFromRepo(750))
    eligable.length should be(3)
  }

  "getOffers" should "have 5 items for 550 score" in {
    val eligable = Await.result(getOffersFromRepo(550))
    eligable.length should be(5)
  }
}
