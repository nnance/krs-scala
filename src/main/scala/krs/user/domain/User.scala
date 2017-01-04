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
  def loadUsers(): Future[List[User]]
}

trait UserDomain {
  val repository: UserRepository

  def getUsers(): Future[List[User]]
  def getUser(id: Int): Future[Option[User]]
}

case class UserSystem(
    repository: UserRepository) extends UserDomain {

  def getUsers(): Future[List[User]] = {
    repository.loadUsers()
  }

  def getUser(id: Int): Future[Option[User]] = {
    repository.loadUsers().map(users => users.find((user) => user.id == id))
  }
}
