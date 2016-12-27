package krs.domain

sealed trait UserTrait {
  val id: Int
  val name: String
  val creditScore: Int
  val outstandingLoanAmount: Double
}

case class UserNotFound(id: Int) extends Exception {
  override def getMessage: String = s"User(${id.toString}) not found."
}

case class User(
  id: Int,
  name: String,
  creditScore: Int,
  outstandingLoanAmount: Double) extends UserTrait

case class UserWithOffers(
  id: Int,
  name: String,
  creditScore: Int,
  outstandingLoanAmount: Double,
  offers: List[Offer]) extends UserTrait

trait UserRepository {
  def loadUsers(): List[User]
  def getUser(id: Int): Option[User] = {
    loadUsers().find((user) => user.id == id)
  }
}

trait UserDomain {
  val repository: UserRepository
  val partnerSystem: PartnerDomain

  def getUsers(): List[User]
  def getUser(id: Int): Option[User]
  def getUserWithOffers(id: Int): Option[UserWithOffers]
}

case class UserSystem(
    repository: UserRepository,
    partnerSystem: PartnerDomain) extends UserDomain {

  def getUsers(): List[User] = {
    repository.loadUsers()
  }

  def getUser(id: Int): Option[User] = {
    repository.getUser(id)
  }

  def getUserWithOffers(id: Int): Option[UserWithOffers] = {
    val user: User = repository.getUser(id) match {
      case Some(u) => u
      case None => throw UserNotFound(id)
    }

    val allOffers = partnerSystem.getOffers
    val filteredOffers = OfferSystem.filterEligible(user, allOffers)
    Some(UserWithOffers(user.id, user.name, user.creditScore, user.outstandingLoanAmount, filteredOffers))
  }
}
