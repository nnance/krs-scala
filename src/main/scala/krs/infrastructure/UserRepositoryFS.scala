package krs.infrastructure

import krs.domain.{ User, UserRepository }

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._

case class JsonUser(
  id: Int,
  name: String,
  creditScore: Int,
  outstandingLoanAmount: Double)

case class UserRepositoryFS(val fileName: String) extends FileSystem with UserRepository {

  var userCache: List[User] = List()

  def readJsonUser(source: String): List[JsonUser] = {
    val res: cats.data.Xor[Error, List[JsonUser]] = decode[List[JsonUser]](source)
    res.getOrElse(throw new Exception())
  }

  def loadUsers(): List[User] = {
    if (userCache.length == 0) {
      userCache = readJsonUser(readFile(fileName)).map(u => {
        User(u.id, u.name, u.creditScore, u.outstandingLoanAmount)
      })
    }
    userCache
  }
}
