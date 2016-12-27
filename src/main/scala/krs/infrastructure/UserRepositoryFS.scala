package krs.infrastructure

import krs.domain.{ User, UserRepository }

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._

case class UserRepositoryFS(val fileName: String) extends FileSystem with UserRepository {

  var userCache: List[User] = List()

  def readUserJson(source: String): List[User] = {
    val res: cats.data.Xor[Error, List[User]] = decode[List[User]](source)
    res.getOrElse(throw new Exception())
  }

  def loadUsers(): List[User] = {
    if (userCache.length == 0) {
      userCache = readUserJson(readFile(fileName))
    }
    userCache
  }
}
