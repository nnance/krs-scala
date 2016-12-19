package krs

import krs.api.ApiModule
import krs.domain.DomainModule
import krs.infrastructure.InfrastructureModule

trait TestModule extends InfrastructureModule with ApiModule with DomainModule
