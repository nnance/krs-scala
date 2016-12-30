package krs.partner.api

import krs.partner.domain._

case class PartnerApi(repository: PartnerRepository) {
  def getOffers(creditScore: Int): List[Offer] = {
    val offers = repository.loadOffers
    PartnerSystem().filterOffers(offers, creditScore)
  }
}
