package krs.eligibility.infrastructure

import krs.eligibility.domain.{ DomainModule }
import krs.eligibility.api.{ ApiModule }

class Injector extends InfrastructureModule with ApiModule with DomainModule
