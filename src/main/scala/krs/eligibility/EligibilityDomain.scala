package krs.eligibility

import com.twitter.util.Future
import krs.user.UserDomain.User

trait EligibilityDomain {
  // Here is our ADT for what an eligibility rule is. Each rule can be one of
  // the following choices (max loan amount is x, credit score range is min/max)
  sealed trait Rule
  case class CreditScoreRange(val range: Range) extends Rule
  case class MaxLoanAmount(val amount: Double) extends Rule

  sealed trait EligibilityRule[T] {
    def isEligible(user: User, rule: T): Boolean
  }
}

object EligibilitySystem extends EligibilityDomain {
  import krs.partner.PartnerDomain._

  private implicit object CreditScoreRangeRule extends EligibilityRule[CreditScoreRange] {
    def isEligible(user: User, rule: CreditScoreRange): Boolean =
      user.creditScore >= rule.range.min && user.creditScore <= rule.range.max
  }

  private implicit object MaxLoanAmountRule extends EligibilityRule[MaxLoanAmount] {
    def isEligible(user: User, rule: MaxLoanAmount): Boolean =
      user.outstandingLoanAmount < rule.amount
  }

  private def isEligible[T](user: User, t: T)(implicit rule: EligibilityRule[T]) =
    rule.isEligible(user, t)

  def isEligible(user: User, offer: Offer): Boolean =
    offer match {
      case cc: CreditCard =>
        isEligible(user, CreditScoreRange(cc.creditScoreRange))
      case pl: PersonalLoan =>
        isEligible(user, CreditScoreRange(pl.creditScoreRange)) &&
          isEligible(user, MaxLoanAmount(pl.maxLoanAmount))
    }

  type EligibilityFilter = (User, Seq[Offer]) => Future[Seq[Offer]]

  def filterEligible: EligibilityFilter = (user, offers) =>
    Future.value(offers.filter(offer => EligibilitySystem.isEligible(user, offer)))
}
