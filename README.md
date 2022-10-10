# SISO-API
## Micronaut 3.7.1 Documentation

- [User Guide](https://docs.micronaut.io/3.7.1/guide/index.html)
- [API Reference](https://docs.micronaut.io/3.7.1/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/3.7.1/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)
---

- [Shadow Gradle Plugin](https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow)
## Feature hibernate-jpa documentation

- [Micronaut Hibernate JPA documentation](https://micronaut-projects.github.io/micronaut-sql/latest/guide/index.html#hibernate)


## Feature security documentation

- [Micronaut Security documentation](https://micronaut-projects.github.io/micronaut-security/latest/guide/index.html)


## Feature jdbc-hikari documentation

- [Micronaut Hikari JDBC Connection Pool documentation](https://micronaut-projects.github.io/micronaut-sql/latest/guide/index.html#jdbc)


## Feature http-client documentation

- [Micronaut HTTP Client documentation](https://docs.micronaut.io/latest/guide/index.html#httpClient)


## Feature testcontainers documentation

- [https://www.testcontainers.org/](https://www.testcontainers.org/)


## Feature test-resources documentation

- [Micronaut Test Resources documentation](https://micronaut-projects.github.io/micronaut-test-resources/latest/guide/)


## Feature security-jwt documentation

- [Micronaut Security JWT documentation](https://micronaut-projects.github.io/micronaut-security/latest/guide/index.html)


## Feature flyway documentation

- [Micronaut Flyway Database Migration documentation](https://micronaut-projects.github.io/micronaut-flyway/latest/guide/index.html)

- [https://flywaydb.org/](https://flywaydb.org/)


## RUN THIS APP
```
docker-compose up
```

## Elasticsearch
```
localhost:9200
user: elastic
pass: changeme
```

## Requests

### Create User
```
curl --location --request POST 'localhost:8080/users' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0ZUBnbWFpbC5jb20iLCJuYmYiOjE2NjUwOTQ5NjgsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJpc3MiOiJzaXNvIiwiZXhwIjoxNjY1MDk4NTY4LCJpYXQiOjE2NjUwOTQ5Njh9.i1DBIIFOMbVqk9vVCSoh6a9K3oZvJv8NV54yP31GfHQ' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "teste@gmail.com",
    "password": "Mudar@123"
}'
```

### Login
```
curl --location --request POST 'localhost:8080/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "teste@gmail.com",
    "password": "Mudar@123"
}'
```
