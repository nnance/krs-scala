package krs.partner.infrastructure

import krs.partner.domain.{ DomainModule }
import krs.partner.api.{ ApiModule }

class Injector extends InfrastructureModule with ApiModule with DomainModule
