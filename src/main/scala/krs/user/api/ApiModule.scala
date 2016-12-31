package krs.user.api

import krs.user.domain.{ DomainModule }

trait ApiModule { this: DomainModule =>
  val partnerModule: krs.partner.api.ApiModule
  val userApi = UserApi(repository, partnerModule)
}
