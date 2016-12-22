package krs.service

import krs.infrastructure.InfrastructureServerModule
import krs.api.ServerModule
import krs.domain.DomainModule

trait ServiceModule extends InfrastructureServerModule with ServerModule with DomainModule
