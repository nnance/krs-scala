package krs.user.infrastructure

import krs.common.{FileSystem}
import krs.user.domain._

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._

case class JsonUser(
  id: Int,
  name: String,
  creditScore: Int,
  outstandingLoanAmount: Double
)

case class UserRepositoryFS(val fileName: String) extends FileSystem with UserRepository {

  private def readJsonUser(source: String): List[JsonUser] = {
    decode[List[JsonUser]](source).getOrElse(List())
  }

  def loadUsers(): List[User] = {
    readJsonUser(readFile(fileName)).map(u => {
      User(u.id, u.name, u.creditScore, u.outstandingLoanAmount)
    })
  }
}
