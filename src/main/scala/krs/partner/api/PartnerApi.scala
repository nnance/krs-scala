package krs.partner.api

import com.twitter.util.{ Future }
import krs.partner.domain._

trait PartnerApiTrait {
  def getOffers(creditScore: Int): Future[Seq[Offer]]
}

case class PartnerApi(repository: PartnerRepository) extends PartnerApiTrait {
  def getOffers(creditScore: Int): Future[Seq[Offer]] = {
    val offers = repository.loadOffers
    val filteredOffers = PartnerSystem().filterOffers(offers, creditScore)
    Future.value(filteredOffers)
  }
}
