package krs.domain

import scala.io.Source
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._

case class User(
  id: Int,
  name: String,
  creditScore: Int,
  outstandingLoanAmount: Double)

trait UserRepository {
  def loadUsers(): List[User]
  def getUser(id: Int): Option[User]
}
