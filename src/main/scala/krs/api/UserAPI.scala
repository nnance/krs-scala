package krs.api

import com.twitter.finagle.Thrift

import io.finch._
import io.circe.{ Encoder, Json }

import krs.thriftscala.{ UserService, User }

trait UserEncoders {
  implicit val encodeUser: Encoder[User] = Encoder.instance(u =>
    Json.obj(
      "id" -> Json.fromInt(u.id),
      "name" -> Json.fromString(u.name),
      "creditScore" -> Json.fromInt(u.creditScore),
      "outstandingLoanAmount" -> Json.fromDoubleOrNull(u.outstandingLoanAmount.getOrElse(0))
    )
  )
}

object UserAPI {
  val client: UserService.FutureIface =
    Thrift.client.newIface[UserService.FutureIface]("localhost:8082", classOf[UserService.FutureIface])

  def getUsers: Endpoint[Seq[User]] = get("users") {
    client.getUsers().map(Ok)
  }

  def getUser: Endpoint[User] = get("user" :: int) { id: Int =>
    client.getUser(id).map(Ok)
  }
}
