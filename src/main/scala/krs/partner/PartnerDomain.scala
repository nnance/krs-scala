package krs.partner

import com.twitter.util.Future

object PartnerDomain {
  type CreditScore = Int

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

object DecoratorPattern {
  def evenCheck(x: Int): Boolean = (x % 2) == 0

  def logValue[A](x: A): A = {
    System.out.println(x)
    x
  }

  def isEven(x: Int): Boolean = logValue(evenCheck(logValue(x)))

  type CustomerId = Int

  case class Customer(
    id: CustomerId,
    name: String
  )
  type GetCustomer = CustomerId => Option[Customer]

  def getCustomerFromList(customers: List[Customer], id: CustomerId): Option[Customer] =
    customers.find(customer => customer.id == id)

  val customers = List(
    Customer(1, "Test"),
    Customer(2, "Other Test")
  )

  def getCustomer: GetCustomer = getCustomerFromList(customers, _)
}

object PartnerSystem {
  import PartnerDomain._

  def filterOffers(offers: List[Offer], creditScore: CreditScore): List[Offer] =
    offers.filter(o => creditScore >= o.creditScoreRange.min && creditScore <= o.creditScoreRange.max)

  def getOffersFromRepo(repository: PartnerRepository, creditScore: CreditScore): Future[Seq[Offer]] = {
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
  import PartnerDomain._

  def partnerRepository: PartnerRepository
  def getOffers: CreditScore => Future[Seq[Offer]] = PartnerSystem.getOffersFromRepo(partnerRepository, _)
}
