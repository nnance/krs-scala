package fps.partner

object PartnerInMemory extends PartnerSystem {
  import PartnerDomain._

  def getOffers(): List[Offer] =
    InMemoryPartnerRepository.getOffers
}
