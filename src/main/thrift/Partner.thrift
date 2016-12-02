namespace java krs.thriftjava
#@namespace scala krs.thriftscala

struct PartnerOffer {
  1: required string provider;
  2: optional i32 minimumCreditScore;
  3: optional i32 maximumCreditScore;
}

struct OfferResponse {
  1: required list<PartnerOffer> offers;
}

service PartnerService {
  OfferResponse getOffers();
}
