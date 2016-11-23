package krs

trait OfferTrait {

  // abstract value types should be declared as val. Really, the only things
  // that should be a var should be localally scoped and never leak outside
  // of that scope.
  val minimumCreditScore: Short
  val maximumCreditScore: Short

  // Attaching behavior to data is one way of solving the problem (and in the
  // context of our offers system, we actually lead people down this path.
  // The other option is to separate the 'data' (offer) from the 'behavior'
  // (isEligable)
  def isEligable(user: User): Boolean = {
    user.creditScore >= minimumCreditScore && user.creditScore <= maximumCreditScore
  }
}

// Converting this to a case class allows allows you to do a few things include
// not having to worry about the 'val' inside the parameter list. This current
// implementation only makes the provider public which is nice however. Case
// class implementations also allow you to pattern match, while normal classes
// do not. The change to this class would simply be adding "case class" to the
// front and removing the redundant "val" in front of provider. If you want to
// keep minScore and maxScore private, you can put "private val" in front of them.
//
// case class CreditCardOffer(
//    provider: String,
//    private val minScore: Short,
//    private val maxScore: Short) extends OfferTrait {
class CreditCardOffer(val provider: String, minScore: Short, maxScore: Short) extends OfferTrait {
  val minimumCreditScore = minScore
  val maximumCreditScore = maxScore
}


/**
 * Added as demonstration
 * ---------------------------------------------------------------------------
 */

// The data which this program operates on
trait OfferT {
  val creditScoreRange: Range
}

case class CCOffer(provider: String, creditScoreRange: Range) extends OfferT

// Singleton object housing functions for eligibility checks
object Eligibility {
  def isEligable(offer: OfferT, user: User): Boolean =
    user.creditScore >= offer.creditScoreRange.min &&
    user.creditScore <= offer.creditScoreRange.max
}
