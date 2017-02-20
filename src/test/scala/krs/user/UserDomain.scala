package krs.user

import com.twitter.util.Await
import org.scalatest._
import krs.partner.{PartnerMemoryRepositoryComponent, PartnerServerComponent}
import krs.eligibility.EligibilitySystemComponent

trait TestModule extends
  UserServiceComponent with
  UserMemoryRepositoryComponent with
  PartnerServerComponent with
  PartnerMemoryRepositoryComponent with
  EligibilitySystemComponent {

  val userRepository = UserMemoryRepository()
  val userService = UserService()
  val partnerRepository = PartnerMemoryRepository()
  val partnerSystem = PartnerServer()
}

class UserSystemSpec extends
  FlatSpec with
  Matchers with
  TestModule {

  "getUser for id 2" should "have TestUser02 for the second user" in {
    val user = userService.getUser(2)
    user.get.name should be("TestUser02")
  }

  "getUser for id 6" should "be undefined" in {
    val user = userService.getUser(6)
    user.isDefined should be(false)
  }

  "getUserWithOffers for id 2" should "have 2 offers" in {
    val user = Await.result(userService.getUserWithOffers(2))
    user.get.offers.length should be(2)
  }
}
