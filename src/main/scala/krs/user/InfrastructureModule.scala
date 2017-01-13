package krs.user

import krs.common.{FileSystem}
import krs.partner.infrastructure.{Injector}

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._

// scalastyle:off magic.number
case class UserRepositoryMemory() extends UserRepository {
  def loadUsers(): List[User] = {
    List(
      new User(1, "TestUser01", 500, 100.00),
      new User(2, "TestUser02", 765, 100.00),
      new User(3, "TestUser03", 500, 0.00),
      new User(4, "TestUser04", 765, 0.00)
    )
  }
}

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

trait InfrastructureModule { this: ApiModule =>

  val repository = UserRepositoryMemory()
  val partnerRepository = new Injector().partnerApi
  val eligibilityApi = new krs.eligibility.infrastructure.Injector().eligibilityApi
}