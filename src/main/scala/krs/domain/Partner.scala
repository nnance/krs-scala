package krs.domain

import com.twitter.util.{ Future }

trait PartnerRepository {
  def loadOffers(): List[Offer]
}

trait PartnerDomain {
  def getOffers: Future[List[Offer]]
}

case class PartnerSystem(repository: PartnerRepository) extends PartnerDomain {
  def getOffers(): Future[List[Offer]] = {
    val offers = repository.loadOffers()
    Future.value(offers)
  }
}
