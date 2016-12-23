package krs.domain

// our Offer ADT. This is basically a choice type. An offer can be either
// a credit card or a personal loan. the "sealed" keyword basically ensures
// only classes in this file can extend the offer trait. Basically this says
// if we were to add a new offer to the system the ADT (and everywhere that
// uses the ADT) needs to be modified to take into account the new requirement.
sealed trait Offer {
  val provider: String
  val creditScoreRange: Range
}

case class CreditCard(
  provider: String,
  creditScoreRange: Range) extends Offer

case class PersonalLoan(
  provider: String,
  creditScoreRange: Range,
  val maxLoanAmount: Double,
  val term: Long) extends Offer

// Here is our ADT for what an eligibility rule is. Each rule can be one of
// the following choices (max loan amount is x, credit score range is min/max)
sealed trait Rule
case class CreditScoreRange(val range: Range) extends Rule
case class MaxLoanAmount(val amount: Double) extends Rule

trait OffersDomain {
  sealed trait EligibilityRule[T] {
    def isEligible(user: User, rule: T): Boolean
  }

  def filterEligible(user: User, offers: Seq[Offer]): Seq[Offer]
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

  def filterEligible(user: User, offers: Seq[Offer]): Seq[Offer] = {
    offers.filter((offer: Offer) => isEligible(user, offer))
  }
}
