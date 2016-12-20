package krs.api

import org.scalatest.{ FlatSpec, Matchers }
import krs.TestModule

class UserApiSpec extends FlatSpec with Matchers {
  "loadUsers" should "have 4 items" in new TestModule {
    val users = userApi.getUsers()
    users.length should be(4)
    users(0).name should be("TestUser01")
  }
}
