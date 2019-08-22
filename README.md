# **Implementation details**
Dropwizard has been used to bootstap the application. 

_The application represents the money in the lowest unit of currency(e.g. penny).<br/> 
For simplicity no currency has been defined.
An account model has property `accountId` and `amount`, defined as `Long` datatype._

ConcurrentHashMap implementation of java Map has been used as persistence of accounts. Currency conversion has not been implemented.

# **How to build** 
Maven has been used to manage the dependencies and create the builds. To build the application run
`mvn package`

# **How to run**
`java -jar target/banking-1.0-SNAPSHOT.jar server config.yml`
<br/>
The application runs on port 8080.
<br/>
Base Url: http://localhost:8080/


# **API's**


`POST /account` - creates an account with 0 balance. Returns the accountId

`GET ​/account​/{accountId}` - Returns the balance

`POST ​/account​/{accountId}​/{amount}` - deposits given amount to the given accountId

`POST ​/transfer` transfers balance from sender to receiver account
  `body - {
       	"sender_account": 1,
       	"receiver_account": 2,
       	"amount": 3000
       }`
    

For more information and data models, please open the swagger endpoint of the application 
    http://localhost:8080/swagger

# **Testing**

Unit testing has been provided for service classes.
<br/>
Resources has been tested using dropwizard-testing module with in-memory jersey server.
