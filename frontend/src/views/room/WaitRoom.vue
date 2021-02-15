<template>
  <div id="waitroom">
    <!--상단 바-->
    <v-app-bar class="d-none" dense dark>
      <v-toolbar-title class="">
        {{ comName }}
        {{ re_year }}
        {{ re_flag }}
        {{ re_status }}

      </v-toolbar-title>
    </v-app-bar>
    <!-- 
    화면 해상도
    <div class="brd" style="width: 400px; height: 225px"></div>
    <div class="brd" style="width: 480px; height: 270px"></div>
    <div class="brd" style="width: 640px; height: 360px"></div>
    <div class="brd" style="width: 960px; height: 540px"></div> -->

    <v-container class="room">
      <v-row style="height: 100%">
        <v-col cols="9" class="main-box">
          <!-- 메인 상단 - 배너, 면접실 이동 안내 -->
          <v-row class="main-banner">
            <!-- 공지 배너 -->
            <v-col cols="9" class="banner">
              <v-text-field solo hide-details height="100%" :readonly='isViewee' class="notice" v-model="banner_message"
                @keyup.13="noticeBanner"></v-text-field>
            </v-col>

            <!-- 면접실 이동 안내 -->
            <v-col cols="3">
              <v-card v-if="!isViewee" color="#304B61" elevation="2" style="height: 100%" dark>
                <v-card-title class="justify-center py-2">
                  <div class="text-center text-subtitle-1">면접실 이동</div>
                  <input class="btn" type="button" v-if="moving_viewee.length" @click="sendSignal" value="면접실 보내기" />
                </v-card-title>
                <v-card-text class="d-flex justify-center pa-0 text-white">
                  <!-- 면접관 -->
                  <a-dropdown v-if="!isViewee" v-model="visible" class="centering" style="width: 90%">
                    <a class="ant-dropdown-link" @click="e => e.preventDefault()">
                      <span v-for="(viewee, i) in moving_viewee" :key="i" class="pr-1">
                        {{ viewee }} &nbsp;
                      </span>
                      <span v-if="!moving_viewee.length" class="px-1">
                        면접실 이동자 선택하기
                      </span>
                      &nbsp;
                      <a-icon type="down" />
                    </a>

                    <a-menu slot="overlay" v-model="moving_viewee" multiple :default-selected-keys="[]"
                      @click="handling">
                      <a-menu-item v-for="name in viewee_list" :key="name">
                        {{ name }}
                      </a-menu-item>
                    </a-menu>
                  </a-dropdown>
                  <!-- 지원자 -->
                  <span v-else v-for="(viewee, i) in moving_viewee" :key="i" class="pr-1">
                    {{ viewee }} &nbsp;
                  </span>
                </v-card-text>
              </v-card>
            </v-col>
          </v-row>

          <v-row>
            <!--스크롤 면접관-->
            <v-col cols="4" class="d-flex flex-column justify-center align-center no-gutters"
              style="">
              <div class="overflow-y-auto pr-2" style="height:70vh">
                <span v-if="!isViewee">
                  <user-video :streamManager="publisher" class="screen-res-sm" />
                </span>
                <span v-for="sub in viewers" :key="sub.stream.connection.connectionId">
                  <user-video class="screen-res-sm" :stream-manager="sub" />
                </span>
              </div>
            </v-col>

            <v-col cols="8" class='d-flex flex-column align-center'>
              <!--지원자 여러명-->
              <v-row v-if="!isViewee" style=" height:18vh; width:100% ">
                <v-col cols="12" class="overflow-x-auto" style="width:100%;">
                 
                <div class='d-flex'>
                  <user-video v-for="sub in viewees" :key="sub.stream.connection.connectionId" class='screen-res  mx-2'
                    :stream-manager="sub" :id="sub.stream.connection.connectionId"
                    @click.native="updateMainVideoStreamManager(sub)" />
                </div>
                </v-col>
              </v-row>
              <!--지원자 1명-->
              <v-row class="d-flex flex-wrap justify-center align-center pt-5" style="">
                <span v-if="isViewee">
                  <user-video :stream-manager="publisher" class="screen-res-md" />
                </span>
                <span v-else>
                  <user-video v-if="mainStreamManager" :stream-manager="mainStreamManager" class="screen-res-md" />
                </span>
              </v-row>
            </v-col>
          </v-row>
          <!--맨위 오른쪽 지원자들-->
          <!-- <v-row>
            <div v-if="!isViewee" class='d-flex justify-center'>
              <user-video v-for="sub in viewees" :key="sub.stream.connection.connectionId"
                class= 'screen-res'
                :stream-manager="sub" 
                :id = "sub.stream.connection.connectionId"
                @click.native="updateMainVideoStreamManager(sub)"
              />
            </div>
          </v-row> -->
          <!-- 면접관 -->
          <!-- <v-row style="height: 87%">
            <v-col cols="4" class="d-flex flex-column justify-center align-center">
               지원자가 아니면 this.publisher는 면접관(대기관)이므로 col 4에 출력
              <span v-if="!isViewee">
                <user-video :streamManager="publisher" class="screen-res-sm"/>
              </span>
               나(this.publisher)를 제외한 모든 면접관
              <span v-for="sub in viewers" :key="sub.stream.connection.connectionId">
                <user-video
                  class="screen-res-sm"
                  :stream-manager="sub"
                />
              </span>
            </v-col>
             지원자 한명의 화면
            <v-col cols="8" class="d-flex flex-wrap justify-center align-center">
               지원자면 나(this.publisher)를 col 8에 출력
              <span v-if="isViewee">
                <user-video :stream-manager="publisher" class="screen-res-md"/>
              </span>
               지원자가 아니면 위의 지원자들 화면에서 클릭한 사람을 보여준다 
              this.mainStreamManager 면접관 입장에서 클릭한 지원자의 계정이 들어있다 
              user-video에서 :stream-manager 이 값은 필수 
              <span v-else>
                <user-video v-if="mainStreamManager" :stream-manager="mainStreamManager" class="screen-res-md"/>
              </span>
            </v-col>
          </v-row> -->
        </v-col>

        <!-- 우측 중앙 - 기능 탭 -->
        <v-col cols="3" class="sub-box">
          <!-- 대기실 - 면접실 메신저 -->
          <div :class="isViewee ? 'hidden' : ''" class="pb-2 mb-2 centering justify-space-between" style="height: 10%">
            <!-- 메시지 보내기 -->
            <a-popover title="Message" trigger="click">
              <v-btn color="#757575" elevation="3" height="100%" width="35%" dark>메시지 보내기</v-btn>
              <template slot="content">
                <v-textarea solo height="100px" clearable no-resize hide-details v-model="messageToSession">
                </v-textarea>
                <v-btn @click="sendToSession" text color="secondary">Send</v-btn>
              </template>
            </a-popover>
            <!-- 메시지 출력 -->
            <v-sheet color="white" height="100%" width="60%" elevation="3" class="d-flex justify-center align-center">
              <!-- <div>면접 준비 완료</div> -->
              <div>{{messageFromSession}}</div>
            </v-sheet>
          </div>
          <!-- 면접 안내 -->
          <div style="height: 35%">
            <v-sheet color="white" height="100%" elevation="3">
              <div class="text-h6 text-center pt-1">면접 안내</div>
              <v-divider class="my-1"></v-divider>
              <!-- 권영일 추가 interview_messages 값 확인 -->
              <v-list class="pa-0" v-auto-bottom="interview_messages">
                <div v-for="(msg, index) in interview_messages" :key="index">
                  {{ getCurrentTime() }} - {{ msg }}
                </div>
                <!-- 권영일 추가 -->
              </v-list>
            </v-sheet>
          </div>
          <!-- FAQ -->
          <div style="height: 10%">
            <v-sheet color="white" height="100%" elevation="3">
              <v-dialog v-model="faq_dialog" scrollable max-width="500px">
                <!-- FAQ 버튼 -->
                <template v-slot:activator="{ on, attrs }">
                  <v-btn color="#757575" dark block tile v-bind="attrs" v-on="on" height="100%">
                    FAQ
                  </v-btn>
                </template>
                <!-- FAQ Modal -->
                <v-card>
                  <v-card-title class="justify-center">FAQ</v-card-title>
                  <v-divider class="mt-2 mb-3"></v-divider>
                  <v-card-text style="height: 300px">
                    <v-expansion-panels accordion focusable>
                      <v-expansion-panel v-for="(item, i) in questions" :key="i">
                        <v-expansion-panel-header>{{ item.title }}</v-expansion-panel-header>
                        <v-expansion-panel-content>
                          <div class="mt-5">{{ item.answer }}</div>
                        </v-expansion-panel-content>
                      </v-expansion-panel>
                    </v-expansion-panels>
                  </v-card-text>
                </v-card>
              </v-dialog>
            </v-sheet>
          </div>
          <!-- 채팅창 -->
          <div style="height: 45%">
            <v-sheet color="white" height="100%" elevation="3">
              <v-list class="pa-0" v-auto-bottom="messages">
                <div v-for="(msg, index) in messages" :key="index">
                  {{ msg.from }} : {{ msg.text }}
                </div>
              </v-list>
              <v-text-field v-model="text" label="메세지를 입력하세요." class="pa-0 ma-0 mx-1" single-line hide-details dense
                @keyup.13="sendMessage"></v-text-field>
            </v-sheet>
          </div>
        </v-col>
      </v-row>
    </v-container>

    <!-- 화면 공유 기능(미구현) -->
    <!-- <div id="screen"></div> -->

    <!-- 메인 하단 - 환경설정 -->
    <v-bottom-navigation dark class="main-bg-navy">
      <v-btn @click="audioOnOOff">
        <v-icon v-if="audioOn === true">mdi-volume-high</v-icon>
        <v-icon v-if="audioOn === false">mdi-volume-off</v-icon>
      </v-btn>
      <v-btn @click="videoOnOOff">
        <v-icon v-if="videoOn === true">mdi-video</v-icon>
        <v-icon v-if="videoOn === false">mdi-video-off</v-icon>
      </v-btn>
      <v-btn @click="leaveSession">방 나가기</v-btn>
    </v-bottom-navigation>
  </div>
