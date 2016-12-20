package krs.service

import com.twitter.util.{ Await }
import com.twitter.finagle.Thrift
import com.twitter.finagle.stats.Counter
import com.twitter.server.TwitterServer
import krs.service.impl.{ UserServerImpl }

object UserServer extends TwitterServer {
  val serviceImpl = new UserServerImpl()
  val userService: Counter = statsReceiver.counter("userService")

  def main(): Unit = {
    val server = Thrift.server
      .withStatsReceiver(statsReceiver)
      .serveIface("localhost:8082", serviceImpl())

    onExit { server.close() }
    Await.ready(server)
  }
}
