![image](https://user-images.githubusercontent.com/68232654/119592262-16234b80-be13-11eb-8574-b0e6cbabdd0d.png)
- 채용과정에서 비대면으로 면접을 진행하는 상황이 많아지며 비대면 채용을 위한 채용 일정, 면접 일정 생성 및 관리 플랫폼입니다.
- 엑셀 형태로 면접관과 지원자의 정보를 입력하면 면접 일정을 자동으로 생성해주고, 화상으로 면접까지 진행할 수 있습니다.
- 면접관, 지원자에게 자동으로 면접일정을 보내주며 링크 하나로 바로 면접을 진행할 수 있습니다.
- 실제 면접과 유사한 환경에서 비대면으로 면접을 진행할 수 있도록 하였습니다.

# 주요기능
## 홈 화면
![홈화면최종1](https://user-images.githubusercontent.com/68232654/108472328-f2aaab00-72cf-11eb-9108-081587c8004a.gif)

## 면접 현황 및 생성
![공고최종2](https://user-images.githubusercontent.com/68232654/108472502-2b4a8480-72d0-11eb-9661-e90583d8b6c7.gif)
![면접생성최종3](https://user-images.githubusercontent.com/68232654/108472515-31d8fc00-72d0-11eb-8eea-b9cb712fec47.gif)

## 면접 진행
### 대기방 - 지원자 목소리 체크 및 신분증 확인 진행, 확인 후 면접방으로 이동
![면접영상3](https://user-images.githubusercontent.com/68232654/108474880-4ff42b80-72d3-11eb-82bc-43361b42ee5f.gif)
### 면접방 - 면접 진행하는 방, 클릭으로 지원자의 자기소개서 확인 가능
![면접영상_면접방](https://user-images.githubusercontent.com/68232654/108475519-2a1b5680-72d4-11eb-9782-99bb8733f03a.gif)


# 기획
## 스토리보드
![image](https://user-images.githubusercontent.com/68232654/119592612-cbee9a00-be13-11eb-83e9-7aa38d188a94.png)
## ERD
![DB](https://user-images.githubusercontent.com/68232654/108472875-a449dc00-72d0-11eb-8d39-7425c00f4e37.png) 
![image](https://user-images.githubusercontent.com/68232654/108472915-b0ce3480-72d0-11eb-95b1-114532c1abf5.png)

## 기술 스택
![기술스택](https://user-images.githubusercontent.com/68232654/108472650-5fbe4080-72d0-11eb-91f4-234a851ec761.png)

# 개발환경
## FrontEnd
```
- node v14.15.4
- npm 6.14.10
- yarn 1.22.5
- @vue/cli 4.5.10
- VScode Extensions
    - GitLab Workflow - GitLab
    - Vetur - Pine Wu
    - Vue 3 Snippets - hollowtree
    - HTML Snippets - Mohamed Abusaid
    - Live Server - Ritwick Dey
    - Visual Studio IntelliCode - Microsoft
```
## Bacnd
```
- Java jdk 13.0.1
    - [http://jdk.java.net/archive/](http://jdk.java.net/archive/)
- Apache Maven 3.6.3
    - [http://maven.apache.org/download.cgi](http://maven.apache.org/download.cgi) → apache-maven-3.6.3-bin.zip
- mySQL 8.0.22
- full distribution on Eclipse 4.15
    - [https://github.com/spring-projects/toolsuite-distribution/wiki/Spring-Tool-Suite-3](https://github.com/spring-projects/toolsuite-distribution/wiki/Spring-Tool-Suite-3)
    - STS 3.9.14.RELEASE
    - 디펜던시 : Spring Boot DevTools, Lombok, MyBatis Framework, MySQL Driver, Spring Web

- WebRTC
    - openVidu [https://openvidu.io/](https://openvidu.io/)
```

# 만든사람들

```
FrontEnd : 김민정, 이준희
BackEnd  : 권영일, 김윤지, 정일규
```
