package krs.eligibility.domain

import krs.user.domain.{ User }
import krs.partner.domain.{ Offer, CreditCard, PersonalLoan }

// Here is our ADT for what an eligibility rule is. Each rule can be one of
// the following choices (max loan amount is x, credit score range is min/max)
sealed trait Rule
case class CreditScoreRange(val range: Range) extends Rule
case class MaxLoanAmount(val amount: Double) extends Rule

trait OffersDomain {
  sealed trait EligibilityRule[T] {
    def isEligible(user: User, rule: T): Boolean
  }
}

object OfferSystem extends OffersDomain {
  implicit object CreditScoreRangeRule extends EligibilityRule[CreditScoreRange] {
    def isEligible(user: User, rule: CreditScoreRange): Boolean =
      user.creditScore >= rule.range.min && user.creditScore <= rule.range.max
  }

  implicit object MaxLoanAmountRule extends EligibilityRule[MaxLoanAmount] {
    def isEligible(user: User, rule: MaxLoanAmount): Boolean =
      user.outstandingLoanAmount < rule.amount
  }

  def isEligible[T](user: User, t: T)(implicit rule: EligibilityRule[T]) =
    rule.isEligible(user, t)

  def isEligible(user: User, offer: Offer): Boolean = {
    offer match {
      case cc: CreditCard => {
        isEligible(user, CreditScoreRange(cc.creditScoreRange))
      }
      case pl: PersonalLoan => {
        isEligible(user, CreditScoreRange(pl.creditScoreRange)) &&
          isEligible(user, MaxLoanAmount(pl.maxLoanAmount))
      }
    }
  }
}
