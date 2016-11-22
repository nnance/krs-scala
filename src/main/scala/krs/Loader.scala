package krs

object Loader {
  def loadOffers(): List[OfferTrait] = {
    val ccOffer = new CreditCardOffer("Chase", 500, 700)
    val plOffer = new PersonalLoanOffer("Quicken", 500, 600, 12, 40000.00)
    List(ccOffer, plOffer)
  }

  def getOffersByUser(user: User): List[OfferTrait] = {
    val offers = loadOffers()
    val userFilter = (offer: OfferTrait) => offer.isEligable(user)
    offers.filter(userFilter)
  }
}
