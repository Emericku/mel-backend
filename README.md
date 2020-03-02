# Melusine Backend
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

# Start application in production 

```java -jar app.jar --spring.config.location=file:///Users/home/config/jdbc.properties```

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