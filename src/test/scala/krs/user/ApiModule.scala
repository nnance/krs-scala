package krs.user

import org.scalatest._
import com.twitter.util.{Await}

trait TestModule extends InfrastructureModule with ApiModule with DomainModule

class UserApiSpec extends FlatSpec with Matchers {
  "getUser for id 2" should "have a name TestUser02" in new TestModule {
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
