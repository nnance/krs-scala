package krs.api.service

import com.twitter.util.{ Await }
import org.scalatest.{ FlatSpec, Matchers }

import krs.TestServerModule

class PartnerServerSpec extends FlatSpec with Matchers {
  "loadOffers" should "have 4 items" in new TestServerModule {
    val resp = Await.result(partnerApi().getOffers())
    resp.offers.length should be(7)
    resp.offers(0).provider should be("CapitalOne")
  }
}
