package krs.common

import krs.thriftscala.{PartnerOffer}
import krs.partner.{Offer}

object PartnerUtil {
  def convertOffer(offer: Offer): PartnerOffer =
    PartnerOffer(
      offer.provider,
      Option(offer.creditScoreRange.min),
      Option(offer.creditScoreRange.max)
    )
}
