package krs.api

import krs.domain.{ PartnerRepository }

class PartnerApi(partnerRepository: PartnerRepository) {
  def getOffers() = {
    partnerRepository.loadOffers()
  }
}
