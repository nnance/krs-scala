package krs.api

import com.twitter.app.Flag
import com.twitter.finagle.{ Http, Service }
import com.twitter.finagle.http.{ Request, Response }
import com.twitter.finagle.stats.Counter
import com.twitter.server.TwitterServer

import io.finch._
import io.finch.circe._
import io.circe.{ Encoder, Json }

import com.twitter.finagle.Thrift

import com.twitter.util.{ Await }

import krs.thriftscala.{ PartnerService, OfferResponse }

trait PartnerResponseEncoders {
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

object APIServer extends TwitterServer with PartnerResponseEncoders {

  val port: Flag[Int] = flag("port", 8080, "TCP port for HTTP server")

  val todos: Counter = statsReceiver.counter("partnerclient")

  val client: PartnerService.FutureIface =
    Thrift.client.newIface[PartnerService.FutureIface]("localhost:8081", classOf[PartnerService.FutureIface])

  def getOffers: Endpoint[OfferResponse] = get("offers") {
    client.getOffers().map(Ok)
  }

  val api: Service[Request, Response] = (
    getOffers
  ).toServiceAs[Application.Json]

  def main(): Unit = {
    log.info("Serving the Client application")

    val server = Http.server
      .withStatsReceiver(statsReceiver)
      .serve(s":${port()}", api)

    onExit { server.close() }

    Await.ready(server)
  }
}
