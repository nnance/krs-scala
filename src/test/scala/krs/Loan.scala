import org.scalatest._
import krs.PersonalLoanOffer

class LoanSpec extends FlatSpec with Matchers {
  val loan = new PersonalLoanOffer("Chase", 500, 700, 12, 40000.00)

  "Loan provider" should "be Chase for new offer" in {
    loan.provider should be("Chase")
  }

  "Loan isEligable" should "be true for credit score within range" in {
    loan.isEligable(500) should be(true)
  }

  "Loan isEligable" should "be true for max debt within range" in {
    loan.isEligable(500, 39999.99) should be(true)
  }

  "Loan isEligable" should "be false for max debt out of range" in {
    loan.isEligable(500, 40000.01) should be(false)
  }

  "Loan isEligable" should "be false for score out of range" in {
    loan.isEligable(499, 30000.00) should be(false)
  }

  "Loan term" should "be 12 months" in {
    loan.term should be(12)
  }
}
