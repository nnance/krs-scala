package krs.eligibility.api

import com.twitter.util.{ Future }
import krs.user.domain.{ User }
import krs.partner.domain.{ Offer }

import krs.eligibility.domain._

trait EligibilityApi {
  def filterEligible(user: User, offers: Seq[Offer]): Future[Seq[Offer]]
}

case class EligibilityApiImpl() extends EligibilityApi {
  def filterEligible(user: User, offers: Seq[Offer]): Future[Seq[Offer]] =
    Future.value(offers.filter(offer => OfferSystem.isEligible(user, offer)))
}
