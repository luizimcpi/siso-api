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

## Requests

### Login
```
curl --location --request POST 'localhost:9001/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "teste@gmail.com",
    "password": "Mudar@123"
}'
```

### Create User
```
curl --location --request POST 'localhost:9001/users' \
--header 'Authorization: Bearer {admin_token}' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "teste@gmail.com",
    "password": "Mudar@123"
}'
```

### Refresh Token
```
curl --location --request POST 'localhost:9001/oauth/access_token' \
--header 'Content-Type: application/json' \
--data-raw '{
    "grant_type": "refresh_token",
    "refresh_token": "eyJhbGciOiJIUzI1NiJ9.YTY3MWUzNTktMWYxZC00ZTBkLWE1OWEtZWNmNmNkMTk0MDQz.0sdzFhdfTQfjzhk3rl4pnFxrrN_h5Wnsaek6oLiDruU"
}'
```

### Create Customer
```
curl --location --request POST 'localhost:9001/customers' \
--header 'Authorization: Bearer {user_token}' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "customer teste",
    "document": "44444411111",
    "email": "customer@gmail.com",
    "birthDate": "1990-03-03"
}'
```

### Find Customer By Id
```
curl --location --request GET 'localhost:9001/customers/1' \
--header 'Authorization: Bearer {user_token}'
```

### Delete Customer By Id
```
curl --location --request DELETE 'localhost:9001/customers/1' \
--header 'Authorization: Bearer {user_token}'
```

### Update Customer By Id
```
curl --location --request PUT 'localhost:9001/customers/1' \
--header 'Authorization: Bearer {user_token}' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "customer teste Alterado ",
    "document": "11111111111 alterado",
    "email": "customer-alterado@gmail.com",
    "birthDate": "2019-01-14"
}'
```

### Find all Customers By UserId
```
curl --location --request GET 'localhost:9001/customers?size=20&page=0' \
--header 'Authorization: Bearer {user_token}'
```

### Search By Customer Name
```
curl --location --request GET 'localhost:9001/customers/search?name=Maria' \
--header 'Authorization: Bearer {user_token}'
```