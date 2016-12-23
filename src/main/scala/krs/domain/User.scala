package krs.domain

import scala.io.Source
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._

sealed trait UserTrait {
  val id: Int
  val name: String
  val creditScore: Int
  val outstandingLoanAmount: Double
}

case class User(
  id: Int,
  name: String,
  creditScore: Int,
  outstandingLoanAmount: Double) extends UserTrait

case class UserNotFound(id: Int) extends Exception {
  override def getMessage: String = s"User(${id.toString}) not found."
}

case class UserWithOffers(
  id: Int,
  name: String,
  creditScore: Int,
  outstandingLoanAmount: Double,
  offers: List[Offer]) extends UserTrait

trait UserRepository {
  def loadUsers(): List[User]
  def getUser(id: Int): Option[User]
}

trait UserDomain {
  def getUser(id: Int): Option[User]
  def getUserWithOffers(id: Int): Option[UserWithOffers]
}

case class UserService(
    userRepository: UserRepository,
    offerSystem: OffersDomain) extends UserDomain {

  def getUser(id: Int): Option[User] = {
    userRepository.getUser(id)
  }

  def getUserWithOffers(id: Int): Option[UserWithOffers] = {
    val user: User = userRepository.getUser(id) match {
      case Some(u) => u
      case None => throw UserNotFound(id)
    }
    Some(UserWithOffers(user.id, user.name, user.creditScore, user.outstandingLoanAmount, List()))
  }
}
