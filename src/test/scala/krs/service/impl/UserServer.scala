package krs.service.impl

import com.twitter.util.{ Await }
import org.scalatest.{ FlatSpec, Matchers }

class UserServerSpec extends FlatSpec with Matchers {
  "loadUsers" should "have 4 items" in {
    val server = new UserServerImpl()
    val users = Await.result(server().getUsers())
    users.length should be(4)
    users(0).name should be("TestUser01")
  }
}
