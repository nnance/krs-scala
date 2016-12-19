package krs.service.client

import com.twitter.app.Flag
import com.twitter.finagle.{ Http, Service }
import com.twitter.finagle.http.{ Request, Response }
import com.twitter.finagle.stats.Counter
import com.twitter.server.TwitterServer
import io.finch._
import io.finch.circe._
import io.circe.generic.auto._
import com.twitter.util.Await

object APIService {
  val api: Service[Request, Response] = (
    OfferAPI.getOffers :+: UserAPI.getUsers :+: UserAPI.getUser
  ).toServiceAs[Application.Json]
}

object APIServer extends TwitterServer {

  val port: Flag[Int] = flag("port", 8080, "TCP port for HTTP server")

  val api: Counter = statsReceiver.counter("api")

  def main(): Unit = {
    log.info("Serving the Client application")

    val server = Http.server
      .withStatsReceiver(statsReceiver)
      .serve(s":${port()}", APIService.api)

    onExit { server.close() }

    Await.ready(server)
  }
}
