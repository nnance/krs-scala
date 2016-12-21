package krs.domain

trait DomainModule {
  def userRepository: UserRepository
  def partnerRepository: PartnerRepository
}
