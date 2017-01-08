package krs.user.domain

import org.scalatest._

object TestRepository extends UserRepository {
  def loadUsers(): List[User] = {
    List(
      new User(1, "TestUser01", 500, 100.00),
      new User(2, "TestUser02", 765, 100.00),
      new User(3, "TestUser03", 500, 0.00),
      new User(4, "TestUser04", 765, 0.00)
    )
  }
}

class UserSystemSpec extends FlatSpec with Matchers {

  "find for id 2" should "have TestUser02 for the second user" in {
    val api = UserSystem(TestRepository)
    val user = api.find(2)
    user.get.name should be("TestUser02")
  }

  "find for id 6" should "be undefined" in {
    val api = UserSystem(TestRepository)
    val user = api.find(6)
    user.isDefined should be(false)
  }
}
