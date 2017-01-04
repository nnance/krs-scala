package krs.user.api

import com.twitter.util.{ Future }

import krs.user.domain._
import krs.partner.domain.{ Offer }
import krs.partner.api.{ PartnerApi }
import krs.eligibility.api.{ EligibilityApi }

case class UserWithOffers(
  user: User,
  offers: Seq[Offer])

case class UserApi(
    repository: UserRepository,
    partnerRepository: PartnerApi,
    eligibilitySystem: EligibilityApi) {

  def getUser(id: Int): Future[Option[User]] = {
    UserSystem(repository).getUser(id)
  }

  def getUserWithOffers(id: Int): Future[Option[UserWithOffers]] = {
    UserSystem(repository).getUser(id).map(user => {
      user match {
        case Some(u) => {
          partnerRepository.getOffers(u.creditScore).map(offers => {
            eligibilitySystem.filterEligible(u, offers).map(eligible => {
              Option(UserWithOffers(u, eligible))
            })
          })
        }
        case None => Future.value(None)
      }
    })
  }
}
