package krs.partner.api

import krs.partner.domain.{ DomainModule, PartnerSystem }

trait ApiModule { this: DomainModule =>
  val partnerApi = PartnerApi(partnerRepository)
}
