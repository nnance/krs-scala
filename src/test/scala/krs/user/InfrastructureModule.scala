package krs.user

import org.scalatest._

class UserFileRepositorySpec extends FlatSpec with Matchers with UserFileRepositoryComponent {
  val fixtureData = "./fixtures/users.json"
  val userRepository = UserFileRepository(fixtureData)

  "loadUsers" should "have 4 items" in {
    val users = userRepository.loadUsers()
    users.length should be(4)
    users(0).name should be("TestUser01")
    users(2).name should be("TestUser03")
  }

}
