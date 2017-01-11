package fps

import org.scalatest._

class UserSystemSpec extends FlatSpec with Matchers {

  "UserSystem" should
    "allow finding a user given a RepositoryFilter" in {
      object API extends UserSystem
      API.find(5, InMemoryUserRepository.findFromRepo) should be(None)
      API.find(2, InMemoryUserRepository.findFromRepo).get.name should be("TestUser02")
    }

  "UserInMemory" should
    "allow finding a user given a RepositoryFilter" in {
      object API extends UserSystem
      UserInMemory.find(5) should be(None)
      UserInMemory.find(2).get.name should be("TestUser02")
    }

  "InMemoryUserRepository" should
    "allow us to find a user given the domain function find:Long => Option[User]" in {
      InMemoryUserRepository.findFromRepo(5) should be(None)
      InMemoryUserRepository.findFromRepo(2).get.name should be("TestUser02")
    }

}