package krs.service

import com.twitter.finagle.{ Http, Service }
import com.twitter.finagle.http
import com.twitter.finagle.Thrift

import com.twitter.util.{ Await, Future }

import krs.thriftscala.{ PartnerService, PartnerOffer, OfferResponse }

object PartnerClient extends App {
  val service = new Service[http.Request, http.Response] {
    def apply(req: http.Request): Future[http.Response] = {
      val client: PartnerService.FutureIface =
        Thrift.client.newIface[PartnerService.FutureIface]("localhost:8081", classOf[PartnerService.FutureIface])

      client.getOffers().onSuccess { response =>
        println("Received response: " + response)
      }
      // val result: Future[Log.Result] = clientServiceIface.getOffers()

      Future.value(
        http.Response(req.version, http.Status.Ok)
      )
    }
  }

  Await.ready(Http.serve(":8080", service))
}
