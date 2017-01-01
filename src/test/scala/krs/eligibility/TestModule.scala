package krs.eligibility

import krs.eligibility.api.ApiModule
import krs.eligibility.domain.DomainModule
import krs.eligibility.infrastructure.InfrastructureModule

trait TestModule extends InfrastructureModule with ApiModule with DomainModule
