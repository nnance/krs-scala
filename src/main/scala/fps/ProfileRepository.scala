package fps

/**
  * Created by nicknance on 1/18/17.
  */

object ProfileDomain {
  case class Profile(id: Long, name: String)
}

trait ProfileSystem {
  import ProfileDomain._

  def getProfile: Long => Option[Profile]
  def getProfileForRepo: ProfileRepository => Long => Option[Profile]
}

case class ProfileApplication(repo: ProfileRepository) extends ProfileSystem {
  def getProfile = getProfileForRepo(repo)
}

trait ProfileRepository {
  import ProfileDomain._

  def find: Long => Option[Profile]
}

trait InMemoryProfileRepository extends ProfileRepository {
  import ProfileDomain._

  def findFromRepo: List[Profile] => Long => Option[Profile] = repo => id => repo.find(_.id == id)
}