package krs.infrastructure

import org.scalatest._

class UserRepositoryFSSpec extends FlatSpec with Matchers {
  val fixtureData = "./fixtures/users.json"

  "loadUsers" should "have 4 items" in {
    val repo = UserRepositoryFS(fixtureData)
    val users = repo.loadUsers()
    users.length should be(4)
    users(0).name should be("TestUser01")
  }

  "getUser for id 2" should "have TestUser02 for the second user" in {
    val repo = UserRepositoryFS(fixtureData)
    val user = repo.getUser(2)
    user.get.name should be("TestUser02")
  }

  "getUser for id 6" should "be undefined" in {
    val repo = UserRepositoryFS(fixtureData)
    val user = repo.getUser(6)
    user.isDefined should be(false)
  }
}
