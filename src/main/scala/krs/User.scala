package krs

// Would suggest that user becomes a case class instead of 'class' .. its not a
// major thing at all, but it has added benefits that normal classes do not.
class User(val name: String, val creditScore: Short, val outstandingLoanAmount: Double)
