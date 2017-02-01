package krs.common

import krs.partner.PartnerDomain.Offer
import krs.thriftscala.PartnerOffer

object PartnerUtil {
  def convertOffer(offer: Offer): PartnerOffer =
    PartnerOffer(
      offer.provider,
      Option(offer.creditScoreRange.min),
      Option(offer.creditScoreRange.max)
    )
}
