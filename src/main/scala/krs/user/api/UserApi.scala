package krs.user.api

import krs.user.domain._

case class UserApi(repository: UserRepository) {
  def getUser(id: Int): Option[User] = {
    UserSystem(repository).getUser(id)
  }
}
