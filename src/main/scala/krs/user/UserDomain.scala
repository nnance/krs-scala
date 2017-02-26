package krs.user

import com.twitter.util.Future

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

  type GetUser = Int => Option[User]
}

trait UserRepository {
  import UserDomain._

  def getUser: GetUser
}

trait UserSystem {
  import UserDomain._
  import krs.partner.PartnerDomain._
  import krs.eligibility.EligibilitySystem._

  def getUserWithOffers(getUser: GetUser,
                        getOffers: GetOffers,
                        filter: EligibilityFilter,
                        id: Int): Future[Option[UserWithOffers]] =
    getUser(id) match {
      case Some(u) =>
        for {
          offers <- getOffers(u.creditScore)
          eligible <- filter(u, offers)
        } yield Option(UserWithOffers(u, eligible))
      case None => Future.value(None)
    }
}
