package krs.api.service

import com.twitter.util.{ Await }
import org.scalatest.{ FlatSpec, Matchers }

import krs.TestServerModule

class UserServerSpec extends FlatSpec with Matchers {
  "getUsers" should "have 4 items" in new TestServerModule {
    val users = Await.result(userApi().getUsers())
    users.length should be(4)
    users(0).name should be("TestUser01")
  }

  "getUser for id 2" should "have a name TestUser02" in new TestServerModule {
    val user = Await.result(userApi().getUser(2))
    user.name should be("TestUser02")
  }

  "getUserWithOffers for id 1" should "have 4 offers" in new TestServerModule {
    val user = Await.result(userApi().getUserWithOffers(1))
    user.offers.get.length should be(4)
  }

}
