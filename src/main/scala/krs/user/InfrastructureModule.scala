package krs.user

import com.twitter.util.Future
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

  def getUser: GetUser = id =>
    loadUsers().find(_.id == id)
}

case class UserFileRepository(val fileName: String) extends FileSystem with UserRepository {
  import UserDomain._

  case class JsonUser(id: Int,
                      name: String,
                      creditScore: Int,
                      outstandingLoanAmount: Double)

  private def readJsonUser(source: String): List[JsonUser] =
    decode[List[JsonUser]](source).getOrElse(List())

  def loadUsers(): List[User] = {
    readJsonUser(readFile(fileName)).map(u => {
      User(u.id, u.name, u.creditScore, u.outstandingLoanAmount)
    })
  }

  def getUser: GetUser = id =>
    loadUsers().find(_.id == id)

}

trait ServiceInfrastructure extends UserSystem {
  import UserDomain._
  import krs.eligibility.EligibilitySystem.filterEligible
  import krs.user.service.PartnerFinagleClient

  private val conf = com.typesafe.config.ConfigFactory.load()
  private val userData = conf.getString("krs.user.data")
  private val partnerHost = conf.getString("krs.partner.host")

  val repo = UserFileRepository(userData)
  val partnerService = PartnerFinagleClient(partnerHost)


  case class UserNotFound(id: Int) extends Exception {
    override def getMessage: String = s"User(${id.toString}) not found."
  }

  def getUserFromRepo: GetUser = repo.getUser
  def getUserWithOffersFromRepo: Int => Future[Option[UserWithOffers]] =
    super.getUserWithOffers(repo.getUser, partnerService.getOffers, filterEligible, _)
}
