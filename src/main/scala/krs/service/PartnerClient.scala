package krs.service

import com.twitter.app.Flag
import com.twitter.finagle.{Http, Service}
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.stats.Counter
import com.twitter.server.TwitterServer
import io.finch._

import com.twitter.finagle.Thrift

import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._

import com.twitter.util.{ Await, Future }

import krs.thriftscala.{ PartnerService, PartnerOffer }

object PartnerClient extends TwitterServer {

  val port: Flag[Int] = flag("port", 8080, "TCP port for HTTP server")

  val todos: Counter = statsReceiver.counter("partnerclient")
  val client: PartnerService.FutureIface =
    Thrift.client.newIface[PartnerService.FutureIface]("localhost:8081", classOf[PartnerService.FutureIface])
  //
  // val service = new Service[http.Request, http.Response] {
  //   def apply(req: http.Request): Future[http.Response] = {
  //     client.getOffers().map((response) => {
  //       val json =
  //         ("offers" ->
  //           response.offers.map {
  //             case PartnerOffer(provider, Some(min), Some(max)) =>
  //               Some((
  //                 ("provider" -> provider) ~
  //                 ("minScore" -> min) ~
  //                 ("maxScore" -> max)
  //               ))
  //             case _ => None
  //           }.flatten)
  //       val httpResponse = http.Response(req.version, http.Status.Ok)
  //       httpResponse.setContentString(compact(render(json)))
  //       httpResponse
  //     })
  //   }
  // }
  def getOffers: Endpoint[List[PartnerOffer]] = get("offers") {
    client.getOffers().map(Ok)
  }

  val api: Service[Request, Response] = (
    getOffers
  ).toServiceAs[Application.Json]

  def main(): Unit = {
      log.info("Serving the Todo application")

      val server = Http.server
        .withStatsReceiver(statsReceiver)
        .serve(s":${port()}", api)

      onExit { server.close() }

      Await.ready(adminHttpServer)
    }
  }
