package krs.eligibility

import krs.user.{User}
import krs.partner.domain.{Offer, CreditCard, PersonalLoan}

// Here is our ADT for what an eligibility rule is. Each rule can be one of
// the following choices (max loan amount is x, credit score range is min/max)
sealed trait Rule
case class CreditScoreRange(val range: Range) extends Rule
case class MaxLoanAmount(val amount: Double) extends Rule

trait EligibilityDomain {
  sealed trait EligibilityRule[T] {
    def isEligible(user: User, rule: T): Boolean
  }
}

object EligibilitySystem extends EligibilityDomain {
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
}
