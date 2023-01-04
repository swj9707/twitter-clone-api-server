# twitter-clone-api-server

<img src="https://user-images.githubusercontent.com/36991763/208076544-576595e4-f601-4ba4-9ba5-719a68dc84fb.png" width="230" height="200"/>

트위터 클론코딩 토이 프로젝트 API Server  
클라이언트 : https://github.com/swj9707/twitter-clone-client  
어드민 : https://github.com/swj9707/twitter-clone-admin 

SNS 서비스의 백엔드 설계 및 개발 공부 목적의 프로젝트입니다. 

# Tech Stacks
<div>
  <row>
    <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=flat-square&logo=Kotlin&logoColor=white"/>
    <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat-square&logo=Spring Boot&logoColor=white"/>
    <img src="https://img.shields.io/badge/MariaDB-003545?style=flat-square&logo=MariaDB&logoColor=white"/>
    <img src="https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=Redis&logoColor=white"/>
  </row>
</div>

# Features
- 인증
  - JWT 방식
- 서비스 API 제공
  - 트윗 생성, 수정, 열람, 삭제
  - 유저 정보 조회 및 수정

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
