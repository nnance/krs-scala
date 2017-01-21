package krs.user

object UserDomain {
  case class User(
    id: Int,
    name: String,
    creditScore: Int,
    outstandingLoanAmount: Double)

  case class UserNotFound(id: Int) extends Exception {
    override def getMessage: String = s"User(${id.toString}) not found."
  }
}

trait UserRepository {
  import UserDomain._

  def loadUsers(): List[User]
}

case class UserSystem(repository: UserRepository) {
  import UserDomain._

  def getUsers(): List[User] = {
    repository.loadUsers()
  }

  def getUser(id: Int): Option[User] = {
    repository.loadUsers().find(_.id == id)
  }
}

trait DomainModule {
  def repository: UserRepository
}
