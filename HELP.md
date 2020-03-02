# Documentation

# Start on your local environment
Execute this following command line :
``` docker-compose up```

This docker compose will start the following services: 
- Database SQL
- Mailhog (SMTP)

Then execute the following command to start up your backend application with default configurations :
```mvn spring-boot:run```

If you want to override configuration create your own application.yml,
then execute the following command line to start up you backend application with you own configuration:

```mvn spring-boot:run -Dspring.config.location=${filePath}```

### Configuration file .yml
```
# Database SQL configuration
spring.datasource:
  url: database-uri
  username: database-username
  password: database-password

# SMTP configuration
spring.mail:
  host: smtp-host
  port: smtp-port
  username: smtp-usernam
  password: smtp-password
```

# Releases
### Release 1.0.0

Features:
- User : Create, Update, Delete, Get
- User : Credit a user
- User : Search by Firstname, lastname or nickname
- User : When you create a user you can create a linked account
- Account : Create, update an account
- Ingredient : Create, Update, Delete, Get and Get with type unique filter
- Order : Create, Get
- Item of an order: Update
- Product : Create, Update, Delete, Get

# Documentations
### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.1.RELEASE/maven-plugin/)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.2.1.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)
* [Spring Security](https://docs.spring.io/spring-boot/docs/2.2.1.RELEASE/reference/htmlsingle/#boot-features-security)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.2.1.RELEASE/reference/htmlsingle/#boot-features-jpa-and-spring-data)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

