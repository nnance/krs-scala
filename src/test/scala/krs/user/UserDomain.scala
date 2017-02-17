package krs.user

import com.twitter.util.Await
import org.scalatest._

trait TestModule extends
  UserServiceComponent with
  UserMemoryRepositoryComponent
{
  val userRepository = UserMemoryRepository()
  val userService = UserService()
}

class UserSystemSpec extends
  FlatSpec with
  Matchers with
  TestModule {

  "getUser for id 2" should "have TestUser02 for the second user" in {
    val user = userService.getUser(2)
    user.get.name should be("TestUser02")
  }

  "getUser for id 6" should "be undefined" in {
    val user = userService.getUser(6)
    user.isDefined should be(false)
  }

//  "getUserWithOffers for id 2" should "have 2 offers" in {
//    val user = Await.result(userApi.getUserWithOffers(2))
//    user.get.offers.length should be(2)
//  }
}
