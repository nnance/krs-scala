package krs.service

import krs.infrastructure.InfrastructureModule
import krs.api.ServerModule
import krs.domain.DomainModule

trait ServiceModule extends InfrastructureModule with ServerModule with DomainModule
