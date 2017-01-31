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

object UserSystem {
  import UserDomain._

  type UserRepo = List[User]
  type GetUser = Int => Option[User]

  case class UserNotFound(id: Int) extends Exception {
    override def getMessage: String = s"User(${id.toString}) not found."
  }

  def getUser(repository: UserRepo, id: Int): Option[User] =
    repository.find(_.id == id)

  def getUserWithOffers(user: Option[User],
                        getOffers: GetOffers,
                        filter: EligibilityFilter): Future[Option[UserWithOffers]] =
    user match {
      case Some(u) =>
        for {
          offers <- getOffers(u.creditScore)
          eligible <- filter(u, offers)
        } yield Option(UserWithOffers(u, eligible))
      case None => Future.value(None)
    }
}
