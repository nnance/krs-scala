package krs.api

import krs.domain.{ DomainModule, UserSystem, PartnerSystem }

trait ApiModule { this: DomainModule =>
  val partnerSystem = PartnerSystem(partnerRepository)
  val userApi = UserSystem(userRepository, partnerSystem)
}
