package krs.partner.service

import krs.partner.api._
import krs.partner.infrastructure._

trait ServiceModule { this: ApiModule =>
  val conf = com.typesafe.config.ConfigFactory.load();
  val partnerData = conf.getString("krs.partner.data")

  val partnerRepository = PartnerRepositoryFS(partnerData)
}
