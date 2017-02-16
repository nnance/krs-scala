package krs.user

import com.twitter.util.Await
import org.scalatest._
import krs.eligibility.EligibilitySystem
import krs.partner.PartnerMemoryRepository

trait TestInfrastructure {
  val repo = UserMemoryRepository()
  val getOffers = PartnerMemoryRepository().getOffers
  val filter = EligibilitySystem.filterEligible
  val userSystem = new UserSystem(repo, getOffers, filter)
}

class UserSystemSpec extends FlatSpec with Matchers with TestInfrastructure {

  "getUser for id 2" should "have TestUser02 for the second user" in {
    val user = userSystem.getUser(2)
    user.get.name should be("TestUser02")
  }

  "getUser for id 6" should "be undefined" in {
    val user = userSystem.getUser(6)
    user.isDefined should be(false)
  }

  "getUserWithOffers for id 2" should "have 2 offers" in {
    val userWithOffers = Await.result(userSystem.getUserWithOffers(2))
    userWithOffers.get.offers.length should be(2)
  }
}
