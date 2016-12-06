import org.scalatest._

import com.twitter.util.{ Future, Closable }
import com.twitter.finagle.Thrift
import com.twitter.finagle.thrift.ThriftClientFramedCodec
import com.twitter.finagle.builder.ClientBuilder
import krs.thriftscala.{ PartnerService, OfferResponse }
import krs.service.{ PartnerServer }


class ServerTest extends FunSuite with BeforeAndAfterEach {
  var server: PartnerService[Future] = _
  var client: Service[HttpRequest, HttpResponse] = _
  // var client: PartnerService.FutureIface = _

  override def beforeEach(): Unit = {
    server = PartnerServer.buildServer()
    client = Thrift.client.newIface[PartnerService.FutureIface]("localhost:8081", classOf[PartnerService.FutureIface])
    client = ClientBuilder()
      .codec(ThriftClientFramedCodec())
      .hosts("localhost:8081")
      .hostConnectionLimit(1)
      .build()
    // client.getOffers()
  }
  override def afterEach(): Unit = {
    Closable.all(server, client).close()
  }
}