</template>

<script>
  import {
    OpenVidu
  } from "openvidu-browser"
  import UserVideo from "@/components/room/UserVideo"
  import axios from "axios"

  import Stomp from 'webstomp-client'
  import SockJS from 'sockjs-client'

  const SERVER_URL = process.env.VUE_APP_SERVER_URL

  export default {
    name: "WaitRoom",
    components: {
      UserVideo,
    },
    data: function () {
      return {
        isViewee: false,
        roomType: '',

        OV: undefined,
        session: undefined,
        mainStreamManager: undefined, // 메인 비디오 <- 가장 큰 비디오
        publisher: undefined, // 연결 객체
        myConnectionId: undefined,

        // 면접관, 지원자들만 담아두는 리스트
        viewers: [],
        viewees: [],

        // 채팅
        text: "",
        messages: [],

        // 화면, 소리, 화면 공유
        audioOn: true,
        videoOn: true,

        // From SessionController
        sessionName: undefined, // 대기방 세션
        token: undefined,
        userName: "",
        type: undefined, // 대기실 관리자(manager) / 면접관(viewer) / 면접자(viewee)

        // From Main.vue
        comName: undefined,
        re_year: undefined,
        re_flag: undefined,
        re_status: undefined,
        interviewSession: undefined,
        userSeq: undefined,
        interviewType: undefined,
        groupTypeSeq: undefined,

        // 배너
        banner_dialog: false,
        banner_message: '',

        // 면접 이동
        visible: false,
        // viewee_list: ['김일번', '박이번', '신삼번', '강사번', '류오번', '이육번'],
        viewee_list: [],
        moving_viewee: [],

        // 면접 안내
        interview_messages: [],

        // 세션간 통신
        messageToSession: '',
        messageFromSession: '',

        // FAQ
        faq_dialog: false,
        questions: [{
            title: '면접 시간은 어떻게 되나요?',
            answer: '안 알랴줌'
          },
          {
            title: '뭐야 내 면접 돌려줘요',
            answer: '안 돼 안 바꿔줘 바꿀 생각 없어 빨리 돌아가'
          },
        ],

        //스크롤
        benched: 0,
      }
    },
    created: function () {
      window.addEventListener("beforeunload", this.leaveSession)
      window.addEventListener("backbutton", this.leaveSession)

      let user_data = ['comName', 're_year', 're_flag', 're_status', 'token', 'userName', 'userSeq', 'type',
        'sessionName', 'interviewSession', 'interviewType', 'groupTypeSeq'
      ]

      for (const data of user_data) {
        this[data] = this.$route.query[data]
      }

      if (this.type === 'viewee') {
        this.isViewee = true;
      } else {
        this.isViewee = false;
        this.connect()
      }

      if (this.interviewType === 'PT') {
        this.roomType = 'pt';
      } else if (this.interviewType === '토론') {
        this.roomType = 'gr';
      } else {
        this.roomType = 'ca';
      }
    },

    beforeDestroy() {
      window.removeEventListener("beforeunload", this.leaveSession)
      window.removeEventListener("backbutton", this.leaveSession)
    },

    mounted() {
      this.OV = new OpenVidu()
      this.session = this.OV.initSession()

      // 신규 생성된 Stream 동기화
      this.session.on("streamCreated", ({
        stream
      }) => {
        const subscriber = this.session.subscribe(stream)

        let info = JSON.parse(subscriber.stream.connection.data.split('%/%')[0])

        if (info['type'] === 'viewee') {
          this.viewees.push(subscriber)
          this.viewee_list.push(info['name'])

          if (!this.mainStreamManager) {
            this.mainStreamManager = subscriber
          }

        } else {
          this.viewers.push(subscriber)
        }
        console.log("viewee : ", this.viewees, " viewer : ", this.viewers)
      })

      // Stream 삭제
      this.session.on("streamDestroyed", ({
        stream
      }) => {
        let info = JSON.parse(stream.connection.data.split('%/%')[0])

        if (info['type'] === 'viewee') {
          const index = this.viewees.indexOf(stream.streamManager, 0);
          if (index >= 0) {
            this.viewees.splice(index, 1);
          }

          const idx = this.viewee_list.indexOf(info['name'], 0);
          if (idx >= 0) {
            this.viewee_list.splice(idx, 1);
          }

          if (this.mainStreamManager.stream === stream) {
            if (this.viewees.length !== 0) {
              this.mainStreamManager = this.viewees[0]
            } else {
              this.mainStreamManager = null
            }
          }

        } else {
          const index = this.viewers.indexOf(stream.streamManager, 0);
          if (index >= 0) {
            this.viewers.splice(index, 1);
          }
        }

        console.log("viewee : ", this.viewees, " viewer : ", this.viewers)
      })

      // this.session.on('publisherStartSpeaking', (event) => {
      //   let id = event.connection.connectionId
      //   this.viewers.forEach(viewer => {
      //     if(viewer !== this.mainStreamManager && viewer.stream.connection.connectionId === id){
      //       document.getElementById(id).classList.add("speaking");
      //     }
      //   })
      //   this.viewees.forEach(viewee => {
      //     if(viewee !== this.mainStreamManager && viewee.stream.connection.connectionId === id){
      //       document.getElementById(id).classList.add("speaking");
      //     }
      //   })
      // });

      // this.session.on('publisherStopSpeaking', (event) => {
      //   let id = event.connection.connectionId

      //   this.viewers.forEach(viewer => {
      //     if(viewer !== this.mainStreamManager && viewer.stream.connection.connectionId === id){
      //       document.getElementById(id).classList.remove("speaking");
      //     }
      //   })
      //   this.viewees.forEach(viewee => {
      //     if(viewee !== this.mainStreamManager && viewee.stream.connection.connectionId === id){
      //       document.getElementById(id).classList.remove("speaking");
      //     }
      //   })
      // });

      this.session.on("signal:my-chat", (event) => {
        let message = {
          from: "",
          text: ""
        }
        message.from = event.from.data.split('":"')[1].slice(0, -7)
        message.text = event.data

        this.messages.push(message)
      })

      this.session.on("signal:signal", (event) => {
        if (this.type === 'viewee' && this.userName === event.data) {
          if (confirm("이동하시겠습니까?")) {
            this.goInterview();
          } else {
            this.session.signal({
                data: "No",
                to: [],
                type: "answer"
              })
              .then()
              .catch(err => console.error(err))
          }
        }
      })

      this.session.on("signal:notice", (event) => {
        this.banner_message = event.data
      })

      this.session.on("signal:answer", (event) => {
        if (this.type === 'manager') {
          let message = event.from.data.split('":"')[1].slice(0, -7)

          if (event.data === "No") {
            message = message + " 입장 보류"
          } else if (event.data === "Yes") {
            message = message + " 입장"
          } else {
            message = message + " 입장 실패"
          }

          this.interview_messages.push(message)
        }
      })

      this.session.connect(this.token, {
          name: this.userName,
          type: this.type,
          userSeq: this.userSeq
        })
        .then(() => {
          let publisher = this.OV.initPublisher(undefined, {
            audioSource: undefined, // The source of audio. If undefined default microphone
            videoSource: undefined, // The source of video. If undefined default webcam
            publishAudio: true, // Whether you want to start publishing with your audio unmuted or not
            publishVideo: true, // Whether you want to start publishing with your video enabled or not
            // resolution: "272x153", // The resolution of your video
            resolution: "1280x720", // The resolution of your video
            frameRate: 30, // The frame rate of your video
            insertMode: "APPEND", // How the video is inserted in the target element 'video-container'
            mirror: false, // Whether to mirror your local video or not
          })

          // this.mainStreamManager = publisher
          this.publisher = publisher

          this.session.publish(this.publisher)

          this.myConnectionId = this.publisher.stream.connection.connectionId

          let info = JSON.parse(this.publisher.stream.connection.data.split('%/%')[0])
          console.log("내 이름은 ", info['name'], " 이고", info['type'], "야")

          // if(this.type === 'viewee'){
          //   this.viewees.push(this.publisher)
          //   this.viewee_list.push(info['name'])
          // }else{
          //   this.viewers.push(this.publisher)
          // }
        })
        .catch(err => {
          console.log("There was an error connecting to the session:", err.code, err.message)
        })

      window.addEventListener("beforeunload", this.leaveSession)
      window.addEventListener("backbutton", this.leaveSession)
    },

    methods: {
      // 채팅 메시지 전송
      sendMessage() {
        if (this.text === "") return

        this.session.signal({
            data: this.text,
            to: [],
            type: "my-chat"
          })
          .then()
          .catch(err => console.error(err))

        this.text = ""
      },

      leaveSession() {
        axios.get(`${SERVER_URL}/session/leaveSession`, {
            params: {
              sessionName: this.sessionName,
              token: this.token,
            },
          })
          .then(() => {
            if (this.session) {
              this.session.disconnect()
              this.session = undefined
              this.mainStreamManager = undefined
              this.publisher = undefined
              this.viewers = [],
                this.viewees = [],
                this.OV = undefined
            }

            window.close()
          })
          .catch(err => console.log(err))
      },

      updateMainVideoStreamManager(stream) {
        console.log("클릭한 세션", stream)
        console.log("클릭 전 메인 스트림", this.mainStreamManager)
        this.mainStreamManager = stream
        console.log("클릭 후 메인 스트림", this.mainStreamManager)
      },

      audioOnOOff() {
        this.audioOn = !this.audioOn

        this.publisher.publishAudio(this.audioOn)
      },

      videoOnOOff() {
        this.videoOn = !this.videoOn

        this.publisher.publishVideo(this.videoOn)
      },

      handling(e) {
        if (this.moving_viewee.includes(e.key)) {
          this.moving_viewee.splice(this.moving_viewee.indexOf(e.key), 1)
        } else {
          this.moving_viewee.push(e.key)
        }

        this.visible = true
      },

      getCurrentTime() {
        let time
        const today = new Date();
        let hour = today.getHours()
        if (hour >= 12) {
          if (hour > 12) hour = hour - 12
          time = "오후 " + hour + " : "
        } else {
          time = "오전 " + hour + " : "
        }
        let minute = today.getMinutes();
        if (minute % 10 === 0) time = time + '0'

        return time + minute
      },

      sendSignal() {
        let message = this.moving_viewee.join(", ")
        this.moving_viewee.forEach(name => {
          this.viewees.forEach(stream => {
            if (JSON.parse(stream.stream.connection.data.split('%/%')[0])['name'] === name) {
              this.session.signal({
                  data: name,
                  to: [stream.stream.connection.connectionId],
                  type: "signal"
                })
                .then()
                .catch(err => console.error(err))
            }
          })
        })
        message += " 에게 입장 메세지 전송"
        this.interview_messages.push(message)
        this.moving_viewee = []
      },

      connect() {
        let socket = new SockJS(`${SERVER_URL}/ws-stomp`);
        this.stompClient = Stomp.over(socket);
        this.stompClient.connect({},
          frame => {
            this.connected = true;
            console.log('소켓 연결 성공', frame);
            this.stompClient.subscribe("/send/" + this.groupTypeSeq, res => {
              // let message = JSON.parse(res.body)
              // if(message['name'] === this.userName) return
              this.messageFromSession = JSON.parse(res.body)['message']
            });
          },
          error => {
            console.log('소켓 연결 실패', error);
            this.connected = false;
          }
        );
      },

      sendToSession() {
        if (this.stompClient && this.stompClient.connected) {
          const msg = {
            name: this.userName,
            message: this.messageToSession
          };
          this.stompClient.send("/receive/" + this.groupTypeSeq, JSON.stringify(msg), {});
        }
        this.messageToSession = '';
      },

      noticeBanner() {
        this.session.signal({
            data: this.banner_message,
            to: [],
            type: "notice"
          })
          .then()
          .catch(err => console.error(err))
        this.banner_message = ''
      },

      goInterview() {
        axios.get(`${SERVER_URL}/session/join`, {
            params: {
              applicantName: this.userName,
              sessionName: this.interviewSession,
            },
          }).then(res => {
            this.session.signal({
                data: "Yes",
                to: [],
                type: "answer"
              })
              .then()
              .catch(err => console.error(err))

            let routeData = this.$router.resolve({
              name: "ViewRoom",
              query: {
                comName: this.comName,
                re_year: this.re_year,
                re_flag: this.re_flag,
                re_status: this.re_status,
                userName: res.data.applicantName,
                type: res.data.type,
                token: res.data.token,
                sessionName: res.data.sessionName,
                userSeq: this.userSeq,
                interviewType: this.interviewType
              },
            })
            this.leaveSession();
            window.open(routeData.href, "_blank")
          })
          .catch(err => {
            alert("방이 아직 개설되지 않았습니다.")
            console.log(err)
            this.session.signal({
                data: "Fail",
                to: [],
                type: "answer"
              })
              .then()
              .catch(err => console.error(err))
          })
      }
    },

    computed: {
      items() {
        return Array.from({
          length: this.length
        }, (k, v) => v + 1)
      },
      length() {
        return 7000
      },
    },
  }
