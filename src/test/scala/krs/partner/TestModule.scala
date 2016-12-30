package krs.partner

import krs.partner.api.ApiModule
import krs.partner.domain.DomainModule
import krs.partner.infrastructure.InfrastructureModule

trait TestModule extends InfrastructureModule with ApiModule with DomainModule
