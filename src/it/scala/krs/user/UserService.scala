package krs.user

import com.twitter.util.Await
import org.scalatest._
import krs.eligibility.EligibilitySystem
import krs.user.UserDomain.UserNotFound
import krs.user.service.PartnerLocalClient

object TestIntegrationInfrastructure extends ServiceInfrastructure {
  val filter = EligibilitySystem.filterEligible
  override val partnerClient = PartnerLocalClient()
}

// scalastyle:off magic.number
class UserServiceSpec extends FlatSpec with Matchers {

  val service = TestIntegrationInfrastructure()

  "getUser for id 2" should "have TestUser02 for the second user" in {
    val user = Await.result(service.getUser(2))
    user.name should be("TestUser02")
  }

  "getUser for id 6" should "be undefined" in {
    val error = try {
      Await.result(service.getUser(6))
      false
    } catch {
      case UserNotFound(msg) => true
    }
    error should be(true)
  }

  "getUserWithOffers for id 2" should "have 2 offers" in {
    val userWithOffers = Await.result(service.getUserWithOffers(2))
    userWithOffers.offers.get.length should be(2)
  }
}
