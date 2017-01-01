package krs.partner.service

import com.twitter.util.{ Await }
import com.twitter.finagle.Thrift
import com.twitter.finagle.stats.Counter
import com.twitter.server.TwitterServer

import krs.partner.infrastructure.InfrastructureModule
import krs.partner.api.ApiModule
import krs.partner.domain.DomainModule

object PartnerServer
    extends TwitterServer
    with ServiceModule
    with ApiModule
    with DomainModule {

  val serviceImpl = PartnerServiceImpl(partnerApi)

  val partnerService = statsReceiver.counter("partnerService")

  def main(): Unit = {
    val server = Thrift.server
      .withStatsReceiver(statsReceiver)
      .serveIface("localhost:8081", serviceImpl)

    onExit { server.close() }
    Await.ready(server)
  }
}
