package krs.user.infrastructure

import krs.user.api._
import krs.partner.{ PartnerModule }

trait InfrastructureModule { this: ApiModule =>

  val repository = UserRepositoryMemory()
  val partnerModule = PartnerModule

}
