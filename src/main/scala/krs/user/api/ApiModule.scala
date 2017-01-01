package krs.user.api

import krs.user.domain.{ DomainModule }
import krs.partner.api.{ PartnerApi }
import krs.eligibility.api.{ EligibilityApi }

trait ApiModule { this: DomainModule =>
  val partnerRepository: PartnerApi
  val eligibilityApi: EligibilityApi
  val userApi = UserApi(repository, partnerRepository, eligibilityApi)
}
