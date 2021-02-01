## Backend 개발 환경

- Java jdk 13.0.1
    - [http://jdk.java.net/archive/](http://jdk.java.net/archive/)
- Apache Maven 3.6.3
    - [http://maven.apache.org/download.cgi](http://maven.apache.org/download.cgi) → apache-maven-3.6.3-bin.zip
- mySQL 8.0.22
- full distribution on Eclipse 4.15
    - [https://github.com/spring-projects/toolsuite-distribution/wiki/Spring-Tool-Suite-3](https://github.com/spring-projects/toolsuite-distribution/wiki/Spring-Tool-Suite-3)
    - STS 3.9.14.RELEASE
    - 디펜던시 : Spring Boot DevTools, Lombok, MyBatis Framework, MySQL Driver, Spring Web


## DB 설계

- E-R 다이어그램 생성 완료

![E-R다이어그램_생성](/uploads/220c550b4110689c0e9bfc15e48069c0/E-R다이어그램_생성.png)


## DB테이블 설명

1. 회사 관련 테이블

- 회사 : 회사의 정보를 가지는 테이블
- 인사팀 : 각 회사의 인사팀에서 회원가입을 통해 등록하는 정보
- 부서 : 각 회사의 부서명 ex ) 인사팀, 개발팀,...
- 직군 : 각 부서의 담당 업무 ex ) 마케팅, SW 개발, 기획...
- 공고 : 각 회사에서 나오는 공고! - 여러 직군 포함

2. 그룹 관련 테이블

- 그룹 : 하나의 그룹은 어떤 공고, 직군에 해당하는 면접 그룹
=> 와이어프레임 36p의 한 row
=> 공고, 직군당 여러 그룹이 생성될 수 있음
- 세부그룹별 순서 : 각 그룹별로 어떤 순서로 진행될지 담고있는 테이블
ex) 직무 -> PT -> 토론

3. 면접 관련 테이블

- 면접 종류 : 해당 면접이 어떤 면접인지에 대한 테이블 
ex ) 직무, pt, 토론
=> 그룹에서 연결될 때는 하나의 면접에 
여러 종류의 면접이 들어갈 수 있기 때문!
- 그룹별 면접 종류 
ex ) sw개발에는 PT, 토론, 마케팅에는 인성, 직무
대기관- 면접관 중 view_wait 컬럼이
 true이면 대기관

- 면접 종료 확인 : 각 지원자 별로 해당되는 
순서의 면접이 끝났는지 확인
ex) 김싸피는 PT(true), 토론(false), 인성(false)
=> 지원자 한명당 하나씩이기 때문에 1:1?

## 서버에 DB 구축

![sql_picture](/uploads/d7fa0f55371e571624b5a2dd828ebba3/sql_picture.png)