package krs.service.client

import com.twitter.finagle.Thrift

import io.finch._
import krs.thriftscala.{ UserService }

case class User(id: Int, name: String, creditScore: Int)

object UserAPI {
  val client: UserService.FutureIface =
    Thrift.client.newIface[UserService.FutureIface]("localhost:8082", classOf[UserService.FutureIface])

  def convertUser(u: krs.thriftscala.User) =
    User(u.id, u.name, u.creditScore)

  def getUsers: Endpoint[Seq[User]] = get("users") {
    client.getUsers().map(users => Ok(users.map(convertUser)))
  }

  def getUser: Endpoint[User] = get("user" :: int) { id: Int =>
    client.getUser(id).map(u => Ok(convertUser(u)))
  }
}
