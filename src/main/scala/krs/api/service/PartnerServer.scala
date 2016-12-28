package krs.api.service

import com.twitter.util.{ Future }
import krs.thriftscala.{ PartnerService, PartnerOffer, OfferResponse }

import krs.domain.{ PartnerRepository, Offer }

object PartnerUtil {
  def convertOffer(offer: Offer) = {
    PartnerOffer(
      offer.provider,
      Option(offer.creditScoreRange.min),
      Option(offer.creditScoreRange.max))
  }
}

class PartnerServer(partnerRepository: PartnerRepository) {
  def apply() = {
    new PartnerService[Future] {
      def getOffers() = {
        val partnerOffers = partnerRepository.loadOffers().map(PartnerUtil.convertOffer)
        Future.value(OfferResponse(partnerOffers))
      }
    }
  }
}
