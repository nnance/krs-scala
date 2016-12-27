package krs.domain

import org.scalatest._
import krs.TestModule

class UserSystemSpec extends FlatSpec with Matchers {
  val fixtureData = "./fixtures/users.json"

  "getUsers" should "have 4 items" in new TestModule {
    val api: UserSystem = UserSystem(userRepository, partnerSystem)
    val users = api.getUsers()
    users.length should be(4)
    users(0).name should be("TestUser01")
  }

  "getUser for id 2" should "have TestUser02 for the second user" in new TestModule {
    val api: UserSystem = UserSystem(userRepository, partnerSystem)
    val user = api.getUser(2)
    user.get.name should be("TestUser02")
  }

  "getUserWithOffers for id 2" should "have 7 offers" in new TestModule {
    val api: UserSystem = UserSystem(userRepository, partnerSystem)
    val user = api.getUserWithOffers(2)
    user.get.name should be("TestUser02")
    user.get.offers.length should be(7)
  }

  "getUser for id 6" should "be undefined" in new TestModule {
    val api: UserSystem = UserSystem(userRepository, partnerSystem)
    val user = api.getUser(6)
    user.isDefined should be(false)
  }
}
