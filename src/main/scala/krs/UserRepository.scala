package krs

trait UserRepository {
  def loadUsers(): List[User]
  def getUser(id: Int): Option[User]
}
