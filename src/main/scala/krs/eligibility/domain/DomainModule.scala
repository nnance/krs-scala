package krs.eligibility.domain

import krs.partner.api.{ PartnerApi }

trait DomainModule {
  def partnerRepository: PartnerApi
}
