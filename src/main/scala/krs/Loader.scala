package krs

import scala.io.Source
import org.json4s._
import org.json4s.native.JsonMethods._
import krs.OfferSystem._

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
      case "creditCard" => Option(CreditCardSerializable.deserialize(offer(0)_2))
      case "personalLoan" => Option(PersonalLoanSerializable.deserialize(offer(0)_2))
      case _ => None
    }
    offers.map(deserialize).flatten
  }
}
