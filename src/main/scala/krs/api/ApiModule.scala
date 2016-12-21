package krs.api

import krs.domain.{ DomainModule }

trait ApiModule { this: DomainModule =>
  val userApi: UserApi = new UserApi(userRepository)
  val partnerApi: PartnerApi = new PartnerApi(partnerRepository)
}
