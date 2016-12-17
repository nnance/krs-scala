package krs.service

import com.twitter.util.{ Await, Future }
import com.twitter.finagle.Thrift
import com.twitter.finagle.stats.Counter
import com.twitter.server.TwitterServer
import krs.thriftscala._
import krs.UserSystem._

case class UserNotFound(id: Int) extends Exception {
  override def getMessage: String = s"User(${id.toString}) not found."
}

object UserServer extends TwitterServer {
  val users = loadUsers(readFile("./fixtures/users.json"))

  def buildServer(): UserService[Future] = {
    new UserService[Future] {
      def getUsers() = {
        val serviceUsers: List[krs.thriftscala.User] = users.map((user) => {
          User(user.id, user.name, user.creditScore, Some(user.outstandingLoanAmount))
        })
        Future.value(serviceUsers)
      }

      def getUser(id: Int) = {
        val user: krs.thriftscala.User = users.find((user) => user.id == id) match {
          case Some(u) => User(u.id, u.name, u.creditScore, Some(u.outstandingLoanAmount))
          case None => throw UserNotFound(id)
        }
        Future.value(user)
      }
    }
  }

  val userService: Counter = statsReceiver.counter("userService")

  def main(): Unit = {
    val service = buildServer()

    val server = Thrift.server
      .withStatsReceiver(statsReceiver)
      .serveIface("localhost:8082", service)

    onExit { server.close() }
    Await.ready(server)
  }
}
