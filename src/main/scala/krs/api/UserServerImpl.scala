package krs.api

import com.twitter.util.{ Future }
import krs.thriftscala.{ User, UserService }

import krs.domain.{ UserRepository }

class UserServerImpl(userRepository: UserRepository) {
  def apply() = {
    new UserService[Future] {
      def getUsers() = {
        val serviceUsers: List[User] = userRepository.loadUsers().map((u) => {
          User(u.id, u.name, u.creditScore, Some(u.outstandingLoanAmount))
        })
        Future.value(serviceUsers)
      }

      def getUser(id: Int) = {
        val user = userRepository.getUser(id) match {
          case Some(u) => User(u.id, u.name, u.creditScore, Some(u.outstandingLoanAmount))
          case None => throw UserNotFound(id)
        }
        Future.value(user)
      }
    }
  }
}
