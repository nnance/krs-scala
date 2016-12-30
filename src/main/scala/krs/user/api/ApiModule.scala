package krs.user.api

import krs.user.domain.{ DomainModule, UserSystem }

trait ApiModule { this: DomainModule =>
  val userApi = UserApi(repository)
}
