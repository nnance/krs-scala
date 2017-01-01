package krs.user.infrastructure

import krs.user.api._
import krs.partner.infrastructure.{ Injector }

trait InfrastructureModule { this: ApiModule =>

  val repository = UserRepositoryMemory()
  val partnerRepository = new Injector().partnerApi
  val eligibilityApi = new krs.eligibility.infrastructure.Injector().eligibilityApi
}
