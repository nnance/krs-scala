package krs.user.api

import krs.user.domain.{ DomainModule }

trait ApiModule { this: DomainModule =>
  val partnerApi: krs.partner.api.PartnerApiTrait
  val userApi = UserApi(repository, partnerApi)
}
