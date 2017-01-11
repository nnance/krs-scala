package fps

object EmailSystem {

  case class Email(
    subject: String,
    text: String,
    sender: String,
    recipient: String
  )

  type EmailFilter = Email => Boolean
  def newMailsForUser(mails: Seq[Email], f: EmailFilter) = mails.filter(f)

  val sentByOneOf: Set[String] => EmailFilter =
    senders => email => senders.contains(email.sender)
  val notSentByAnyOf: Set[String] => EmailFilter =
    senders => email => !senders.contains(email.sender)

  type SizeChecker = Int => Boolean
  val emailSize: SizeChecker => EmailFilter = f => email => f(email.text.size)
  val minimumSize: Int => EmailFilter = n => emailSize(_ >= n)
  val maximumSize: Int => EmailFilter = n => emailSize(_ <= n)

}

object EmailSystemV2 {
  import EmailSystem._

  type SizeChecker = Int => Boolean
  val sizeConstraint: SizeChecker => EmailFilter = f => email => f(email.text.size)

  val minimumSize: Int => EmailFilter = n => sizeConstraint(_ >= n)
  val maximumSize: Int => EmailFilter = n => sizeConstraint(_ <= n)

  def complement[A](predicate: A => Boolean) = (a: A) => !predicate(a)
  val notSentByAnyOf = sentByOneOf andThen (complement(_))

  def any[A](predicates: (A => Boolean)*): A => Boolean = a => predicates.exists(pred => pred(a))
  def none[A](predicates: (A => Boolean)*) = complement(any(predicates: _*))
  def every[A](predicates: (A => Boolean)*) = none(predicates.view.map(complement(_)): _*)

  val addMissingSubject = (email: Email) =>
    if (email.subject.isEmpty) email.copy(subject = "No subject")
    else email
  val checkSpelling = (email: Email) =>
    email.copy(text = email.text.replaceAll("your", "you're"))
  val removeInappropriateLanguage = (email: Email) =>
    email.copy(text = email.text.replaceAll("dynamic typing", "**CENSORED**"))
  val addAdvertismentToFooter = (email: Email) =>
    email.copy(text = email.text + "\nThis mail sent via Super Awesome Free Mail")

  val pipeline = Function.chain(Seq(
    addMissingSubject,
    checkSpelling,
    removeInappropriateLanguage,
    addAdvertismentToFooter
  ))
}