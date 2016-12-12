package krs.api

import com.twitter.app.Flag
import com.twitter.finagle.{ Http, Service }
import com.twitter.finagle.http.{ Request, Response }
import com.twitter.finagle.stats.Counter
import com.twitter.server.TwitterServer

import io.finch._
import io.finch.circe._
import io.circe.generic.auto._

import com.twitter.finagle.Thrift

import com.twitter.util.{ Await, Future }

import krs.thriftscala.{ PartnerService, PartnerOffer, OfferResponse }

object APIServer extends TwitterServer {

  val port: Flag[Int] = flag("port", 8080, "TCP port for HTTP server")

  val todos: Counter = statsReceiver.counter("partnerclient")

  case class Offer(partner: String, minimumScore: Int, maximumScore: Int)

  val client: PartnerService.FutureIface =
    Thrift.client.newIface[PartnerService.FutureIface]("localhost:8081", classOf[PartnerService.FutureIface])

  def getOffers: Endpoint[Seq[Offer]] = get("offers") {
    client.getOffers().map((response) => {
      val offers = response.offers.map {
        case PartnerOffer(provider, Some(min), Some(max)) =>
          Some(Offer(provider, min, max))
        case _ => None
      }.flatten
      Ok(offers)
    })
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
