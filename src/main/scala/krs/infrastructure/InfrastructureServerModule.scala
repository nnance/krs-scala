package krs.infrastructure

import krs.domain._
import krs.api.service.{ ServerModule }

trait InfrastructureServerModule { this: ServerModule with DomainModule =>

  val userRepository = UserRepositoryFS("./fixtures/users.json")
  val partnerRepository = PartnerRepositoryFS("./fixtures/offers.json")

}
