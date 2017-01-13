package krs.partner

import com.twitter.util.{Await}
import org.scalatest._

trait TestModule extends InfrastructureModule with ApiModule with DomainModule

class PartnerApiSpec extends FlatSpec with Matchers {
  "filterOffers" should "have 5 items for 550 score" in new TestModule {
    val eligable = Await.result(partnerApi.getOffers(550))
    eligable.length should be(5)
  }
}
