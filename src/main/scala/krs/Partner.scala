package krs

import scala.io.Source
import org.json4s._
import org.json4s.native.JsonMethods._

trait PartnerDomain {
  sealed trait Serializable[T] {
    def deserialize(json: JValue): T
  }
}

object PartnerSystem extends PartnerDomain {
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

  def readFile(fileName: String): String = {
    val bufferedSource = Source.fromFile(fileName)
    val fileCache = bufferedSource.getLines.fold("")((x, y) => x + y)
    bufferedSource.close()
    fileCache
  }

  def loadOffers(source: String): List[Offer] = {
    val json = parse(source)
    val offers = for {
      JArray(items) <- json
      JObject(offers) <- items
    } yield offers

    def deserialize(offer: List[(String, JValue)]): Option[Offer] = offer(0)._1 match {
      case "creditCard" => Option(CreditCardSerializable.deserialize(offer(0)._2))
      case "personalLoan" => Option(PersonalLoanSerializable.deserialize(offer(0)._2))
      case _ => None
    }
    offers.map(deserialize).flatten
  }
}
