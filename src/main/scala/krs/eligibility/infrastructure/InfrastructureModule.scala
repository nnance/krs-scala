package krs.eligibility.infrastructure

import krs.eligibility.api._
import krs.partner.infrastructure.{ Injector }

trait InfrastructureModule { this: ApiModule =>

  val partnerRepository = new Injector().partnerApi

}
