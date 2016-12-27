package krs.infrastructure

import krs.domain._

case class PartnerRepositoryMemory() extends PartnerRepository {
  def loadOffers(): List[Offer] = {
    List(
      CreditCard("Offer01", Range(500, 700)),
      CreditCard("Offer02", Range(550, 700)),
      CreditCard("Offer03", Range(600, 700)),
      CreditCard("Offer04", Range(650, 700)),
      CreditCard("Offer05", Range(700, 770)),
      CreditCard("Offer06", Range(750, 770)),
      PersonalLoan("Offer07", Range(500, 700), 0.00, 12),
      PersonalLoan("Offer08", Range(550, 700), 0.00, 12),
      PersonalLoan("Offer09", Range(500, 700), 100.00, 12),
      PersonalLoan("Offer10", Range(750, 770), 100.00, 12)
    )
  }
}
