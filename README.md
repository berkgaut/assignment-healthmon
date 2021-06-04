# Test Assignment -- Health Monitor for a Group of Services

## Solution Description

* Backend part only
* a Spring Boot service
* backed by MySQL for persistence
* API: dashboard data plus CRUD for services
* docker-compose to demo/play with 

## Topics for discussion

* Concurrent updates to database
* Running checks in parallel
* Scaling up - multiple health checkers
* Handling of check configuraion update
* Timeout handling
* ...

## Build & Demo

Requirements: Maven, Docker

### Build Images

```shell
mvn  --projects dummy-service,healthmon -Dmaven.test.skip spring-boot:build-image
```

### Start Demo Setup

```shell
docker-compose up
```

### Demo

#### Get list of services:

```shell
curl -X GET http://localhost:8080/v1/services
```

#### Add services

A healthy one:

```shell
echo '
{
  "name": "target-1",
  "url": "http://target-1:8080/health/200",
  "timeoutMillis": 1000
}' \
| curl -X PUT -d@- -H "Content-type: application/json" http://localhost:8080/v1/services 
```

A faulty one:

```shell
echo '
{
  "name": "target-2",
  "url": "http://target-2:8080/health/500",
  "timeoutMillis": 1000
}' \
| curl -X PUT -d@- -H "Content-type: application/json" http://localhost:8080/v1/services 
```

A slow one:

```shell
echo '
{
  "name": "target-3",
  "url": "http://target-3:8080/health/200?delayMs=2000",
  "timeoutMillis": 1000
}' \
| curl -X PUT -d@- -H "Content-type: application/json" http://localhost:8080/v1/services 
```

#### Update a service

Increase timeout on a slow one:
```shell
echo '{"timeoutMillis": 3000}' \
| curl -X POST -d@- -H "Content-type: application/json" http://localhost:8080/v1/services/3
```

----

connect to dev db

```bash
docker exec -it assignment-healthmon_healthmon-mysql_1 \
mysql --user=root --password=JxS5Y9HuI3OpY6kFf6TlMVuuGxcohp7C healthmon
```

start dev db for test

docker-compose up --detach healthmon-mysql