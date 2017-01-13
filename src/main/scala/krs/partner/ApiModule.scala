package krs.partner

import com.twitter.util.{Future}

trait PartnerApi {
  def getOffers(creditScore: Int): Future[Seq[Offer]]
}

case class PartnerApiImpl(repository: PartnerRepository) extends PartnerApi {
  def getOffers(creditScore: Int): Future[Seq[Offer]] = {
    val offers = repository.loadOffers
    val filteredOffers = PartnerSystem().filterOffers(offers, creditScore)
    Future.value(filteredOffers)
  }
}

trait ApiModule { this: DomainModule =>
  val partnerApi = PartnerApiImpl(partnerRepository)
}
