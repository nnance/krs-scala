package krs.user

import com.twitter.util.Await
import org.scalatest._
import UserSystem._
import krs.eligibility.EligibilitySystem
import krs.partner.PartnerMemoryRepository

trait TestInfrastructure {
  val userRepo = UserMemoryRepository().loadUsers()
  val offersRepo = PartnerMemoryRepository().getOffers
  val filter = EligibilitySystem.filterEligible
}

class UserSystemSpec extends FlatSpec with Matchers with TestInfrastructure {

  "getUser for id 2" should "have TestUser02 for the second user" in {
    val user = getUser(userRepo, 2)
    user.get.name should be("TestUser02")
  }

  "getUser for id 6" should "be undefined" in {
    val user = getUser(userRepo, 6)
    user.isDefined should be(false)
  }

  "getUserWithOffers for id 2" should "have 2 offers" in {
    val user = Await.result(getUserWithOffers(offersRepo, filter, userRepo, 2))
    user.get.offers.length should be(2)
  }
}
