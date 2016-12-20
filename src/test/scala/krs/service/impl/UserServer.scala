package krs.service.impl

import org.scalatest.{ FlatSpec, Matchers }
import krs.TestModule

class UserServerSpec extends FlatSpec with Matchers {
  "loadUsers" should "have 4 items" in new UserServerImpl {
    val users = userApi.getUsers()
    users.length should be(4)
    users(0).name should be("TestUser01")
  }
}
