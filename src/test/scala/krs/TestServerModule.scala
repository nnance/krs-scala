package krs

import krs.api.service.ServerModule
import krs.domain.DomainModule
import krs.infrastructure.InfrastructureServerModule

trait TestServerModule extends InfrastructureServerModule with ServerModule with DomainModule
