package krs.api.service

import com.twitter.util.{ Await }
import org.scalatest.{ FlatSpec, Matchers }

import krs.TestServerModule

class UserServerSpec extends FlatSpec with Matchers {
  "loadUsers" should "have 4 items" in new TestServerModule {
    val server = new UserServer(userRepository)
    val users = Await.result(userApi().getUsers())
    users.length should be(4)
    users(0).name should be("TestUser01")
  }
}
