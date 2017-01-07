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
  private val conf = com.typesafe.config.ConfigFactory.load()
  private val host = conf.getString("krs.user.host")

  private val client: UserService.FutureIface =
    Thrift.client.newIface[UserService.FutureIface](host, classOf[UserService.FutureIface])

  private def convertOffer(o: krs.thriftscala.PartnerOffer) =
    Offer(o.provider, o.minimumCreditScore.getOrElse(0), o.maximumCreditScore.getOrElse(0))

  private def convertUser(u: krs.thriftscala.User) =
    User(u.id, u.name, u.creditScore, u.offers.map(o => o.map(convertOffer)))

  def getUser: Endpoint[User] = get("user" :: int) { id: Int =>
    client.getUserWithOffers(id).map(u => Ok(convertUser(u)))
  }
}
