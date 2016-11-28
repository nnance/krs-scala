package krs

import org.json4s._
import org.json4s.native.JsonMethods._

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
  providerName: String,
  scoreRange: Range
) extends Offer {
  val provider = providerName
  val creditScoreRange = scoreRange
}

case class PersonalLoan(
  providerName: String,
  scoreRange: Range,
  val maxLoanAmount: Double,
  val term: Long
) extends Offer {
  val provider = providerName
  val creditScoreRange = scoreRange
}

// Here is our ADT for what an eligibility rule is. Each rule can be one of
// the following choices (max loan amount is x, credit score range is min/max)
sealed trait Rule
case class CreditScoreRange(val range: Range) extends Rule
case class MaxLoanAmount(val amount: Double) extends Rule

sealed trait EligibilityRule[T] {
  def isEligible(user: User, rule: T): Boolean
}

trait OffersDomain {
  def isEligible(user: User, offer: Offer): Boolean
  def filterEligible(user: User, offers: Seq[Offer]): Seq[Offer]
}

object OfferSystem extends OffersDomain {
  sealed trait Serializable[T] {
    def deserialize(json: JValue): T
  }

  object CreditCardSerializable extends Serializable[CreditCard] {
    def deserialize(json: JValue): CreditCard = {
      val JString(provider) = json \ "provider"
      val JInt(minScore) = json \ "minimumCreditScore"
      val JInt(maxScore) = json \ "maximumCreditScore"
      CreditCard(provider, Range(minScore.toInt, maxScore.toInt))
    }
  }

  object PersonalLoanSerializable extends Serializable[PersonalLoan] {
    def deserialize(json: JValue): PersonalLoan = {
      val JString(provider) = json \ "provider"
      val JInt(minScore) = json \ "minimumCreditScore"
      val JInt(maxScore) = json \ "maximumCreditScore"
      val JInt(term) = json \ "term"
      val JInt(maxAmt) = json \ "maximumAmount"
      PersonalLoan(provider, Range(minScore.toInt, maxScore.toInt), maxAmt.toDouble, term.toLong)
    }
  }

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
