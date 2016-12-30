package krs.user.infrastructure

import krs.infrastructure.{ FileSystem }
import krs.user.domain._

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._

case class JsonUser(
  id: Int,
  name: String,
  creditScore: Int,
  outstandingLoanAmount: Double)

case class UserRepositoryFS(val fileName: String) extends FileSystem with UserRepository {

  def readJsonUser(source: String): List[JsonUser] = {
    val res: cats.data.Xor[Error, List[JsonUser]] = decode[List[JsonUser]](source)
    res.getOrElse(throw new Exception())
  }

  def loadUsers(): List[User] = {
    readJsonUser(readFile(fileName)).map(u => {
      User(u.id, u.name, u.creditScore, u.outstandingLoanAmount)
    })
  }
}
