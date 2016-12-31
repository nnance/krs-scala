package krs.user.api

import krs.user.domain._
import krs.partner.domain.{ Offer }

case class UserWithOffers(
  user: Option[User],
  offers: List[Offer])

case class UserApi(repository: UserRepository, partnerModule: krs.partner.api.ApiModule) {
  def getUser(id: Int): Option[User] = {
    UserSystem(repository).getUser(id)
  }

  def getUserWithOffers(id: Int): UserWithOffers = {
    val user = UserSystem(repository).getUser(id)
    val offers = partnerModule.partnerApi.getOffers(user.get.creditScore)
    UserWithOffers(user, offers)
  }
}
