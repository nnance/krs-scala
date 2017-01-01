package krs.user

import krs.user.api.ApiModule
import krs.user.domain.DomainModule
import krs.user.infrastructure.InfrastructureModule

trait TestModule extends InfrastructureModule with ApiModule with DomainModule
