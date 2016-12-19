package krs.infrastructure

import krs.domain.{ DomainModule }
import krs.api.{ ApiModule }

class Injector extends DomainModule with ApiModule with InfrastructureModule
