package fps

object UserDomain {
  type UserRepository = List[User]
  type UserFilter = Long => Option[User]

  case class User(
    id: Int,
    name: String,
    creditScore: Int,
    outstandingLoanAmount: Double
  )
}

trait UserSystem {
  import UserDomain._

  def find(id: Long, f: UserFilter): Option[User] = f(id)
}

object UserInMemory extends UserSystem {
  import UserDomain._

  def find: Long => Option[User] = n => InMemoryUserRepository.findFromRepo(n)
}

object InMemoryUserRepository {
  import UserDomain._
  val users = User(1, "TestUser01", 500, 100.00) ::
    User(2, "TestUser02", 765, 100.00) ::
    User(3, "TestUser03", 500, 0.00) ::
    User(4, "TestUser04", 765, 0.00) ::
    Nil

  def findFromRepo: UserFilter = id => users.find(_.id == id)
}

object FileUserRepository {
  import UserDomain._
  var users = List.empty[User]

  def findFromRepo: UserFilter = id => users.find(_.id == id)
}