package krs.user.service

import com.twitter.util.{ Future }
import krs.common.{ PartnerUtil }
import krs.thriftscala.{ User, UserService }

import krs.user.api.{ UserApi }
import krs.user.domain.{ UserNotFound }

object UserServiceImpl {
  def apply(api: UserApi) = {
    new UserService[Future] {
      def getUser(id: Int) = {
        val user: User = api.getUser(id) match {
          case Some(u) => User(u.id, u.name, u.creditScore, Option(u.outstandingLoanAmount))
          case None => throw UserNotFound(id)
        }
        Future.value(user)
      }

      def getUserWithOffers(id: Int) = {
        api.getUserWithOffers(id).map(userWithOffers => {
          userWithOffers match {
            case Some(u) =>
              User(u.user.id, u.user.name, u.user.creditScore, Option(u.user.outstandingLoanAmount), Option(u.offers.map(PartnerUtil.convertOffer)))
            case None =>
              throw UserNotFound(id)
          }
        })
      }
    }
  }
}
