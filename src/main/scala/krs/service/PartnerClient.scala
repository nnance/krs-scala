package krs.service

import com.twitter.finagle.{ Http, Service }
import com.twitter.finagle.http
import com.twitter.finagle.Thrift

import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._

import com.twitter.util.{ Await, Future }

import krs.thriftscala.{ PartnerService, OfferResponse }

object PartnerClient extends App {
  val client: PartnerService.FutureIface =
    Thrift.client.newIface[PartnerService.FutureIface]("localhost:8081", classOf[PartnerService.FutureIface])

  val service = new Service[http.Request, http.Response] {
    def apply(req: http.Request): Future[http.Response] = {
      client.getOffers().map((response) => {
        val json =
          ("offers" ->
            response.offers.map { offer =>
              (
                ("provider" -> offer.provider) ~
                ("minScore" -> offer.minimumCreditScore.getOrElse(0)) ~
                ("maxScore" -> offer.maximumCreditScore.getOrElse(0))
              )
            })
        val httpResponse = http.Response(req.version, http.Status.Ok)
        httpResponse.setContentString(compact(render(json)))
        httpResponse
      })
    }
  }

  Await.ready(Http.serve(":8080", service))
}
