package krs

import java.net.InetSocketAddress
import org.apache.thrift.protocol.TBinaryProtocol

import com.twitter.util.{ Await, Future }
import com.twitter.finagle.Thrift
import com.twitter.finagle.ThriftMux
import com.twitter.finagle.thrift.ThriftServerFramedCodec
import com.twitter.finagle.builder.{ ServerBuilder, Server }

import thriftscala.{ PartnerService, PartnerOffer, OfferResponse }
import krs.PartnerSystem._

object PartnerServer {

  def main(args: Array[String]) {
    val server = Thrift.serveIface("localhost:8081", new PartnerService[Future] {
      def getOffers() = {
        val offers = loadOffers(readFile("./fixtures/data.json"))
        val partnerOffers = offers.map((offer) => {
          PartnerOffer(
            offer.provider,
            Option(offer.creditScoreRange.min),
            Option(offer.creditScoreRange.max))
        })
        Future(OfferResponse(partnerOffers))
      }
    })
    Await.ready(server)
  }
}
