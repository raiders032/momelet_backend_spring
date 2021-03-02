## Momelet
>  Momelet은 다수와 식당 및 메뉴를 고민할 때 1분 30초 이내에 식당 및 메뉴 결정을 도와주는 서비스입니다.
>
>  시연 영상: https://youtu.be/jpG9aGxycZ4



## 환경설정

* 프로파일별로 환경설정을 달리 합니다.

* 프로파일에는 `local`, `dev`, `prod` 가 있습니다.

  * `local` : local 개발환경
  * `dev` : 서버 개발환경
  * `prod` : 운영 환경

* `env/spring.env` 로 스프링 서버를 컨테이너로 실행시 프로파일을 지정할 수 있습니다.

  * ```
    # 프로파일을 dev로 설정
    spring.profiles.active=dev
    ```



### 데이터베이스 환경설정

* Mysql 데이터베이스를 사용합니다.

* `local` 환경에서 사용할 데이터베이스를 설정합니다.

  * `src/main/resources/application-local-db.properties`

  * ```properties
    spring.datasource.url=jdbc:mysql://<mysql 서버 엔드포인트>
    spring.datasource.username=<유저 ID>
    spring.datasource.password=<password>
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    
    spring.jpa.hibernate.ddl-auto = validate
    spring.jpa.database-platform = com.swm.sprint1.config.MysqlCustomDialect
    spring.jpa.properties.hibernate.format_sql=true
    spring.jpa.properties.hibernate.default_batch_fetch_size=500
    logging.level.org.hibernate.SQL=DEBUG
    logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
    ```

* 마찬가지로 `dev` , `prod` 환경의 데이터베이스 환경설정은 `src/main/resources/application-<운영환경>-db.properties` 를 위와 같이 만들어 줍니다.



### Oauth2.0 클라이언트 환경설정

* `src/main/resources/application-oauth.properties` 로 환경을 설정합니다.
* 각각의 프로파이더의 콘솔에서 앱을 먼저 등록하고 `client id` 와 `secret` 을 받아 아래에 입력해주세요

```properties
# google client
spring.security.oauth2.client.registration.google.client-id=<client id>
spring.security.oauth2.client.registration.google.client-secret=<secret>
spring.security.oauth2.client.registration.google.scope =profile,email
spring.security.oauth2.client.registration.google.redirect-uri={baseUrl}/oauth2/callback/{registrationId}

# naver client
spring.security.oauth2.client.registration.naver.client-id=<client id>
spring.security.oauth2.client.registration.naver.client-secret=<secret>
spring.security.oauth2.client.registration.naver.redirect-uri={baseUrl}/oauth2/callback/{registrationId}
spring.security.oauth2.client.registration.naver.authorization-grant-type=authorization_code
spring.security.oauth2.client.registration.naver.scope=name,profile_image
spring.security.oauth2.client.registration.naver.client-name=Naver
spring.security.oauth2.client.provider.naver.authorization_uri=https://nid.naver.com/oauth2.0/authorize
spring.security.oauth2.client.provider.naver.token-uri=https://nid.naver.com/oauth2.0/token
spring.security.oauth2.client.provider.naver.user-info-uri=https://openapi.naver.com/v1/nid/me
spring.security.oauth2.client.provider.naver.user-name-attribute=response

# kakao client
spring.security.oauth2.client.registration.kakao.client-id=<client id>
spring.security.oauth2.client.registration.kakao.client-secret=<secret>
spring.security.oauth2.client.registration.kakao.redirect-uri={baseUrl}/oauth2/callback/{registrationId}
spring.security.oauth2.client.registration.kakao.authorization-grant-type=authorization_code
spring.security.oauth2.client.registration.kakao.scope=profile,account_email
spring.security.oauth2.client.registration.kakao.client-name=kakao
spring.security.oauth2.client.registration.kakao.client-authentication-method= POST
spring.security.oauth2.client.provider.kakao.user-name-attribute=id
spring.security.oauth2.client.provider.kakao.authorization_uri=https://kauth.kakao.com/oauth/authorize
spring.security.oauth2.client.provider.kakao.token-uri=https://kauth.kakao.com/oauth/token
spring.security.oauth2.client.provider.kakao.user-info-uri=https://kapi.kakao.com/v2/user/me
```

### JWT token 환경설정

* `src/main/resources/application-jwt.properties`
* 시크릿키를 지정하고 토큰의 만료기한을 지정해주세요

```properties
app.auth.tokenSecret = <시크릿>
# 리프레시 토큰 만료기한
app.auth.refreshTokenExpirationMsec= 864000000
# JWT token 만료기한
app.auth.accessTokenExpirationMsec = 3600000
```



## 실행

>  실행에 앞서 `docker` 와 `docker-compose`가 설치되어 있어야 합니다.

```bash
# 실행
docker-compose up --build -d
# 종료
docker-compose down
```



## OAuth 2.0

* 구글, 네이버, 카카오 소셜 로그인을 지원합니다.
* [수행과정 보기](https://github.com/raiders032/md/blob/master/Spring/security/Social-login/social-login.md)



## 모니터링

* Spring Actuator, Prometheus, Grafana을 이용해 모니터링 시스템을 구축합니다.
* [수행과정 보기](https://github.com/raiders032/md/blob/master/Spring/actuator/actuator.md)



## CICD

* Git Hub, Jenkins, AWS CodeDeploy를 이용해 CICD 파이프 라인을 구축합니다. 
* [수행과정 보기](https://github.com/raiders032/md/blob/master/CICD/Jenkins/Jenkins.md)