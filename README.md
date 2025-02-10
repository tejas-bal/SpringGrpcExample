# Spring gRPC Example

This is a simple example demonstrating how to use gRPC with Spring Boot to calculate age based on a given date of birth.

# Features

- Implements a simple Spring gRPC service using Protocol Buffers.
- Calculates age based on the provided day, month and year.
- [Spring GRPC](https://docs.spring.io/spring-grpc/reference/index.html)

# Prerequisites

Ensure you have the following installed:

- Java 21
- Gradle

# Getting Started

Clone the Repository
```
https://github.com/tejas-bal/SpringGrpcExample.git
cd SpringGrpcExample
```

Build the Project

```./gradlew build```

Running the Application

```./gradlew bootRun```

Testing the gRPC Service

Install gRPC client: 
```
brew install grpcurl
```

Request:
```
grpcurl -d '{"day":12, "month":12, "year": 1987}' -plaintext localhost:9090 AgeService.CalculateAge
```

Response:
```
{
  "message": 37
}
```