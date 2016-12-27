package krs.domain

trait DomainModule {
  def userRepository: UserRepository
  def partnerSystem: PartnerDomain
  def partnerRepository: PartnerRepository
}
