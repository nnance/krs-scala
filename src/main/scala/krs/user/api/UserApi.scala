package krs.user.api

import krs.user.domain._
import krs.partner.domain.{ Offer }

case class UserWithOffers(
  user: User,
  offers: List[Offer])

case class UserApi(repository: UserRepository, partnerApi: krs.partner.api.PartnerApi) {
  def getUser(id: Int): Option[User] = {
    UserSystem(repository).getUser(id)
  }

  def getUserWithOffers(id: Int): Option[UserWithOffers] = {
    UserSystem(repository).getUser(id) match {
      case Some(u) => {
        val offers = partnerApi.getOffers(u.creditScore)
        Option(UserWithOffers(u, offers))
      }
      case None => None
    }
  }
}
