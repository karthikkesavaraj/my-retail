# My Retail Product Service

My retail product service is a service that provides Product Name and Price for any given proudct id.
The application starts in http://localhost:8080

## Pre requisites
The following are required to execute this project.
* Jdk 8
* Rest Client (Advanced REST client / Postman / Curl)
* MongoDb in localhost (only if using Option 1 to Run)
* Internet connection to call https://redsky.target.com
* Docker (If using Option 2)

## How to Run
Threre are two options to run the project. In either case use your own Rest client to consume the apis.
### Option 1 - bootRun
The project is build using spring boot and gradle. Clone the project and run "./gradlew bootRun" 
> ./gradlew bootRun

The application runs with default profile. To change the default profile, add new profile section in,
> src/main/resources/application.yml

In Windows set property SPRING_PROFILES_ACTIVE as below
> SET SPRING_PROFILES_ACTIVE=docker

In Linux / Unix set property SPRING_PROFILES_ACTIVE as below
>export SPRING_PROFILES_ACTIVE=docker

Then run 
> ./gradlew bootRun

### Option 2 - docker
The project is dockerized. Make sure you have docker installed (mimimum version 17.x).
By default the Spring profile is set to docker
to change update Dockerfile. 

Build the project using command,
> ./gradlew clean build

Run below command to dockerize your application from your project root directory.
The Dockerfile is in the project root directory. 
> docker build -t my-retail-pdp .

Run compose up as below from your project root directory to start your application as well as mongo db.
>docker-compose up

To stop the application, run below command
>docker-compose down


## APIs
The microservice expose 2 apis. 
>/v1/product/{id} - GET

Sample Reqeust & Response
```$xslt
curl -kv http://localhost:8080/v1/product/53732562
{"id":53732562,"name":"CND Vinylux Long Wear Nail Polish - 0.5 fl oz","current_price":{"value":11.5,"currency_code":"USD"}}
```

>/v1/product - PUT
Sample Request Object:
```$xslt
{"id":53732562,"name":"CND Vinylux Long Wear Nail Polish - 0.5 fl oz","current_price":{"value":11.5,"currency_code":"USD"}}
```

###Supported Product Ids
By default the application loads a few product ids during boot.
#### Product with Price but not Name
Below products are in DB. But do not have a name.

15117729,
164883589,
16696652,
16752456,
15643793,

You should get **404 Status code**  error for these products

#### Product with price and name
Below products are consumable from the api. So will return a success response
 
13860428,
54382797

## TODO
* Add reactive feature
* Enable Authentication for PUT Method
* Write Integration Test