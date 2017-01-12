package fps

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._

object InMemoryUserRepository {
  import UserDomain._
  val users = User(1, "TestUser01", 500, 100.00) ::
    User(2, "TestUser02", 765, 100.00) ::
    User(3, "TestUser03", 500, 0.00) ::
    User(4, "TestUser04", 765, 0.00) ::
    Nil

  def findFromRepo: UserFilter = id => users.find(_.id == id)
}

case class JsonUser(
  id: Int,
  name: String,
  creditScore: Int,
  outstandingLoanAmount: Double
)

case class FileUserRepository(fileName: String) extends FileSystem {
  import UserDomain._

  private def readJsonUser(source: String): List[JsonUser] = {
    decode[List[JsonUser]](source).getOrElse(List())
  }

  def loadUsers(): UserRepository = {
    readJsonUser(readFile(fileName)).map(u => {
      User(u.id, u.name, u.creditScore, u.outstandingLoanAmount)
    })
  }

  def findFromRepo: UserFilter = id => loadUsers.find(_.id == id)
}