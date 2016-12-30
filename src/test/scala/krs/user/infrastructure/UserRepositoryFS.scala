package krs.user.infrastructure

import org.scalatest._

class UserRepositoryFSSpec extends FlatSpec with Matchers {
  val fixtureData = "./fixtures/users.json"

  "loadUsers" should "have 4 items" in {
    val repo = UserRepositoryFS(fixtureData)
    val users = repo.loadUsers()
    users.length should be(4)
    users(0).name should be("TestUser01")
    users(2).name should be("TestUser03")
  }

}
