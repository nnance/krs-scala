package krs.service

import krs.infrastructure.InfrastructureUserServerModule
import krs.api.service.UserModule
import krs.domain.DomainModule

trait UserServiceModule extends InfrastructureUserServerModule with UserModule with DomainModule
