package krs

import scala.io.Source
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._

case class User(
  id: Int,
  name: String,
  creditScore: Int,
  outstandingLoanAmount: Double)

  case class UserNotFound(id: Int) extends Exception {
    override def getMessage: String = s"User(${id.toString}) not found."
  }

  object UserSystem {

  def readFile(fileName: String): String = {
    val bufferedSource = Source.fromFile(fileName)
    val fileCache = bufferedSource.getLines.fold("")((x, y) => x + y)
    bufferedSource.close()
    fileCache
  }

  def loadUsers(source: String): List[User] = {
    val res: cats.data.Xor[Error, List[User]] = decode[List[User]](source)
    res.getOrElse(throw new Exception())
  }

}
