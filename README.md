# Book Store Assignment for Chiwa Kantawong

This demo application is build by spring-boot and using in-memory DB(H2)  that provide the BookStore API that support
https .

## Sequence Diagram

### Login
<img src="images/login.png"/>

### Get user
<img src="images/getuser.png"/>

### Create new user
<img src="images/newuser.png"/>

### Delete user
<img src="images/deleteuser.png"/>

### List books
<img src="images/listbook.png"/>

### Order book
<img src="images/orderbook.png"/>

## How to run the application?

Please checkout the project from https://github.com/peasunseries/book-store.git

~~~
git clone https://github.com/peasunseries/book-store.git
cd book-store
mvn clean install
mvn spring-boot:run

~~~

** If mvn clean install , The Unit Test Failed in some case please do the

~~~
 mvn clean install -DskipTests
~~~
 
The application should be start and you will see some log as below.

~~~
INFO] Nothing to compile - all classes are up to date
[INFO] 
[INFO] <<< spring-boot-maven-plugin:2.1.3.RELEASE:run (default-cli) < test-compile @ book-store <<<
[INFO] 
[INFO] 
[INFO] --- spring-boot-maven-plugin:2.1.3.RELEASE:run (default-cli) @ book-store ---

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.1.3.RELEASE)

2019-05-28 11:36:18.913  INFO 21378 --- [           main] com.scb.bookstore.BookStoreApplication   : Starting BookStoreApplication on Chiwas-MacBook-Pro.local with PID 21378 (/Users/chiwa.kantawong/Projects/book-store/target/classes started by chiwa.kantawong in /Users/chiwa.kantawong/Projects/book-store)
2019-05-28 11:36:18.916  INFO 21378 --- [           main] com.scb.bookstore.BookStoreApplication   : No active profile set, falling back to default profiles: default
2019-05-28 11:36:20.249  INFO 21378 --- [         
.....
.....
2019-05-28 11:36:26.769  INFO 21378 --- [           main] s.d.s.w.s.ApiListingReferenceScanner     : Scanning for api listing references
2019-05-28 11:36:27.498  INFO 21378 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8443 (https) with context path ''
2019-05-28 11:36:27.501  INFO 21378 --- [           main] com.scb.bookstore.BookStoreApplication   : Started BookStoreApplication in 9.003 seconds (JVM running for 12.42)

~~~

I also provide the API docs via swagger, you can access to 

- https://localhost:8443/swagger-ui.html#/

And also provide the Actuator

- https://localhost:8443/actuator/health
- https://localhost:8443/actuator/info
- https://localhost:8443/actuator/metrics

 /health – Shows application health information 
 
 /info – Displays arbitrary application info; not sensitive by default
 
 /metrics – Shows ‘metrics’ information for the current application



When the application starting some dummy users are created you can access all APIs as below

### Login API

~~~
curl --insecure -X POST \
  https://localhost:8443/login \
  -H 'content-type: application/json' \
  -d '{
	"username":"chiwa",
	"password":"password"
}'
~~~

response : 

~~~
{
    "user_name":"chiwa",
    "token":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjaGl3YSIsImlzcyI6Imh0dHA6Ly93d3cuc2NiLmNvbSIsImlhdCI6MTU1OTAxODcxNSwiZXhwIjoxNTU5MDM2NzE1fQ.PZ8BM_yH6S1JvkTe8zzylMI23T9fFC5YZ58xSv_atsM"
}
~~~

The system will return you the token for using in the other secure API.

### Get User
Get the logged in user information

~~~
curl --insecure -X GET \
  https://localhost:8443/users \
  -H 'authorization: SCB eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjaGl3YSIsImlzcyI6Imh0dHA6Ly93d3cuc2NiLmNvbSIsImlhdCI6MTU1OTAxODcxNSwiZXhwIjoxNTU5MDM2NzE1fQ.PZ8BM_yH6S1JvkTe8zzylMI23T9fFC5YZ58xSv_atsM'
~~~

response :

~~~
{
  "username":"chiwa",
  "first_name":"Chiwa",
  "last_name":"Kantawong",
  "date_of_birth":"15/01/1978",
  "books":[1,2,3]
}
~~~

## Create User
Create new user

~~~
curl --insecure -X POST \
  https://localhost:8443/users \
  -H 'authorization: SCB eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjaGl3YSIsImlzcyI6Imh0dHA6Ly93d3cuc2NiLmNvbSIsImlhdCI6MTU1OTAxODcxNSwiZXhwIjoxNTU5MDM2NzE1fQ.PZ8BM_yH6S1JvkTe8zzylMI23T9fFC5YZ58xSv_atsM' \
  -H 'content-type: application/json' \
  -d '{
	"username":"test",
	"password":"test",
	"first_name" : "test",
	"last_name" : "test",
	"date_of_birth" : "10/01/1977"
}'
~~~

