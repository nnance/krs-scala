package krs.user

import org.scalatest._

class UserFileRepositorySpec extends FlatSpec with Matchers {
  val fixtureData = "./fixtures/users.json"

  "loadUsers" should "have 4 items" in {
    val repo = UserFileRepository(fixtureData)
    val users = repo.loadUsers()
    users.length should be(4)
    users(0).name should be("TestUser01")
    users(2).name should be("TestUser03")
  }

}
