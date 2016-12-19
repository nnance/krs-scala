package krs.api

import krs.domain.{ User, UserRepository }

case class UserNotFound(id: Int) extends Exception {
  override def getMessage: String = s"User(${id.toString}) not found."
}

class UserApi(userRepository: UserRepository) {
  def getUsers() = {
    userRepository.loadUsers()
  }

  def getUser(id: Int) = {
    val users = userRepository.loadUsers()
    val user: User = users.find((user) => user.id == id) match {
      case Some(u) => u
      case None => throw UserNotFound(id)
    }
    user
  }
}
