package krs.user.infrastructure

import krs.user.domain._

trait InfrastructureModule { this: DomainModule =>

  override val repository = UserRepositoryMemory()

}
