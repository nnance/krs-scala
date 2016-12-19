package krs.service

import com.twitter.util.{ Await, Future }
import com.twitter.finagle.Thrift
import com.twitter.finagle.stats.Counter
import com.twitter.server.TwitterServer
import krs.thriftscala.{ User, UserService }
import krs.infrastructure.{ Injector }

class UserServiceImpl() {

  def apply(): UserService[Future] = {
    new UserService[Future] {
      val infra = new Injector()
      def getUsers() = {
        val serviceUsers: List[User] = infra.userApi.getUsers().map((u) => {
          User(u.id, u.name, u.creditScore, Some(u.outstandingLoanAmount))
        })
        Future.value(serviceUsers)
      }

      def getUser(id: Int) = {
        val u = infra.userApi.getUser(id)
        Future.value(
          User(u.id, u.name, u.creditScore, Some(u.outstandingLoanAmount))
        )
      }
    }
  }
}

object UserServer extends TwitterServer {
  val serviceImpl: UserServiceImpl = new UserServiceImpl()
  val userService: Counter = statsReceiver.counter("userService")

  def main(): Unit = {
    val server = Thrift.server
      .withStatsReceiver(statsReceiver)
      .serveIface("localhost:8082", serviceImpl())

    onExit { server.close() }
    Await.ready(server)
  }
}
