package krs

import krs.api.ServerModule
import krs.domain.DomainModule
import krs.infrastructure.InfrastructureServerModule

trait TestServerModule extends InfrastructureServerModule with ServerModule with DomainModule
