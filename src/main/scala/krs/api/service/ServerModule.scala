package krs.api.service

import krs.domain.{ DomainModule, PartnerSystem }

trait ServerModule { this: DomainModule =>
  val partnerSystem = PartnerSystem(partnerRepository)
  val userApi = new UserServer(userRepository)
  val partnerApi = new PartnerServer(partnerRepository)
}
