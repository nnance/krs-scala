package krs.service.impl

import com.twitter.util.{ Await }
import org.scalatest.{ FlatSpec, Matchers }

class PartnerServerSpec extends FlatSpec with Matchers {
  "loadOffers" should "have 4 items" in {
    val server = new PartnerServerImpl()
    val resp = Await.result(server().getOffers())
    resp.offers.length should be(7)
    resp.offers(0).provider should be("CapitalOne")
  }
}
