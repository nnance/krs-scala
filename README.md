# krs-scala

A significant part of Credit Karma's platform is identifying well-matched
financial offerings for our users based on many factors including a user's
current financial profile. For this test you'll be asked to develop a
rudimentary system that does exactly that.

The goal of the system is to serve offers to users. You'll develop this
in a stepwise manner. Whenever you're asked to serve an offer to the user
the implementation should result in the offers being printed to the console.

* Step 1: Return all of the offers from each service to the user.
* Step 2: Only return offers from each service where the user's credit score is between (inclusive) the offers minimum and maximum score.
* Step 3: Serve all relevant offers from step 2 and only the auto loan offers to the user if the following conditions are true:
  * (a) The user's credit score is within the offer's range
  * (b) The user's current outstanding loan is less than or equal to the auto loan offer's high limit (maximumAmount).
