package krs.partner.service

import com.twitter.util.{ Future }
import krs.common.{ PartnerUtil }
import krs.thriftscala.{ PartnerService, OfferResponse }

import krs.partner.api.{ PartnerApi }

object PartnerServiceImpl {
  def apply(api: PartnerApi) = {
    new PartnerService[Future] {
      def getOffers() = {
        val partnerOffers = api.getOffers(550).map(PartnerUtil.convertOffer)
        Future.value(OfferResponse(partnerOffers))
      }
    }
  }
}
