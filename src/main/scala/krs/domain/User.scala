package krs.domain

sealed trait UserTrait {
  val id: Int
  val name: String
  val creditScore: Int
  val outstandingLoanAmount: Double
  val offers: Option[List[Offer]]
}

case class UserNotFound(id: Int) extends Exception {
  override def getMessage: String = s"User(${id.toString}) not found."
}

case class User(
  id: Int,
  name: String,
  creditScore: Int,
  outstandingLoanAmount: Double,
  offers: Option[List[Offer]] = None) extends UserTrait

trait UserRepository {
  def loadUsers(): List[User]
  def getUser(id: Int): Option[User] = {
    loadUsers().find((user) => user.id == id)
  }
}
