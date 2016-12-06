package krs.service

import com.twitter.finagle.{ Http, Service }
import com.twitter.finagle.http
import com.twitter.finagle.Thrift

import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._

import com.twitter.util.{ Await, Future }

import krs.thriftscala.{ PartnerService, OfferResponse }

object PartnerClient extends App {
  val service = new Service[http.Request, http.Response] {
    def apply(req: http.Request): Future[http.Response] = {
      val client: PartnerService.FutureIface =
        Thrift.client.newIface[PartnerService.FutureIface]("localhost:8081", classOf[PartnerService.FutureIface])

      client.getOffers().map((response: OfferResponse) => {
        val json =
          ("offers" ->
            response.offers.map { offer =>
              (("provider" -> offer.provider))
            })
        // println(compact(render(json)))
        // val result: Future[Log.Result] = clientServiceIface.getOffers()

        val httpResponse = http.Response(req.version, http.Status.Ok)
        httpResponse.setContentString(compact(render(json)))
        httpResponse
      })
    }
  }

  Await.ready(Http.serve(":8080", service))
}
