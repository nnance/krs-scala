package krs.api.service

import krs.domain.{ DomainModule }

trait ServerModule { this: DomainModule =>
  val userApi = new UserServer(userRepository)
  val partnerApi = new PartnerServer(partnerRepository)
}
