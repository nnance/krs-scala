package krs.api

import krs.domain.{ DomainModule }

trait ServerModule extends ApiModule { this: DomainModule =>
  override val userApi: UserServerImpl = new UserServerImpl(userRepository)
  override val partnerApi: PartnerServerImpl = new PartnerServerImpl(partnerRepository)
}
