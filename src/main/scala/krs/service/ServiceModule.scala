package krs.service

import krs.infrastructure.InfrastructureServerModule
import krs.api.service.ServerModule
import krs.domain.DomainModule

trait ServiceModule extends InfrastructureServerModule with ServerModule with DomainModule
