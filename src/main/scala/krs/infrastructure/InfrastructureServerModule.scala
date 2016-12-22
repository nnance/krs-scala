package krs.infrastructure

import krs.domain._
import krs.api.service.{ ServerModule }

trait InfrastructureServerModule { this: ServerModule with DomainModule =>

  val userRepository: UserRepository = new UserRepositoryFS("./fixtures/users.json")
  val partnerRepository: PartnerRepository = new PartnerRepositoryFS("./fixtures/offers.json")

}
