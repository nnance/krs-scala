package krs

// our Offer ADT. This is basically a choice type. An offer can be either
// a credit card or a personal loan. the "sealed" keyword basically ensures
// only classes in this file can extend the offer trait. Basically this says
// if we were to add a new offer to the system the ADT (and everywhere that
// uses the ADT) needs to be modified to take into account the new requirement.
sealed trait Offer
case class CreditCard(
  val provider: String,
  val creditScoreRange: Range
) extends Offer

case class PersonalLoan(
  val provider: String,
  val creditScoreRange: Range,
  val maxLoanAmount: Double,
  val term: Long
) extends Offer

// Here is our ADT for what an eligibility rule is. Each rule can be one of
// the following choices (max loan amount is x, credit score range is min/max)
sealed trait EligibilityRule
case class CreditScoreRange(val range: Range) extends EligibilityRule
case class MaxLoanAmount(val amount: Double) extends EligibilityRule

trait OffersDomain {
  def isEligible(user: User, rule: EligibilityRule): Boolean
  def eligibilityRules(offer: Offer): Seq[EligibilityRule]
  def filterEligible(user: User, rules: Seq[EligibilityRule]): Seq[EligibilityRule]
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

  // def filterEligible(user: User, offers: Seq[Offer]): Seq[EligibilityRule] = {
    // val rules = offers.map((offer: Offer) => eligibilityRules(offer))
    // val userFilter = (ruleList: Seq[EligibilityRule]) => ruleList.reduceLeft((x,y) => isEligible(user, x) && isEligible(user, y)))
    // rules.filter(userFilter)
  // }
}
