# krs-scala

The goal of the system is to serve offers to users. To be developed
in a stepwise manner. The system should include unit tests that validate each
of the requirements mentioned below.  The system should be designed in such a manor
that new types of offers can be easily added to the pipeline without significant
changes to the software architecture.

### Requirements

* Step 1: Return all of the offers from each service to the user.
* Step 2: Only return offers from each service where the user's credit score is between (inclusive) the offers minimum and maximum score.
* Step 3: Serve all relevant offers from step 2 and only the auto loan offers to the user if the following conditions are true:
  * (a) The user's credit score is within the offer's range
  * (b) The user's current outstanding loan is less than or equal to the auto loan offer's high limit (maximumAmount).
* Step 4: Deserialize the sample data in an asynchronous manor and service the appropriate offers from step 3.

### Data

The json data used for validation is included in the [data.json](./fixtures/data.json) file in the fixtures folder. This data
should be deserialized and processed by type.  

## Getting started

Starting the Partner Service

```sh
sbt "runMain krs.service.PartnerServer"
```

Starting the API Service

```sh
sbt "runMain krs.api.APIServer -admin.port=:9991"
```

## Testing

Confirming API Service

```sh
curl http://localhost:8080/offers
```

Load Testing
```sh
brew install wrk
wrk -t12 -c400 -d30s http://127.0.0.1:8080/offers
```
