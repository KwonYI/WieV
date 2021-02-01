[![License badge](https://img.shields.io/badge/license-Apache2-orange.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![Documentation Status](https://readthedocs.org/projects/openviduio-docs/badge/?version=stable)](https://docs.openvidu.io/en/stable/?badge=stable)
[![Docker badge](https://img.shields.io/docker/pulls/openvidu/openvidu-server-kms.svg)](https://hub.docker.com/r/openvidu/openvidu-server-kms)
[![Support badge](https://img.shields.io/badge/support-sof-yellowgreen.svg)](https://groups.google.com/forum/#!forum/openvidu)

[![][OpenViduLogo]](http://openvidu.io)

openvidu-mvc-java
===

Visit [docs.openvidu.io/en/stable/tutorials/openvidu-mvc-java/](http://docs.openvidu.io/en/stable/tutorials/openvidu-mvc-java/)

[OpenViduLogo]: https://secure.gravatar.com/avatar/5daba1d43042f2e4e85849733c8e5702?s=120



### 실행방법
1. 도커 실행
2. cmd에 docker run -p 4443:4443 --rm -e OPENVIDU_SECRET=MY_SECRET openvidu/openvidu-server-kms:2.16.0 입력<br>
참고 [ https://docs.openvidu.io/en/2.16.0/tutorials/openvidu-mvc-java/ ]
3. 스프링부트 실행
4. localhost:5000 으로 이동

### 내용 추가 정리
#### 서버쪽(Springboot - Sessioncontroller )
1. 방세션 생성(   openVidu.createSession()   )
2. 참가자 연결 객체 생성(ConnectionProperties)
3. 방세션에 참가자연결객체를 연결하고 토큰값 가져와<br>
    - 토큰에는 방세션아이디와 참가자토큰아이디 등 정보가 있다
4. 클라이언트에겐 토큰값만 보낸다

#### 클라이언트쪽(js - session.html )
1. 참가자 세션생성(OpenVidu.initSession) -> 참가자들간의 연결을 이어주기위해서
2. 토큰을 이용해 세션을 연결해준다(connect) = 연결객체와 내 세션을 연결해줘
    connect안에서 비디오 설정하고 publish


### todo
1. 대기관리자가 컨트롤러에서 만든 세션(Session 객체) 가지고있게 하기
2. 대기관리가가 방 터트리기 가능하게
3. 채팅, 녹화, 개개인의 화면 켜고 끄기 기능은 일단 보류