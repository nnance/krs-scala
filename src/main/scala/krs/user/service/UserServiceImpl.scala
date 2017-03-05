package krs.user.service

import com.twitter.util.Future
import krs.eligibility.EligibilitySystem.filterEligible
import krs.user.UserDomain.{GetUser, UserNotFound, UserWithOffers}
import krs.user.UserSystem
import krs.common.PartnerUtil.convertOffer
import krs.partner.PartnerDomain.GetOffers
import krs.thriftscala.{User, UserService}

/**
  * Created by nicknance on 3/4/17.
  */
trait UserServiceImpl extends UserSystem {

  def getUserFromRepo: GetUser
  def getOffersFromRepo: GetOffers

  def getUserWithOffersFromRepo: Int => Future[Option[UserWithOffers]] =
    super.getUserWithOffers(getUserFromRepo, getOffersFromRepo, filterEligible, _)

  def apply(): UserService[Future] = {

    new UserService[Future] {
      def getUser(id: Int) = {
        val user = getUserFromRepo(id) match {
          case Some(u) => User(u.id, u.name, u.creditScore, Option(u.outstandingLoanAmount))
          case None => throw UserNotFound(id)
        }
        Future.value(user)
      }

      def getUserWithOffers(id: Int) =
        getUserWithOffersFromRepo(id).map(_ match {
          case Some(u) =>
            User(
              u.user.id,
              u.user.name,
              u.user.creditScore,
              Option(u.user.outstandingLoanAmount),
              Option(u.offers.map(convertOffer)))
          case None => throw UserNotFound(id)
        })
    }
  }
}