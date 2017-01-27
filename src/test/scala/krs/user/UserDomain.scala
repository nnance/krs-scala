package krs.user

import com.twitter.util.Await
import org.scalatest._
import UserSystem._

class UserSystemSpec extends FlatSpec with Matchers {

  "getUser for id 2" should "have TestUser02 for the second user" in new TestModule {
    val user = getUser(2)
    user.get.name should be("TestUser02")
  }

  "getUser for id 6" should "be undefined" in new TestModule {
    val user = getUser(6)
    user.isDefined should be(false)
  }

  "getUserWithOffers for id 2" should "have 2 offers" in new TestModule {
    val user = Await.result(getUserWithOffers(2))
    user.get.offers.length should be(2)
  }
}
