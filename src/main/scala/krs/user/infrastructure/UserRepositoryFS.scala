package krs.user.infrastructure

import com.twitter.util.{ Future }

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._

import krs.common.{ FileSystem }
import krs.user.domain._

case class JsonUser(
  id: Int,
  name: String,
  creditScore: Int,
  outstandingLoanAmount: Double)

case class UserRepositoryFS(val fileName: String) extends FileSystem with UserRepository {

  def readJsonUser(source: String): List[JsonUser] = {
    decode[List[JsonUser]](source).getOrElse(List())
  }

  def loadUsers(): Future[List[User]] = {
    readFile(fileName).map(json => {
      readJsonUser(json).map(u => User(u.id, u.name, u.creditScore, u.outstandingLoanAmount))
    })
  }
}
