# twitter-clone-api-server
트위터 클론코딩 토이 프로젝트 API Server
클라이언트 : https://github.com/swj9707/twitter-clone-client  
어드민 : https://github.com/swj9707/twitter-clone-admin 

# Tech Stacks
- Server
  - Spring Boot 3
  - Kotlin (OpenJDK 17)
- DB
  - MariaDB
  
# application.yml 양식
```yml
spring:
  application:
    name: twitter-clone-api-server
  datasource:
    url:
    username:
    password:
    hikari:
      maximum-pool-size: 
  jpa:
    hibernate:
      ddl-auto: validate
    database-platform: org.hibernate.dialect.MariaDB103Dialect
    properties:
      hibernate:
        default_batch_fetch_size: 1000

logging:
  level:
    org.springframework.r2dbc.core.DefaultDatabaseClient: debug

```
