package krs.user.api

import com.twitter.util.{ Future }

import krs.user.domain._
import krs.partner.domain.{ Offer }

case class UserWithOffers(
  user: User,
  offers: Seq[Offer])

case class UserApi(repository: UserRepository, partnerApi: krs.partner.api.PartnerApiTrait) {
  def getUser(id: Int): Option[User] = {
    UserSystem(repository).getUser(id)
  }

  def getUserWithOffers(id: Int): Future[Option[UserWithOffers]] = {
    UserSystem(repository).getUser(id) match {
      case Some(u) => {
        partnerApi.getOffers(u.creditScore).map(offers =>
          Option(UserWithOffers(u, offers))
        )
      }
      case None => Future.value(None)
    }
  }
}
