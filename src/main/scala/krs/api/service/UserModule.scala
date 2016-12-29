package krs.api.service

import krs.domain.{ DomainModule }
import krs.api.service.client.{ PartnerClient }

trait UserModule { this: DomainModule =>
  val partnerSystem = PartnerClient()
  val userApi = new UserServer(userRepository, partnerSystem)
}
