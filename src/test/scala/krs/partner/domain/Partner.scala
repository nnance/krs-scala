package krs.partner.domain

import org.scalatest._
import krs.TestModule

import krs.partner.infrastructure.{ PartnerRepositoryMemory }

class PartnerSystemSpec extends FlatSpec with Matchers {
  "filterOffers" should "have 5 items for 550 score" in {
    val offers = PartnerRepositoryMemory().loadOffers
    val eligable = PartnerSystem().filterOffers(offers, 550)
    eligable.length should be(5)
  }
}
