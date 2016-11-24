package krs

object OfferTypes {
  // our Offer ADT. This is basically a choise type. An offer can be either
  // a credit card or a personal loan. the "sealed" keyword basically ensures
  // only classes in this file can extend the offer trait. Basically this says
  // if we were to add a new offer to the system the ADT (and everywhere that
  // uses the ADT) needs to be modified to take into account the new requirement.
  sealed trait Offer {
    val provider: String
    val creditScoreRange: Range
    val term: Long = 0
  }
  case class CreditCard(name: String, scoreRange: Range) extends Offer {
    val provider = name
    val creditScoreRange = scoreRange
  }
  case class PersonalLoan(name: String, scoreRange: Range, loanTerm: Long) extends Offer {
    val provider = name
    val creditScoreRange = scoreRange
    override val term = loanTerm
  }

  // Here is our ADT for what an eligibility rule is. Each rule can be one of
  // the following choices (max loan amount is x, credit score range is min/max)
  sealed trait EligibilityRule
  case class CreditScoreRange(range: Range) extends EligibilityRule
  case class MaxLoanAmount(amount: Long) extends EligibilityRule
}

trait OffersDomain {
  // This is where the domain becomes tricky. Does this function take an offer
  // and a user? does it take a user and a list of eligibility rules? What
  // makes most sense?
  // def isEligible(offer: Offer, user: User): Boolean
  // def isEligible(user: User, rules: Seq[EligibilityRule]): Boolean
  //
  // Once you made that decision, how does the caller handle grabbing the rules?
  // Since we're only modling the domain here (ddd) we're not really putting it
  // all together.. that will be the tricky part. Where do the rules get stored?
  // My guess is some sort of database (or in-memory map in our case) of
  // Map[Offer, Seq[EligibilityRule])
  //
  // Anyway, since we're just modeling the domain here... might as well define
  // the function
  def isEligible(user: User, rules: Seq[OfferTypes.EligibilityRule]): Boolean
  def eligibilityRules(offer: OfferTypes.Offer): Seq[OfferTypes.EligibilityRule]
}

object OfferSystem extends OffersDomain {
  def isEligible(user: User, rules: Seq[OfferTypes.EligibilityRule]): Boolean = {
    false
  }

  def eligibilityRules(offer: OfferTypes.Offer): Seq[OfferTypes.EligibilityRule] = {
    offer match {
      case OfferTypes.CreditCard(_, _) => Seq(
        OfferTypes.CreditScoreRange(offer.creditScoreRange)
      )
      case OfferTypes.PersonalLoan(_, _, _) => Seq(
        OfferTypes.CreditScoreRange(offer.creditScoreRange),
        OfferTypes.MaxLoanAmount(offer.term)
      )
    }
  }
}
