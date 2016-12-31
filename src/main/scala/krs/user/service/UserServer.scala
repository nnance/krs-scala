package krs.user.service

import com.twitter.util.{ Await }
import com.twitter.finagle.Thrift
import com.twitter.finagle.stats.Counter
import com.twitter.server.TwitterServer

import krs.user.infrastructure.InfrastructureModule
import krs.user.api.ApiModule
import krs.user.domain.DomainModule

object UserServer
    extends TwitterServer
    with InfrastructureModule
    with ApiModule
    with DomainModule {

  val userImpl = UserServiceImpl(userApi)

  val userService = statsReceiver.counter("userService")

  def main(): Unit = {
    val server = Thrift.server
      .withStatsReceiver(statsReceiver)
      .serveIface("localhost:8082", userImpl)

    onExit { server.close() }
    Await.ready(server)
  }
}
