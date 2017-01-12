package fps

import org.scalatest._

class UserRepositorySpec extends FlatSpec with Matchers {

  "InMemoryUserRepository" should
    "allow us to find a user given the domain function find:Long => Option[User]" in {
      InMemoryUserRepository.findFromRepo(5) should be(None)
      InMemoryUserRepository.findFromRepo(2).get.name should be("TestUser02")
    }

  "FileUserRepository" should
    "allow us to find a user given the domain function find:Long => Option[User]" in {
      val repo = FileUserRepository("fixtures/users.json")
      repo.findFromRepo(5) should be(None)
      repo.findFromRepo(2).get.name should be("TestUser02")
    }

}