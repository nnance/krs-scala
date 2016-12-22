package krs.domain

import scala.io.Source
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._

trait UserTrait {
  id: Int
  name: String
  creditScore: Int
  outstandingLoanAmount: Double
}

case class User(
  val id: Int,
  val name: String,
  val creditScore: Int,
  val outstandingLoanAmmount: Double) extends UserTrait

case class UserWithOffers(
  val id: Int,
  val name: String,
  val creditScore: Int,
  val outstandingLoanAmmount: Double,
  offers: List[Offer]) extends UserTrait

trait UserRepository {
  def loadUsers(): List[User]
  def getUser(id: Int): Option[User]
}
