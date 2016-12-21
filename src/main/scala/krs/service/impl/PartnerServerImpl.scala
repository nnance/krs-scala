package krs.service.impl

import com.twitter.util.{ Future }
import krs.thriftscala.{ PartnerService, PartnerOffer, OfferResponse }
import krs.api.ApiModule
import krs.domain.DomainModule
import krs.infrastructure.InfrastructureModule

class PartnerServerImpl() extends InfrastructureModule with ApiModule with DomainModule {
  def apply() = {
    new PartnerService[Future] {
      def getOffers() = {
        val partnerOffers = partnerApi.getOffers().map((offer) => {
          PartnerOffer(
            offer.provider,
            Option(offer.creditScoreRange.min),
            Option(offer.creditScoreRange.max))
        })
        Future.value(OfferResponse(partnerOffers))
      }
    }
  }
}
