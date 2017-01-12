package fps

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._

object InMemoryPartnerRepository {
  import PartnerDomain._

  val offers = List(
    CreditCard("Offer01", Range(500, 700)),
    CreditCard("Offer02", Range(550, 700)),
    CreditCard("Offer03", Range(600, 700)),
    CreditCard("Offer04", Range(650, 700)),
    CreditCard("Offer05", Range(700, 770)),
    CreditCard("Offer06", Range(750, 770)),
    PersonalLoan("Offer07", Range(500, 700), 0.00, 12),
    PersonalLoan("Offer08", Range(550, 700), 0.00, 12),
    PersonalLoan("Offer09", Range(500, 700), 100.00, 12),
    PersonalLoan("Offer10", Range(750, 770), 100.00, 12)
  )

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