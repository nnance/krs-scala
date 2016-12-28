package krs.api.service

import com.twitter.util.{ Future }
import krs.thriftscala.{ User, UserService }

import krs.domain.{ UserRepository, UserSystem, UserNotFound, PartnerSystem }

class UserServer(userRepository: UserRepository, partnerSystem: PartnerSystem) {
  def apply() = {
    val users = UserSystem(userRepository, partnerSystem)
    new UserService[Future] {
      def getUsers() = {
        val serviceUsers: List[User] = users.getUsers().map((u) => {
          User(u.id, u.name, u.creditScore, Some(u.outstandingLoanAmount))
        })
        Future.value(serviceUsers)
      }

      def getUser(id: Int) = {
        val user: User = users.getUser(id) match {
          case Some(u) => User(u.id, u.name, u.creditScore, Some(u.outstandingLoanAmount))
          case None => throw UserNotFound(id)
        }
        Future.value(user)
      }

      def getUserWithOffers(id: Int) = {
        val user: User = users.getUserWithOffers(id) match {
          case Some(u) =>
            User(u.id, u.name, u.creditScore, Some(u.outstandingLoanAmount), Option(u.offers.map(PartnerUtil.convertOffer)))
          case None =>
            throw UserNotFound(id)
        }
        Future.value(user)
      }
    }
  }
}
