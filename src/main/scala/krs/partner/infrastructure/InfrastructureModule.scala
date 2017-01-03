package krs.partner.infrastructure

import krs.partner.domain._

trait InfrastructureModule { this: DomainModule =>

  val partnerRepository = PartnerRepositoryMemory()

}
