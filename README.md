# krs-scala

The goal of the system is to serve offers to users. Using the Ports and Adaptors pattern to illustrate how a service can be easily developed and tested in isolation before being deployed as microservices.

The project is a POC that supporting the concepts outlined in the associated [BLOG](blog/POST.md) post.

### Requirements

* Return offers filtered by credit score.
* Return user with eligible offers based on the following rules:
  * (a) The user's credit score is within the offer's range
  * (b) The user's current outstanding loan is less than or equal to the auto loan offer's high limit (maximumAmount).
* Design the eligibility system to be easily extended with new rules.
* Deserialize the sample data in an asynchronous manor and service the appropriate offers from step

### Data

The json data used for validation is included in the [data.json](./fixtures/data.json) file in the fixtures folder. This data should be deserialized and processed by type.  

## Getting started

Starting the Partner Service

```sh
sbt 'runMain krs.partner.PartnerServer -admin.port=:9990'
```

Starting the User Service

```sh
sbt 'runMain krs.user.UserServer -admin.port=:9991'
```

Starting the API Service

```sh
sbt 'runMain krs.rest.APIServer -admin.port=:9992'
```

## Testing

Confirm the services are working with the REST services:

Getting offers by credit score
```sh
curl http://localhost:8080/offers/550
```

Getting user with offers
```sh
curl http://localhost:8080/user/1
```


Load Testing
```sh
brew install wrk
wrk -t12 -c400 -d30s http://127.0.0.1:8080/user/2
```
