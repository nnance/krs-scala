package krs

import scala.io.Source
import org.json4s._
import org.json4s.native.JsonMethods._

object Loader {
  def readFile(fileName: String): String = {
    Source.fromFile(fileName).getLines.fold("")((x, y) => x + y)
  }

  def loadOffers(source: String): List[Offer] = {
    val json = parse(source)
    val offers = for {
      JArray(items) <- json
      JObject(offers) <- items
    } yield offers

    def deserializeCreditCard(json: JValue): CreditCard = {
      val JString(provider) = json \ "provider"
      val JInt(minScore) = json \ "minimumCreditScore"
      val JInt(maxScore) = json \ "maximumCreditScore"
      CreditCard(provider, Range(minScore.toInt, maxScore.toInt))
    }

    def deserializePersonalLoan(json: JValue): PersonalLoan = {
      val JString(provider) = json \ "provider"
      val JInt(minScore) = json \ "minimumCreditScore"
      val JInt(maxScore) = json \ "maximumCreditScore"
      val JInt(term) = json \ "term"
      val JInt(maxAmt) = json \ "maximumAmount"
      PersonalLoan(provider, Range(minScore.toInt, maxScore.toInt), maxAmt.toDouble, term.toLong)
    }

    def deserialize(offer: List[(String, JValue)]): Option[Offer] = offer(0)_1 match {
      case "creditCard" => Option(deserializeCreditCard(offer(0)_2))
      case "personalLoan" => Option(deserializePersonalLoan(offer(0)_2))
      case _ => None
    }
    offers.map(deserialize).flatten
  }
}
