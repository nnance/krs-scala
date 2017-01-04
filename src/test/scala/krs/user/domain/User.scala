package krs.user.domain

import org.scalatest._
import com.twitter.util.{ Future, Await }

object TestRepository extends UserRepository {
  def loadUsers(): Future[List[User]] = {
    Future.value(List(
      new User(1, "TestUser01", 500, 100.00),
      new User(2, "TestUser02", 765, 100.00),
      new User(3, "TestUser03", 500, 0.00),
      new User(4, "TestUser04", 765, 0.00)
    ))
  }
}

class UserSystemSpec extends FlatSpec with Matchers {

  "getUsers" should "have 4 items" in {
    val api = UserSystem(TestRepository)
    val users = Await.result(api.getUsers())
    users.length should be(4)
    users(0).name should be("TestUser01")
  }

  "getUser for id 2" should "have TestUser02 for the second user" in {
    val api = UserSystem(TestRepository)
    val user = Await.result(api.getUser(2))
    user.get.name should be("TestUser02")
  }

  "getUser for id 6" should "be undefined" in {
    val api = UserSystem(TestRepository)
    val user = Await.result(api.getUser(6))
    user.isDefined should be(false)
  }
}
