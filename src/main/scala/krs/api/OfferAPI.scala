package krs.api

import com.twitter.finagle.Thrift

import io.finch._
import io.circe.{ Encoder, Json }

import krs.thriftscala.{ PartnerService, OfferResponse }

trait OfferResponseEncoders {
  implicit val encodeOfferResponse: Encoder[OfferResponse] = Encoder.instance(e =>
    Json.obj(
      "offers" -> Json.fromValues(e.offers.map((o) => {
        Json.obj(
          "provider" -> Json.fromString(o.provider),
          "minimumScore" -> Json.fromInt(o.minimumCreditScore.getOrElse(0)),
          "maximumScore" -> Json.fromInt(o.maximumCreditScore.getOrElse(0))
        )
      }))
    )
  )
}

object OfferAPI {
  val client: PartnerService.FutureIface =
    Thrift.client.newIface[PartnerService.FutureIface]("localhost:8081", classOf[PartnerService.FutureIface])

  def getOffers: Endpoint[OfferResponse] = get("offers") {
    client.getOffers().map(Ok)
  }
}
