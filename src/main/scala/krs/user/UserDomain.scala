package krs.user

import com.twitter.util.Future
import krs.eligibility.EligibilitySystem.EligibilityFilter
import krs.partner.PartnerSystem.GetOffers

object UserDomain {
  import krs.partner.PartnerDomain._

  case class User(
    id: Int,
    name: String,
    creditScore: Int,
    outstandingLoanAmount: Double)

  case class UserWithOffers(
    user: User,
    offers: Seq[Offer]
  )

}

trait UserRepository {
  import UserDomain._

  def getUser: Int => Option[User]
}

case class UserNotFound(id: Int) extends Exception {
  override def getMessage: String = s"User(${id.toString}) not found."
}

case class UserSystem(repo: UserRepository) {
  import UserDomain._

  def getUser: Int => Option[User] = repo.getUser

  def getUserWithOffers(id: Int,
                        getOffers: GetOffers,
                        filter: EligibilityFilter): Future[Option[UserWithOffers]] =
    getUser(id) match {
      case Some(u) =>
        for {
          offers <- getOffers(u.creditScore)
          eligible <- filter(u, offers)
        } yield Option(UserWithOffers(u, eligible))
      case None => Future.value(None)
    }
}
