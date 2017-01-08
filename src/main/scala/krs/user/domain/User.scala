package krs.user.domain

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
  outstandingLoanAmount: Double
) extends UserTrait

trait UserRepository {
  def loadUsers: List[User]
}

trait UserDomain {
  val repository: UserRepository

  def find: Long => Option[User]
}

case class UserSystem(repository: UserRepository) extends UserDomain {

  private def findFromRepo: UserRepository => Long => Option[User] = repo => id => repo.loadUsers.find(_.id == id)
  def find: Long => Option[User] = findFromRepo(repository)

}
