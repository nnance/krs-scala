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

    def deserialize(offer: List[(String, JValue)]): Option[Offer] = offer(0)_1 match {
      case "creditCard" => {
        val json: JValue = offer(0)_2
        val JString(provider) = json \ "provider"
        val JInt(minScore) = json \ "minimumCreditScore"
        val JInt(maxScore) = json \ "maximumCreditScore"
        Option(CreditCard(provider, Range(minScore.toInt, maxScore.toInt)))
      }
      case _ => None
    }
    offers.map(deserialize).flatten
  }
}
