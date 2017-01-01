package krs.user.service

import krs.user.api._
import krs.user.infrastructure._

trait ServiceModule { this: ApiModule =>

  val repository = UserRepositoryMemory()
  val partnerApi = PartnerClient()

}
