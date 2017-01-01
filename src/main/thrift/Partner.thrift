namespace java krs.thriftjava
#@namespace scala krs.thriftscala

struct PartnerOffer {
  1: required string provider;
  2: optional i32 minimumCreditScore;
  3: optional i32 maximumCreditScore;
}

struct PartnerResponse {
  1: required list<PartnerOffer> offers;
}

service PartnerService {
  PartnerResponse getOffers(1: i32 creditScore);
}
