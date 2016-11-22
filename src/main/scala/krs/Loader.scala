package krs

object Loader {
  def loadOffers(): List[OfferTrait] = {
    val ccOffer = new CreditCardOffer("Chase", 500, 700)
    val plOffer = new PersonalLoanOffer("Quicken", 500, 700, 12, 40000.00)
    List(ccOffer, plOffer)
  }
  
}
