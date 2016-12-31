package krs.partner.api

import krs.partner.domain.{ DomainModule }

trait ApiModule { this: DomainModule =>
  val partnerApi = PartnerApi(partnerRepository)
}
