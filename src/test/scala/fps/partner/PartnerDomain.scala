package fps

import org.scalatest._

class UserSystemSpec extends FlatSpec with Matchers {

  "UserSystem" should
    "allow finding a user given a RepositoryFilter" in {
      val API: UserSystem = UserInMemory
      API.find(5) should be(None)
      API.find(2).get.name should be("TestUser02")
    }

  "UserInMemory" should
    "allow finding a user given a RepositoryFilter" in {
      UserInMemory.find(5) should be(None)
      UserInMemory.find(2).get.name should be("TestUser02")
    }

}