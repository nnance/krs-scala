package krs.user.infrastructure

import krs.user.api._

object PartnerModule
  extends krs.partner.infrastructure.InfrastructureModule
  with krs.partner.api.ApiModule
  with krs.partner.domain.DomainModule

trait InfrastructureModule { this: ApiModule =>

  val repository = UserRepositoryMemory()
  val partnerModule = PartnerModule
  val partnerApi = partnerModule.partnerApi

}
