package krs.user

import com.twitter.util.{Await, Future}
import org.scalatest._

// scalastyle:off magic.number
class UserIntegrationSpec extends FlatSpec with Matchers {

  val service = UserServiceImpl()

  "getUser for id 2" should "have TestUser02 for the second user" in {
    val user = Await.result(service.getUser(2))
    user.name should be("TestUser02")
  }

//  "getUser for id 6" should "be undefined" in {
//    val user = Await.result(service.getUser(6))
//    user should be(false)
//  }

  "getUserWithOffers for id 2" should "have 2 offers" in {
    val userWithOffers = Await.result(service.getUserWithOffers(2))
    userWithOffers.offers.map(_.length should be(2))
  }
}
