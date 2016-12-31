package krs.rest

import com.twitter.finagle.Thrift

import io.finch._
import krs.thriftscala.{ UserService }

case class User(
  id: Int,
  name: String,
  creditScore: Int,
  offers: Option[Seq[Offer]] = None)

object UserAPI {
  val client: UserService.FutureIface =
    Thrift.client.newIface[UserService.FutureIface]("localhost:8082", classOf[UserService.FutureIface])

  def convertOffer(o: krs.thriftscala.PartnerOffer) =
    Offer(o.provider, o.minimumCreditScore.getOrElse(0), o.maximumCreditScore.getOrElse(0))

  def convertUser(u: krs.thriftscala.User) =
    User(u.id, u.name, u.creditScore, u.offers.map(o => o.map(convertOffer)))

  def getUsers: Endpoint[Seq[User]] = get("users") {
    client.getUsers().map(users => Ok(users.map(convertUser)))
  }

  def getUser: Endpoint[User] = get("user" :: int) { id: Int =>
    client.getUserWithOffers(id).map(u => Ok(convertUser(u)))
  }
}
