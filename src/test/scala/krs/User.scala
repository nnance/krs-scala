package krs

import org.scalatest._
import krs.UserSystem._

class UserSpec extends FlatSpec with Matchers {
  val fixtureData = "./fixtures/users.json"

  "loadUsers" should "have 4 items from CapitalOne" in {
    val users = loadUsers(readFile(fixtureData))
    users.length should be(4)
    users(0).name should be("TestUser01")
  }
}
