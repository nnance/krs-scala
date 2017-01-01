package krs.eligibility.api

import krs.eligibility.domain.{ DomainModule }

trait ApiModule { this: DomainModule =>
  val eligibilityApi = EligibilityApiImpl()
}
