package krs.eligibility

import com.twitter.util.{Future}
import krs.user.{User}
import krs.partner.domain.{Offer}

trait EligibilityApi {
  def filterEligible(user: User, offers: Seq[Offer]): Future[Seq[Offer]]
}

object EligibilityApi extends EligibilityApi {
  def filterEligible(user: User, offers: Seq[Offer]): Future[Seq[Offer]] =
    Future.value(offers.filter(offer => EligibilitySystem.isEligible(user, offer)))
}
