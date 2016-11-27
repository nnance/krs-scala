package krs

// our Offer ADT. This is basically a choice type. An offer can be either
// a credit card or a personal loan. the "sealed" keyword basically ensures
// only classes in this file can extend the offer trait. Basically this says
// if we were to add a new offer to the system the ADT (and everywhere that
// uses the ADT) needs to be modified to take into account the new requirement.
sealed trait Offer {
  val provider: String
}
case class CreditCard(
  providerName: String,
  val creditScoreRange: Range
) extends Offer {
  val provider = providerName
}

case class PersonalLoan(
  providerName: String,
  val creditScoreRange: Range,
  val maxLoanAmount: Double,
  val term: Long
) extends Offer {
  val provider = providerName
}

// Here is our ADT for what an eligibility rule is. Each rule can be one of
// the following choices (max loan amount is x, credit score range is min/max)
sealed trait EligibilityRule
case class CreditScoreRange(val range: Range) extends EligibilityRule
case class MaxLoanAmount(val amount: Double) extends EligibilityRule

trait OffersDomain {
  def isEligible(user: User, offer: Offer): Boolean
  def filterEligible(user: User, offers: Seq[Offer]): Seq[Offer]
}

object OfferSystem extends OffersDomain {
  def isEligible(user: User, rule: EligibilityRule): Boolean = {
    rule match {
      case cs: CreditScoreRange =>
        user.creditScore >= cs.range.min && user.creditScore <= cs.range.max
      case ml: MaxLoanAmount =>
        user.outstandingLoanAmount < ml.amount
    }
  }

  def eligibilityRules(offer: Offer): Seq[EligibilityRule] = {
    offer match {
      case cc: CreditCard => Seq(
        CreditScoreRange(cc.creditScoreRange)
      )
      case pl: PersonalLoan => Seq(
        CreditScoreRange(pl.creditScoreRange),
        MaxLoanAmount(pl.maxLoanAmount)
      )
    }
  }

  def isEligible(user: User, offer: Offer): Boolean = {
    val checkEligibility = (rule: EligibilityRule) => isEligible(user, rule)
    eligibilityRules(offer).map(checkEligibility).fold(true)((x, y) => x && y)
  }

  def filterEligible(user: User, offers: Seq[Offer]): Seq[Offer] = {
    offers.filter((offer: Offer) => isEligible(user, offer))
  }
}
