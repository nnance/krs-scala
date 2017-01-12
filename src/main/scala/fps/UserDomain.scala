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

  def find: UserFilter
}

object UserInMemory extends UserSystem {
  import UserDomain._

  def find: UserFilter = n => InMemoryUserRepository.findFromRepo(n)
}
