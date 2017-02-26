package krs.user

import com.twitter.util.{Await, Future}
import org.scalatest._
import krs.eligibility.EligibilitySystem
import krs.partner.PartnerMemoryRepository

trait TestInfrastructure extends UserSystem {
  import UserDomain._

  val repo = UserMemoryRepository()
  val getOffers = PartnerMemoryRepository().getOffers
  val filter = EligibilitySystem.filterEligible

  def getUser: GetUser = repo.getUser

  def getUserWithOffers(id: Int): Future[Option[UserWithOffers]] = super.getUserWithOffers(getUser, getOffers, filter)(id)
}

// scalastyle:off magic.number
class UserSystemSpec extends FlatSpec with Matchers with TestInfrastructure {

  "getUser for id 2" should "have TestUser02 for the second user" in {
    val user = getUser(2)
    user.get.name should be("TestUser02")
  }

  "getUser for id 6" should "be undefined" in {
    val user = getUser(6)
    user.isDefined should be(false)
  }

  "getUserWithOffers for id 2" should "have 2 offers" in {
    val userWithOffers = Await.result(getUserWithOffers(2))
    userWithOffers.get.offers.length should be(2)
  }
}
