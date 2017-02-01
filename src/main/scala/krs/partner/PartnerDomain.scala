package krs.partner

import com.twitter.util.Future

object PartnerDomain {
  sealed trait Offer {
    val provider: String
    val creditScoreRange: Range
  }

  case class CreditCard(
    provider: String,
    creditScoreRange: Range
  ) extends Offer

  case class PersonalLoan(
    provider: String,
    creditScoreRange: Range,
    maxLoanAmount: Double,
    term: Long
  ) extends Offer
}

trait PartnerApi {
  import PartnerDomain._

  def getOffers(creditScore: Int): Future[Seq[Offer]]
}

case class PartnerSystem(repository: PartnerRepository) extends PartnerApi {
  import PartnerDomain._

  def filterOffers(offers: List[Offer], creditScore: Int): List[Offer] =
    offers.filter(o => creditScore >= o.creditScoreRange.min && creditScore <= o.creditScoreRange.max)

  def getOffers(creditScore: Int): Future[Seq[Offer]] = {
    val offers = repository.loadOffers
    val filteredOffers = filterOffers(offers, creditScore)
    Future.value(filteredOffers)
  }
}

trait PartnerRepository {
  import PartnerDomain._

  def loadOffers(): List[Offer]
}

trait DomainModule {
  def partnerRepository: PartnerRepository
  val partnerApi = PartnerSystem(partnerRepository)
}
