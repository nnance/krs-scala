package krs.user

import com.twitter.util.{Await, Future}
import org.scalatest._

trait TestInfrastructure extends
  UserSystem with
  krs.partner.ServiceInfrastructure {

  import UserDomain._
  import krs.partner.PartnerDomain._
  import krs.eligibility.EligibilitySystem

  private val conf = com.typesafe.config.ConfigFactory.load()
  private val userData = conf.getString("krs.user.data")

  val repo = UserFileRepository(userData)
  val filter = EligibilitySystem.filterEligible

  def getUser: GetUser = repo.getUser

  def getUserWithOffers: CreditScore => Future[Option[UserWithOffers]] =
    getUserWithOffers(getUser, getOffersFromRepo, filter, _)
}

// scalastyle:off magic.number
class UserSystemSpec extends FlatSpec with Matchers with TestInfrastructure {

  "getUser for id 2" should "have TestUser02 for the second user" in {
    val user = getUser(2)
    user.get.name should be("TestUser02")
  }

  "getUser for id 6" should "be undefined" in {
    val user = getUser(6)
    user.isDefined should be(false)
  }

  "getUserWithOffers for id 2" should "have 2 offers" in {
    val userWithOffers = Await.result(getUserWithOffers(2))
    userWithOffers.get.offers.length should be(2)
  }
}
