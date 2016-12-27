package krs.domain

trait PartnerRepository {
  def loadOffers(): List[Offer]
}

trait PartnerDomain {
  val repository: PartnerRepository
  def getOffers: List[Offer]
}

case class PartnerSystem(repository: PartnerRepository) extends PartnerDomain {
  def getOffers(): List[Offer] = {
    repository.loadOffers()
  }
}
