package krs.service

import com.twitter.util.{ Await, Future }
import com.twitter.finagle.Thrift
import com.twitter.finagle.stats.Counter
import com.twitter.server.TwitterServer

object PartnerServer extends TwitterServer with ServiceModule {
  val service = partnerApi()
  val partnerService: Counter = statsReceiver.counter("partnerService")

  def main(): Unit = {
    val server = Thrift.server
      .withStatsReceiver(statsReceiver)
      .serveIface("localhost:8081", service)

    onExit { server.close() }
    Await.ready(server)
  }
}
