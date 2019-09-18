# Aequilibrium Transformers Assignment
Java backend interview assignment for Aequilibrium.

## Dependencies
* Maven
* Spring
* Java 8
* DynamoDB

# Setup
## Database Setup
1. Download [DynamoDB Local Version](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBLocal.DownloadingAndRunning.html)
2. Run the DynamoDB JAR file on port 8001 by executing the following command
```java -Djava.library.path=./DynamoDBLocal_lib -jar DynamoDBLocal.jar -sharedDb -port 8001```

## Server Setup
1. Ensure DynamoDB local is running on port 8001
2. In the root directory, run `mvn spring-boot:run`
3. Above the Spring logo ensure you see the message "Successfully created table" or "Table is already created" to ensure you have connected to DynamoDB properly

# Calling the API
I used Postman to call the API but CURL or any program/method of calling RESTful applications will do.

## API Routes
### POST localhost:8080/create-transformer
Example request body:
```
{
	"name": "Bumblebee",
	"strength": 3,
	"intelligence": 9,
	"speed": 5,
	"endurance": 3,
	"rank": 6,
	"courage": 4,
	"firepower": 5,
	"skill": 9,
	"allegiance": "A"
}
```
### GET localhost:8080/list-transformers
### PUT localhost:8080/update-transformer
Example request body:
```
{
   "id": "0643ef06-b3b2-4065-aa81-4f1cdb5856d9",
   "endurance": 9,
   "allegiance": "D"
}
```
### DEL localhost:8080/delete-transformer-by-id?id=*{transformer_id}*
### POST localhost:8080/battle
Example request body:
```
{
    "transformerIds": ["bea70c1a-9476-4b1d-916f-5e831d83b26f", "0643ef06-b3b2-4065-aa81-4f1cdb5856d9", "ae5af7e1-6c55-4ec7-887a-04b8acea2527", "aa97cc90-c714-4629-8752-3570a4f61882"]
}
```

# Running Tests
1. Ensure DynamoDB local is running on port 8001
2. In the root directory, run `mvn test`