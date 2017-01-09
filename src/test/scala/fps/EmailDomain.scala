package fps

import org.scalatest._

class EmailSystemSpec extends FlatSpec with Matchers {
  import EmailSystem._

  val helloEmail = Email(
    subject = "It's me again, your stalker friend!",
    text = "Hello my friend! How are you?",
    sender = "johndoe@example.com",
    recipient = "me@example.com"
  )

  val mails = helloEmail :: Nil

  "EmailSystem" should "allow us to filter email by sent by" in {
    newMailsForUser(mails, notSentByAnyOf(Set("johndoe@example.com"))) should be(Nil)
    newMailsForUser(mails, notSentByAnyOf(Set("abc@example.com"))) should be(helloEmail :: Nil)
  }

  "EmailSystem" should "allow us to filter email by size" in {
    newMailsForUser(mails, maximumSize(5)) should be(Nil)
    newMailsForUser(mails, maximumSize(50)) should be(helloEmail :: Nil)
  }
  /**
   * "EmailSystem" should "allow us to filter email by size" in {
   * val filter: EmailFilter = every(
   * notSentByAnyOf(Set("johndoe@example.com")),
   * minimumSize(100),
   * maximumSize(10000)
   * )
   * newMailsForUser(mails, filter) should be(Nil)
   * }
   */
}