package krs.domain

trait PartnerRepository {
  def loadOffers(): List[Offer]
}
