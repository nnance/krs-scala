package krs

/**
  * Created by nicknance on 1/24/17.
  */
object DecoratorPattern {
  def evenCheck(x: Int): Boolean = (x % 2) == 0

  def logValue[A](x: A): A = {
    System.out.println(x)
    x
  }

  def isEven(x: Int): Boolean = logValue(evenCheck(logValue(x)))

  type CustomerId = Int

  case class Customer(
    id: CustomerId,
    name: String
  )

  type GetCustomer = CustomerId => Option[Customer]

  def getCustomerFromList(customers: List[Customer], id: CustomerId): Option[Customer] =
    customers.find(customer => customer.id == id)

  val customers = List(
    Customer(1, "Test"),
    Customer(2, "Other Test")
  )

  def getCustomer: GetCustomer = getCustomerFromList(customers, _)
}

