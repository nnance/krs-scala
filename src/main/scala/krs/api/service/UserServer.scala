package krs.api.service

import com.twitter.util.{ Future }
import krs.thriftscala.{ User, UserService }

import krs.domain.{ UserRepository, UserSystem, UserNotFound, PartnerDomain }

class UserServer(userRepository: UserRepository, partnerSystem: PartnerDomain) {
  def apply() = {
    val users = UserSystem(userRepository, partnerSystem)
    new UserService[Future] {
      def getUsers() = {
        val serviceUsers: List[User] = users.getUsers().map((u) => {
          User(u.id, u.name, u.creditScore, Option(u.outstandingLoanAmount))
        })
        Future.value(serviceUsers)
      }

      def getUser(id: Int) = {
        val user: User = users.getUser(id) match {
          case Some(u) => User(u.id, u.name, u.creditScore, Option(u.outstandingLoanAmount))
          case None => throw UserNotFound(id)
        }
        Future.value(user)
      }

      def getUserWithOffers(id: Int) = {
        users.getUserWithOffers(id).map(u => {
          val user = u match {
            case Some(u) =>
              User(u.id, u.name, u.creditScore, Some(u.outstandingLoanAmount), u.offers.map(o => o.map(PartnerUtil.convertOffer)))
            case None =>
              throw UserNotFound(id)
          }
          user
        })
      }
    }
  }
}
