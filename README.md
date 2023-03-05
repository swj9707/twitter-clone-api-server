# twitter-clone-api-server

<img src="https://user-images.githubusercontent.com/36991763/208076544-576595e4-f601-4ba4-9ba5-719a68dc84fb.png" width="230" height="200"/>

트위터 클론코딩 토이 프로젝트 API Server  
클라이언트 : https://github.com/swj9707/twitter-clone-client  
어드민 : https://github.com/swj9707/twitter-clone-admin 

위키 : https://github.com/swj9707/twitter-clone-api-server/wiki

SNS 서비스의 백엔드 설계 및 개발 공부 목적의 프로젝트입니다. 

현재 v1.0.0 가 프리 릴리즈 되었습니다. 안정화 후 최종 배포 예정입니다. 

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
- JWT 기반 인증
- 트윗 생성 및 삭제
- 좋아요, 리트윗
- 댓글 
- 프로필 조회 및 수정
- 자신이 작성한 트윗, 리트윗, 댓글 및 좋아요 조회


# Demo
## 로그인 화면
![image](https://user-images.githubusercontent.com/36991763/222965661-d202174a-ec3b-4542-888c-adb6bd42b108.png)
## 메인 화면
![image](https://user-images.githubusercontent.com/36991763/222965721-a2dd0193-1ee8-4d48-9ba2-1e26802d0bd2.png)
## 트윗 생성
![image](https://user-images.githubusercontent.com/36991763/222965729-34634dd8-f19e-4f77-b17c-b32613f65851.png)
## 프로필 화면
![image](https://user-images.githubusercontent.com/36991763/222965765-5e42b8b2-c5b2-4591-bba3-a7fdd8845024.png)

# How to use?
## 로컬에 셋업하는 법
- Web Client 를 다운받는다.
  - https://github.com/swj9707/twitter-clone-client
- Docker compose 를 통해 인프라 셋업을 한다.
  ```yaml
  version: "3.8"
  services:
    cdn-server:
      container_name: twitter-clone-cdn-server
      image: nginx:latest
      restart: always
      ports:
        - 8082:8082
      volumes:
        - ./cdn/nginx.conf:/etc/nginx/nginx.conf
        - ./cdn/data:/static_files
      environment:
        - TZ=Asia/Seoul

    mysql:
      container_name: twitter-clone-db
      image: mariadb:latest
      volumes:
        - ./db/conf.d:/etc/mysql/conf.d
        - ./db/data:/var/lib/mysql
        - ./db/initdb.d:/docker-entrypoint-initdb.d
      ports:
        - 3306:3306
      env_file: .env
      command:
        - "--character-set-server=utf8mb4"
        - "--collation-server=utf8mb4_unicode_ci"

    redis:
      image: redis:alpine
      command: redis-server --port 6379
      container_name: twitter-cloud-redis
      hostname: twitter-clone-redis
      labels:
        - "name=redis"
        - "mode=standalone"
      ports:
        - 6379:6379
  ```
  로컬에서 돌릴 땐 위의 셋업으로 진행합니다. 
  nginx.conf는 아래와 같습니다. 
  ```conf
  events {}
  http {
      server {
          listen 8082;
          root /static_files;

          location /files/ {
              add_header Content-disposition 'attachment; filename="$1"';
          }

          location / {
              index index.html index.htm;
          }
      }
  }
  ```
- API 서버 내 Application.yml 설정을 해 준 후 DB 설정을 해 준다.
  ```yaml
  spring:
    datasource:
      driver-class-name: org.mariadb.jdbc.Driver
      url: jdbc:mariadb://localhost:3306/twitter_clone_db_dev
      username: {root username}
      password: {root password}
    jpa:
      hibernate:
        ddl-auto: validate
    data:
      redis:
        host: localhost
        port: {redis port}
    output:
      ansi:
        enabled: always
  jwt:
    security:
      key: {secret key}

  file:
    ImageLocation: {image location}
    cdn:
      tweetImage: {cdn url}

  spring-doc:
    api-docs:
      path: /docs

    swagger-ui:
      path: /swagger-ui
      display-request-duration: true

    cache:
      disabled: true
  ```
