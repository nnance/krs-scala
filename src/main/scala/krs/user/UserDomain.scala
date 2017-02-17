package krs.user

import com.twitter.util.Future
import krs.eligibility.EligibilityApi

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

  case class UserNotFound(id: Int) extends Exception {
    override def getMessage: String = s"User(${id.toString}) not found."
  }
}

trait UserRepositoryComponent {

  def userRepository: UserRepository

  trait UserRepository {

    import UserDomain._

    def get: Int => Option[User]
  }

}

trait UserServiceComponent {
  this: UserRepositoryComponent =>

  val userService: UserService

  case class UserService() {

    import UserDomain._

    def getUser: Int => Option[User] = userRepository.get

//    def getUserWithOffers(id: Int): Future[Option[UserWithOffers]] = {
//      getUser(id) match {
//        case Some(u) =>
//          for {
//            offers <- partnerRepository.getOffers(u.creditScore)
//            eligible <- eligibilitySystem.filterEligible(u, offers)
//          } yield Option(UserWithOffers(u, eligible))
//        case None => Future.value(None)
//      }
//    }
  }

}
