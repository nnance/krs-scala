package krs.service.impl

import com.twitter.util.{ Future }
import krs.thriftscala.{ User, UserService }
import krs.api.ApiModule
import krs.domain.DomainModule
import krs.infrastructure.InfrastructureModule

class UserServerImpl() extends InfrastructureModule with ApiModule with DomainModule {
  def apply() = {
    new UserService[Future] {
      def getUsers() = {
        val serviceUsers: List[User] = userApi.getUsers().map((u) => {
          User(u.id, u.name, u.creditScore, Some(u.outstandingLoanAmount))
        })
        Future.value(serviceUsers)
      }

      def getUser(id: Int) = {
        val u = userApi.getUser(id)
        Future.value(
          User(u.id, u.name, u.creditScore, Some(u.outstandingLoanAmount))
        )
      }
    }
  }
}
