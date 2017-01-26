package krs

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by nicknance on 1/25/17.
  */
class DecoratorPatternSpec extends FlatSpec with Matchers {
  import DecoratorPattern._

  "isEven" should "return true for an even number" in {
    isEven(2) should be (true)
  }

  "isEven" should "return false for an odd number" in {
    isEven(1) should be (false)
  }

  "getCustomer for id 1" should "return a user" in {
    getCustomer(1).getOrElse(None) should not be(None)
  }

  "getCustomer for id 10" should "return not user" in {
    getCustomer(10).getOrElse(None) should be(None)
  }
}