</script>

<style scoped>
  #waitroom {
    /* height: 100%; */
    height: 91vh;
    display: flex;
    flex-direction: column;
    background-color: #ECEFF1;
  }

  .room {
    flex: 1;
    align-items: center;
    max-width: 95%;
  }

  .col {
    padding: 0.5rem;
  }

  .row {
    margin: 0;
    --bs-gutter-x: initial;
  }

  .main-box {
    padding: 0;
  }

  .main-banner {
    height: 14%;
  }

  .main-screen {
    height: 86%;
  }

  .v-list {
    height: 85%;
    width: 100%;
    overflow-y: auto;
  }

  .v-dialog__content {
    position: absolute;
  }

  ::v-deep .banner .v-input,
  ::v-deep .banner .v-input__control {
    height: 100%;
  }

  ::v-deep .ant-popover-inner .ant-popover-inner-content {
    padding-bottom: 0;
  }

  .brd {
    border: 0.1px solid grey;
    background-color: darkgrey;
    color: white;
    font-size: 1.5rem;
    padding-top: 60px;
    text-align: center;
  }

  .centering {
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .hidden {
    visibility: hidden;
  }

  .screen-res {
    width: 160px;
    height: 90px;
  }

  .screen-res-sm {
    width: 288px;
    height: 162px;
  }

  .screen-res-md {
    width: 640px;
    height: 360px;
  }

  .notice {
    color: red
  }

  ::-webkit-scrollbar {
    width: 5px;
    height: 5px;
  }

  /*  / 스크롤 바 밑의 배경 / */
  ::-webkit-scrollbar-track {
    background-color: grey;
    opacity: 0.2;
  }

  /* / 실질적 스크롤 바 / */
  ::-webkit-scrollbar-thumb {
    background: aliceblue;
    border-radius: 10px;
  }

  /* / 스크롤 바 상 하단 버튼 */
  ::-webkit-scrollbar-button {
    display: none;
  }
</style>