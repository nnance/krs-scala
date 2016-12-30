package krs.partner.infrastructure

import krs.partner.domain._

trait InfrastructureModule { this: DomainModule =>

  override val partnerRepository = PartnerRepositoryMemory()

}
