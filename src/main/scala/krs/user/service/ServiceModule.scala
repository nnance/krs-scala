package krs.user.service

import krs.user.api._
import krs.user.infrastructure._

trait ServiceModule { this: ApiModule =>
  val conf = com.typesafe.config.ConfigFactory.load()
  val userData = conf.getString("krs.user.data")

  val repository = UserRepositoryFS(userData)
  val partnerRepository = PartnerClient()
  val eligibilityApi = new krs.eligibility.infrastructure.Injector().eligibilityApi
}
