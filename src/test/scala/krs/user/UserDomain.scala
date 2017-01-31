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

  def getUserForRepo: GetUser = getUser(userRepo, _)
}

class UserSystemSpec extends FlatSpec with Matchers with TestInfrastructure {

  "getUser for id 2" should "have TestUser02 for the second user" in {
    val user = getUserForRepo(2)
    user.get.name should be("TestUser02")
  }

  "getUser for id 6" should "be undefined" in {
    val user = getUserForRepo(6)
    user.isDefined should be(false)
  }

  "getUserWithOffers for id 2" should "have 2 offers" in {
    val user = getUserForRepo(2)
    val userWithOffers = Await.result(getUserWithOffers(user, offersRepo, filter))
    userWithOffers.get.offers.length should be(2)
  }
}
