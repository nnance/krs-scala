package krs.partner

object PartnerModule
  extends krs.partner.infrastructure.InfrastructureModule
  with krs.partner.api.ApiModule
  with krs.partner.domain.DomainModule
