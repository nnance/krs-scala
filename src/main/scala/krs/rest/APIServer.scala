package krs.rest

import com.twitter.app.Flag
import com.twitter.finagle.{Http, Service}
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.stats.Counter
import com.twitter.server.TwitterServer
import io.finch._
import io.finch.circe._
import io.circe.generic.auto._
import com.twitter.util.Await

object APIService {
  val api: Service[Request, Response] = (
    PartnerAPI.getOffers :+: UserAPI.find
  ).toServiceAs[Application.Json]
}

object APIServer extends TwitterServer {

  private val conf = com.typesafe.config.ConfigFactory.load()
  private val portConf = conf.getInt("krs.rest.port")
  private val port: Flag[Int] = flag("port", portConf, "TCP port for HTTP server")

  private val api: Counter = statsReceiver.counter("api")

  def main(): Unit = {
    log.info("Serving the Client application")

    val server = Http.server
      .withStatsReceiver(statsReceiver)
      .serve(s":${port()}", APIService.api)

    onExit { server.close() }

    Await.ready(server)
  }
}
