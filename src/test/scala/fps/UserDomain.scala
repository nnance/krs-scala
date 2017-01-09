package fps

import org.scalatest._

class UserSystemSpec extends FlatSpec with Matchers {

  "InMemoryUserRepository" should
    "allow us to find a user given the domain function find:Long => Option[User]" in {
      InMemoryUserRepository.find(5) should be(None)
      InMemoryUserRepository.find(2).get.name should be("TestUser02")
    }

}