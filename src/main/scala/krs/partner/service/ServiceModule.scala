package krs.partner.service

import krs.partner.api._
import krs.partner.infrastructure._

trait ServiceModule { this: ApiModule =>

  val partnerRepository = PartnerRepositoryFS("./fixtures/offers.json")

}
