package krs.infrastructure

import krs.domain._
import krs.api.service.{ UserModule }

trait InfrastructureUserServerModule { this: UserModule with DomainModule =>

  val userRepository = UserRepositoryFS("./fixtures/users.json")
  val partnerRepository = PartnerRepositoryMemory()

}
