package krs.partner.service

import com.twitter.util.{ Future }
import krs.thriftscala.{ PartnerService, PartnerOffer, OfferResponse }

import krs.partner.api.{ PartnerApi }
import krs.partner.domain.{ Offer }

object PartnerServiceImpl {
  def convertOffer(offer: Offer) = {
    PartnerOffer(
      offer.provider,
      Option(offer.creditScoreRange.min),
      Option(offer.creditScoreRange.max))
  }

  def apply(api: PartnerApi) = {
    new PartnerService[Future] {
      def getOffers() = {
        val partnerOffers = api.getOffers(550).map(convertOffer)
        Future.value(OfferResponse(partnerOffers))
      }
    }
  }
}
