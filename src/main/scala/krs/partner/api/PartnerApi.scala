package krs.partner.api

import com.twitter.util.{ Future }
import krs.partner.domain._

trait PartnerApi {
  def getOffers(creditScore: Int): Future[Seq[Offer]]
}

case class PartnerApiImpl(repository: PartnerRepository) extends PartnerApi {
  def getOffers(creditScore: Int): Future[Seq[Offer]] = {
    repository.loadOffers.map(offers => {
      PartnerSystem().filterOffers(offers, creditScore)
    })
  }
}
