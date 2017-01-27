package krs.user

import io.circe.generic.auto._
import io.circe.parser._
import krs.common.FileSystem

// scalastyle:off magic.number
case class UserMemoryRepository() extends UserRepository {
  import UserDomain._

  def loadUsers(): List[User] = {
    List(
      new User(1, "TestUser01", 500, 100.00),
      new User(2, "TestUser02", 765, 100.00),
      new User(3, "TestUser03", 500, 0.00),
      new User(4, "TestUser04", 765, 0.00)
    )
  }
}

object UserFileRepository {
  case class JsonUser(
                       id: Int,
                       name: String,
                       creditScore: Int,
                       outstandingLoanAmount: Double
                     )

  case class Repository(val fileName: String) extends FileSystem with UserRepository {
    import UserDomain._

    private def readJsonUser(source: String): List[JsonUser] = {
      decode[List[JsonUser]](source).getOrElse(List())
    }

    def loadUsers(): List[User] = {
      readJsonUser(readFile(fileName)).map(u => {
        User(u.id, u.name, u.creditScore, u.outstandingLoanAmount)
      })
    }
  }


}

trait InfrastructureModule { this: DomainModule =>
  val repository = UserMemoryRepository()
  val getOffers = krs.partner.PartnerMemoryRepository().getOffers
  val filter = krs.eligibility.EligibilitySystem.filterEligible
}