Response :

~~~
{
    "username":"test",
    "first_name":"test",
    "last_name":"test",
    "date_of_birth":"10/01/1977",
    "books":[]
}
~~~

## Delete User
Delete logged in user and his order history

~~~
curl --insecure -X DELETE \
  https://localhost:8443/users \
  -H 'authorization: SCB eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjaGl3YSIsImlzcyI6Imh0dHA6Ly93d3cuc2NiLmNvbSIsImlhdCI6MTU1OTAxODcxNSwiZXhwIjoxNTU5MDM2NzE1fQ.PZ8BM_yH6S1JvkTe8zzylMI23T9fFC5YZ58xSv_atsM'
~~~

Response : 200

## GET Books
This API does not need the authorization it's return the list of books.

~~~
curl --insecure -X GET https://localhost:8443/books
~~~

Response :

~~~
[
  {
    "id": 1,
    "book_name": "Before We Were Yours: A Novel",
    "author_name": "Lisa Wingate",
    "price": 340,
    "is_recommended": false
  },
  {
    "id": 2,
    "book_name": "When Never Comes",
    "author_name": "Barbara Davis",
    "price": 179,
    "is_recommended": false
  },
  {
    "id": 3,
    "book_name": "Giraffes Can't Dance",
    "author_name": "Giles Andreae, Guy Parker-Rees",
    "price": 200.5,
    "is_recommended": false
  },
  {
    "id": 4,
    "book_name": "The Great Alone: A Novel Kristin Hannah",
    "author_name": "Kristin Hannah",
    "price": 495,
    "is_recommended": true
  },
  {
    "id": 5,
    "book_name": "An American Princess: The Many Lives of Allene Tew",
    "author_name": "Annejet van der Zijl, Michele Hutchison",
    "price": 149,
    "is_recommended": true
  },
  {
    "id": 6,
    "book_name": "Medical Medium Life-Changing Foods",
    "author_name": "Anthony William",
    "price": 929.7,
    "is_recommended": false
  },
  {
    "id": 7,
    "book_name": "Vegan 100",
    "author_name": "Gaz Oakley",
    "price": 897.96,
    "is_recommended": false
  },
  {
    "id": 8,
    "book_name": "Have You Filled A Bucket Today?",
    "author_name": "Carol McCloud",
    "price": 290.06,
    "is_recommended": false
  },
  {
    "id": 9,
    "book_name": "The Very Hungry Caterpillar",
    "author_name": "Eric Carle",
    "price": 208.51,
    "is_recommended": false
  },
  {
    "id": 10,
    "book_name": "The Hate U Give",
    "author_name": "Angie Thomas",
    "price": 319.16,
    "is_recommended": false
  },
  {
    "id": 11,
    "book_name": "The Alice Network",
    "author_name": "Kate Quinn",
    "price": 393.22,
    "is_recommended": false
  },
  {
    "id": 12,
    "book_name": "Harry Potter - A History of Magic",
    "author_name": "British Library",
    "price": 1379.78,
    "is_recommended": false
  }
]
~~~
 
 ## Order books 
 Order books for logged in user and store order information in DB
 
 ~~~
 curl --insecure -X POST \
   https://localhost:8443/users/orders \
   -H 'authorization: SCB eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjaGl3YSIsImlzcyI6Imh0dHA6Ly93d3cuc2NiLmNvbSIsImlhdCI6MTU1OTAxODcxNSwiZXhwIjoxNTU5MDM2NzE1fQ.PZ8BM_yH6S1JvkTe8zzylMI23T9fFC5YZ58xSv_atsM' \
   -H 'content-type: application/json' \
   -d '{ "orders": [6, 7] }'
 ~~~
 
 Response :
 
 ~~~
 { "price":1827.66 }
 ~~~
 
 if you send none existed book id, the system will rejected the order as below
 
 ~~~
  curl --insecure -X POST \
    https://localhost:8443/users/orders \
    -H 'authorization: SCB eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjaGl3YSIsImlzcyI6Imh0dHA6Ly93d3cuc2NiLmNvbSIsImlhdCI6MTU1OTAxODcxNSwiZXhwIjoxNTU5MDM2NzE1fQ.PZ8BM_yH6S1JvkTe8zzylMI23T9fFC5YZ58xSv_atsM' \
    -H 'content-type: application/json' \
    -d '{ "orders": [6, 99] }'
  ~~~
  
  Response :
  
  ~~~
  {
    "status_code":500,
    "error_message":"99 is not found.",
    "developer_message":"99 is not found."
  }
  ~~~
  
  #Good luck guy :) 