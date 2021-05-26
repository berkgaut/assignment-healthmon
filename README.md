# Test Assignment -- Health Monitor for a Group of Services

## Cases to discuss

* parallel execution of checks
* Handling of service URL update while teh check is in progress 

## How to run

start dev db

```bash
docker run \
--name healthmon-mysql \
--detach \
-p 3306:3306 \
-e MYSQL_DATABASE=healthmon \
-e MYSQL_ROOT_PASSWORD=riP8BEglH3REwOugqxHoeuaYuEQCvjO8 \
mysql:8.0
```

connect to dev db

```bash
docker exec -it healthmon-mysql \
mysql --user=root --password=riP8BEglH3REwOugqxHoeuaYuEQCvjO8 healthmon
```