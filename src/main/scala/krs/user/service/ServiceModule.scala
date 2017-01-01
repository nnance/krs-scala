package krs.user.service

import krs.user.api._
import krs.user.infrastructure._

trait ServiceModule { this: ApiModule =>

  val repository = UserRepositoryFS("./fixtures/users.json")
  val partnerRepository = PartnerClient()
  val eligibilityApi = new krs.eligibility.infrastructure.Injector().eligibilityApi

}
