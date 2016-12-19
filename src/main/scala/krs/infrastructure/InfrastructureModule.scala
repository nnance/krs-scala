package krs.infrastructure

import krs.domain._
import krs.api.{ ApiModule }

trait InfrastructureModule { this: ApiModule with DomainModule =>

  override val userRepository: UserRepository = new UserRepositoryFS("./fixtures/users.json")

}
