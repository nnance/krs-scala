package krs.infrastructure

import krs.domain._

trait InfrastructureModule { this: DomainModule =>

  override val userRepository: UserRepository = new UserRepositoryFS("./fixtures/users.json")

}
