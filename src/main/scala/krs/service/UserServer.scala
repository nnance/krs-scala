package krs.service

import com.twitter.util.{ Await }
import com.twitter.finagle.Thrift
import com.twitter.finagle.stats.Counter
import com.twitter.server.TwitterServer

import krs.infrastructure.InfrastructureModule

object UserServer extends TwitterServer with ServiceModule {
  val service = userApi()
  val userService: Counter = statsReceiver.counter("userService")

  def main(): Unit = {
    val server = Thrift.server
      .withStatsReceiver(statsReceiver)
      .serveIface("localhost:8082", service)

    onExit { server.close() }
    Await.ready(server)
  }
}
