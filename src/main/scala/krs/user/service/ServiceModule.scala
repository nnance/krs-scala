package krs.user.service

import krs.user.api._
import krs.user.infrastructure._

trait ServiceModule { this: ApiModule =>

  val repository = UserRepositoryMemory()
  val partnerRepository = PartnerClient()
  val eligibilityApi = new krs.eligibility.infrastructure.Injector().eligibilityApi

}
