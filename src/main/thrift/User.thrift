namespace java krs.thriftjava
#@namespace scala krs.thriftscala

include "./Partner.thrift"

struct User {
  1: required i32 id;
  2: required string name;
  3: required i32 creditScore;
  4: optional double outstandingLoanAmount;
  5: optional list<Partner.PartnerOffer> offers;
}

service UserService {
  list<User> getUsers();
  User getUser(1:i32 id);
  User getUserWithOffers(1:i32 id);
}
