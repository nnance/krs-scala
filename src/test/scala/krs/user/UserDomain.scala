package krs.user

import com.twitter.util.Await
import org.scalatest._

trait TestModule extends InfrastructureModule with DomainModule

class UserSystemSpec extends FlatSpec with Matchers {

  "getUsers" should "have 4 items" in new TestModule {
    val users = userApi.getUsers()
    users.length should be(4)
    users(0).name should be("TestUser01")
  }

  "getUser for id 2" should "have TestUser02 for the second user" in new TestModule {
    val user = userApi.getUser(2)
    user.get.name should be("TestUser02")
  }

  "getUser for id 6" should "be undefined" in new TestModule {
    val user = userApi.getUser(6)
    user.isDefined should be(false)
  }

  "getUserWithOffers for id 2" should "have 2 offers" in new TestModule {
    val user = Await.result(userApi.getUserWithOffers(2))
    user.get.offers.length should be(2)
  }
}
