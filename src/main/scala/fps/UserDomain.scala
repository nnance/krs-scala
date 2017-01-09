package fps

object UserDomain {
  type UserRepository = List[User]

  case class User(
    id: Int,
    name: String,
    creditScore: Int,
    outstandingLoanAmount: Double
  )
}

trait UserSystem {
  import UserDomain._

  def find: Long => Option[User]
}

object InMemoryUserRepository extends UserSystem {
  import UserDomain._
  val users = User(1, "TestUser01", 500, 100.00) ::
    User(2, "TestUser02", 765, 100.00) ::
    User(3, "TestUser03", 500, 0.00) ::
    User(4, "TestUser04", 765, 0.00) ::
    Nil

  def findFromRepo: UserRepository => Long => Option[User] = repo => id => repo.find(_.id == id)
  def find: Long => Option[User] = findFromRepo(users)
}

object FileUserRepository extends UserSystem {
  import UserDomain._
  var users = List.empty[User]

  def findFromRepo: UserRepository => Long => Option[User] = repo => id => repo.find(_.id == id)
  def find: Long => Option[User] = findFromRepo(users)
}