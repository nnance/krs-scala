package krs.user.domain

import com.twitter.util.{ Future }

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

trait UserRepository {
  def loadUsers(): List[User]
}

trait UserDomain {
  val repository: UserRepository

  def getUsers(): List[User]
  def getUser(id: Int): Option[User]
}

case class UserSystem(
    repository: UserRepository) extends UserDomain {

  def getUsers(): List[User] = {
    repository.loadUsers()
  }

  def getUser(id: Int): Option[User] = {
    repository.loadUsers().find((user) => user.id == id)
  }
}
