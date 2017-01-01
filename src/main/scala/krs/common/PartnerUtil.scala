package krs.common

import krs.thriftscala.{ PartnerOffer }
import krs.partner.domain.{ Offer }

object PartnerUtil {
  def convertOffer(offer: Offer) = {
    PartnerOffer(
      offer.provider,
      Option(offer.creditScoreRange.min),
      Option(offer.creditScoreRange.max))
  }
}
