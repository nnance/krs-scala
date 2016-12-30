package krs.user.api

import org.scalatest._
import krs.user.TestModule

class UserApiSpec extends FlatSpec with Matchers {
  "getUser for id 2" should "have a name TestUser02" in new TestModule {
    val user = userApi.getUser(2)
    user.get.name should be("TestUser02")
  }

  "getUser for id 6" should "be undefined" in new TestModule {
    val user = userApi.getUser(6)
    user.isDefined should be(false)
  }
}
