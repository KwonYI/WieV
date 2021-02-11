<template>
  <div id="waitRoom" class="d-flex flex-column">
    <!-- 대기실 페이지. 신분에 따라 보여지는 컴포넌트가 다르다.  -->

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


    <v-container style="max-width: 90%; height: 100%">
      <v-row style="margin: auto; height: 100%">

        <v-col cols="9">
          <!-- 메인 상단 - 배너, 면접실 이동 안내 -->
          <v-row style="height: 17%">
            <!-- 공지 배너 -->
            <v-col cols="9" class="banner">
              <v-text-field
                solo
                hide-details
                height="100%"
              ></v-text-field>
            </v-col>
            <!-- 면접실 이동 안내 -->
            <v-col cols="3">
              <v-card color="#304B61" elevation="2" style="height: 100%" dark>
                <v-card-title class="justify-center pt-1 pb-2">
                  <div class="text-center text-subtitle-1">면접실 이동</div>
                  <input class="btn" type="button" v-if="moving_viewee.length" @click="sendSignal" value="면접실 보내기" />
                </v-card-title>
                <v-card-text class="d-flex justify-center pa-0 text-white">
                  <a-dropdown v-model="visible" class="d-flex justify-center align-center" style="width: 90%">
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
                    
                    <a-menu
                      slot="overlay"
                      v-model="moving_viewee"
                      multiple
                      :default-selected-keys="[]"
                      @click="handling"
                    >
                      <a-menu-item v-for="name in viewee_list" :key="name">
                        {{ name }}
                      </a-menu-item>
                    </a-menu>
                  </a-dropdown>
                </v-card-text>
              </v-card>
            </v-col>
          </v-row>
          <!-- 메인 중앙 - 면접관, 지원자 화면 -->

          <v-row style="height: 87%">
            <v-col cols="4" class="d-flex flex-column justify-center align-center">
              <span v-for="sub in viewers" :key="sub.stream.connection.connectionId">
                <user-video
                  :stream-manager="sub" 
                />
              </span>
            </v-col>
            <!-- 지원자 -->
            <v-col cols="8" class="d-flex flex-wrap justify-center align-center">
              <span v-for="sub in viewees" :key="sub.stream.connection.connectionId">
                <user-video
                  :stream-manager="sub" 
                  @click.native="updateMainVideoStreamManager(sub)"
                />
              </span>
            </v-col>
            <!-- 면접관 -->
            <!-- <v-col cols="4" class="d-flex flex-column justify-center align-center">
              <span v-for="sub in subscribers" :key="sub.stream.connection.connectionId">
                <user-video
                  v-if="JSON.parse(sub.stream.connection.data.split('%/%')[0])['type'] !== 'viewee'"
                  :stream-manager="sub" 
                  @click.native="updateMainVideoStreamManager(sub)"
                />
              </span>
            </v-col> -->
            <!-- 지원자 -->
            <!-- <v-col cols="8" class="d-flex flex-wrap justify-center align-center">
              <span v-for="sub in subscribers" :key="sub.stream.connection.connectionId">
                <user-video
                  v-if="JSON.parse(sub.stream.connection.data.split('%/%')[0])['type'] === 'viewee'"
                  :stream-manager="sub" 
                  @click.native="updateMainVideoStreamManager(sub)"
                />
              </span>
            </v-col> -->
          </v-row>
        </v-col>

        <!-- 우측 중앙 - 부가기능 탭 -->
        <v-col cols="3">
          <!-- 대기실 - 면접실 메신저 -->
          <div class="pb-2 mb-2 d-flex justify-space-between align-center" style="height: 10%">
            <!-- 메시지 보내기 -->
            <a-popover title="Message" trigger="click">
              <v-btn color="#757575" elevation="3" height="100%" width="35%" dark>메시지 보내기</v-btn>
              <template slot="content">
                <v-textarea
                  solo
                  height="100px"
                  clearable
                  no-resize
                  hide-details
                ></v-textarea>
                <v-btn text color="secondary">Send</v-btn>
              </template>
            </a-popover>
            <!-- 메시지 출력 -->
            <v-sheet color="white" height="100%" width="60%" elevation="3" class="d-flex justify-center align-center">
              <div>면접 준비 완료</div>
            </v-sheet>
          </div>
          <!-- 면접 안내 -->
          <div style="height: 35%">
            <v-sheet color="white" height="100%" elevation="3">
              <div class="text-h6 text-center pt-1">면접 안내</div>
              <v-divider class="my-1"></v-divider>
            </v-sheet>
          </div>
          <!-- FAQ -->
          <div style="height: 10%">
            <v-sheet color="white" height="100%" elevation="3">
              <v-dialog
                v-model="faq_dialog"
                scrollable
                max-width="500px"
              > 
                <!-- FAQ 버튼 -->
                <template v-slot:activator="{ on, attrs }">
                  <v-btn
                    color="#757575"
                    dark
                    block
                    tile
                    v-bind="attrs"
                    v-on="on"
                    height="100%"
                  >
                    FAQ
                  </v-btn>
                </template>
                <!-- FAQ Modal -->
                <v-card>
                  <v-card-title class="justify-center">FAQ</v-card-title>
                  <v-divider class="mt-2 mb-3"></v-divider>
                  <v-card-text style="height: 300px">
                    <v-expansion-panels accordion focusable>
                      <v-expansion-panel
                        v-for="(item, i) in questions"
                        :key="i"
                      >
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
              <v-text-field
                v-model="text"
                label="메세지를 입력하세요."
                class="pa-0 ma-0 mx-1"
                hide-details
                dense
                @keyup.13="sendMessage"
              ></v-text-field>
            </v-sheet>
          </div>
        </v-col>
      </v-row>
    </v-container>

    <!-- 화면 공유 기능(미구현) -->
    <!-- <div id="screen"></div> -->
    
    <!-- 바 밑에 내용물들.  -->    
    <v-bottom-navigation dark class="main-bg-navy">
      <input class="btn" type="button" @click="audioOnOOff" :value="audioMsg" />
      <input class="btn" type="button" @click="videoOnOOff" :value="videoMsg" />
      <input class="btn" type="button" @click="leaveSession" value="방 나가기" />
      <input class="btn" type="button" v-if="type === 'viewee' " @click="goInterview" value="면접실 들어가기" />
    </v-bottom-navigation>
  </div>
</template>

<script>
import { OpenVidu } from "openvidu-browser"
import UserVideo from "@/components/room/UserVideo"
import axios from "axios"

import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'

const SERVER_URL = process.env.VUE_APP_SERVER_URL
// import ManagerList from "@/components/room/ManagerList.vue"
// import VieweeList from "@/components/room/VieweeList.vue"

export default {
  name: "WaitRoom",
  components: {
    UserVideo,
    // ManagerList,
    // VieweeList,
  },
  data: function () {
    return {
      OV: undefined,
      session: undefined,
      mainStreamManager: undefined, // 메인 비디오
      publisher: undefined, // 연결 객체
      subscribers: [],

      // 면접관, 지원자들만 담아두는 리스트
      viewers : [],
      viewees : [],

      // 채팅
      text: "",
      messages: [],

      // 화면, 소리, 화면 공유
      audioOn: true,
      audioMsg: "소리 Off",
      videoOn: true,
      videoMsg: "화면 Off",
      // shareOn: false,
      // shareMsg: "공유 Off",

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
      userSeq : undefined,

      // 배너
      banner_dialog: false,

      // 면접 이동
      visible: false,
      // viewee_list: ['김일번', '박이번', '신삼번', '강사번', '류오번', '이육번'],
      viewee_list : [],
      moving_viewee: [],

      // 면접 안내

      // FAQ
      faq_dialog: false,
      questions: [
        { title: '면접 시간은 어떻게 되나요?', answer: '안 알랴줌' },
        { title: '뭐야 내 면접 돌려줘요', answer: '안 돼 안 바꿔줘 바꿀 생각 없어 빨리 돌아가' },
      ]
    }
  },
  created: function () {
    //소켓 연결 시도
    this.connect()

    window.addEventListener("beforeunload", this.leaveSession)
    window.addEventListener("backbutton", this.leaveSession)

    let user_data = ['comName', 're_year', 're_flag', 're_status', 'token', 'userName', 'userSeq', 'type', 'sessionName', 'interviewSession']

    for (const data of user_data) {
      this[data] = this.$route.query[data]
    }

    // this.comName = this.$route.query.comName
    // this.re_year = this.$route.query.re_year
    // this.re_flag = this.$route.query.re_flag
    // this.re_status = this.$route.query.re_status
    // this.sessionName = this.$route.query.sessionName
    // this.token = this.$route.query.token
    // this.userName = this.$route.query.userName
    // this.type = this.$route.query.type
  },
  beforeDestroy() {
    window.removeEventListener("beforeunload", this.leaveSession)
    window.removeEventListener("backbutton", this.leaveSession)
  },

  mounted() {
    this.OV = new OpenVidu()
    this.session = this.OV.initSession()

    this.session.on("streamCreated", ({ stream }) => {
      const subscriber = this.session.subscribe(stream)

      let info = JSON.parse(subscriber.stream.connection.data.split('%/%')[0])

      if(info['type'] === 'viewee'){
        this.viewees.push(subscriber)
        this.viewee_list.push(info['name'])
      }else{
        this.viewers.push(subscriber)
      }
      
      // this.subscribers.push(subscriber)
    })

    this.session.on("streamDestroyed", ({ stream }) => {
      let info = JSON.parse(stream.connection.data.split('%/%')[0])

      if(info['type'] === 'viewee'){
        const index = this.viewees.indexOf(stream.streamManager, 0);
        if (index >= 0) {
          this.viewees.splice(index, 1);
        }

        const idx = this.viewee_list.indexOf(info['name'], 0);
        if (idx >= 0) {
          this.viewee_list.splice(idx, 1);
        }

      }else{
        const index = this.viewers.indexOf(stream.streamManager, 0);
        if (index >= 0) {
          this.viewers.splice(index, 1);
        }
      }

      console.log("viewee : ",this.viewees, " viewer : ", this.viewers)
      // const index = this.subscribers.indexOf(stream.streamManager, 0)
      // if (index >= 0) {
      //   this.subscribers.splice(index, 1)
      // }
    })

    this.session.on('publisherStartSpeaking', (event) => {
      console.log('Publisher ' +  JSON.parse(event.connection.data,split('%/%')[0])['name'] + ' start speaking');
    });

    this.session.on('publisherStopSpeaking', (event) => {
      console.log('Publisher ' + JSON.parse(event.connection.data,split('%/%')[0])['name'] + ' stop speaking');
    });

    this.session.on("signal:my-chat", (event) => {
      let message = { from: "", text: "" }
      message.from = event.from.data.split('":"')[1].slice(0, -7)
      message.text = event.data

      this.messages.push(message)
    })

    this.session.connect(this.token, { name: this.userName, type : this.type, userSeq : this.userSeq, interviewSession : this.interviewSession})
      .then(() => {
        let publisher = this.OV.initPublisher(undefined, {
          audioSource: undefined, // The source of audio. If undefined default microphone
          videoSource: undefined, // The source of video. If undefined default webcam
          publishAudio: true, // Whether you want to start publishing with your audio unmuted or not
          publishVideo: true, // Whether you want to start publishing with your video enabled or not
          resolution: "272x153", // The resolution of your video
          frameRate: 30, // The frame rate of your video
          insertMode: "APPEND", // How the video is inserted in the target element 'video-container'
          mirror: false, // Whether to mirror your local video or not
        })

        this.mainStreamManager = publisher
        this.publisher = publisher

        this.session.publish(this.publisher)

        let info = JSON.parse(this.publisher.stream.connection.data.split('%/%')[0])
        console.log("내 이름은 ", info['name'], " 이고", info['type'], "야")

        if(this.type === 'viewee'){
          this.viewees.push(this.publisher)
          this.viewee_list.push(info['name'])
        }else{
          this.viewers.push(this.publisher)
        }
        // this.subscribers.push(this.publisher)
      })
      .catch(err => {
        console.log("There was an error connecting to the session:", err.code, err.message)
      })

    window.addEventListener("beforeunload", this.leaveSession)
    window.addEventListener("backbutton", this.leaveSession)
  },

  methods: {
    sendMessage() {
      if (this.text === "") return

      this.session.signal({ data: this.text, to: [], type: "my-chat" })
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
            this.subscribers = []
            this.OV = undefined
          }

          window.close()
        })
        .catch((err) => {
          console.log(err);
        });
    },

    updateMainVideoStreamManager(stream) {
      // 지원자면 이벤트 발생 X
      if(JSON.parse(this.mainStreamManager.stream.connection.data.split('%/%')[0])['type'] === 'viewee') {
        return
      }
      // 자기자신이면 이벤트 발생X
      if (this.mainStreamManager === stream) {
        return
      }
      let info = JSON.parse(stream.stream.connection.data.split('%/%')[0])
      console.log(info)
    },

    audioOnOOff() {
      this.audioOn = !this.audioOn
      if (this.audioOn === true) this.audioMsg = "소리 Off"
      else this.audioMsg = "소리 On"

      this.publisher.publishAudio(this.audioOn)
    },

    videoOnOOff() {
      this.videoOn = !this.videoOn
      if (this.videoOn === true) this.videoMsg = "화면 Off"
      else this.videoMsg = "화면 On"

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

    sendSignal() {
      console.log("Send Signal");
      if (this.stompClient && this.stompClient.connected) {
        const msg = {
          message : '면접실 보내기',
          target : this.moving_viewee,
        };
        this.stompClient.send("/receive", JSON.stringify(msg), {});
      }
    },

    connect() {
      let socket = new SockJS(SERVER_URL);
      this.stompClient = Stomp.over(socket);
      this.stompClient.connect(
        {},
        frame => {
          // 소켓 연결 성공
          this.connected = true;
          console.log('소켓 연결 성공', frame);
          this.stompClient.subscribe("/send", res => {
            let target = JSON.parse(res.body)['target']
            target.forEach(name => {
              if(name === this.userName){
                alert("이동하래")
              }
            });
          });
        },
        error => {
          console.log('소켓 연결 실패', error);
          this.connected = false;
        }
      );        
    },

    goInterview(){
      axios.get(`${SERVER_URL}/session/join`, {
        params: {
          applicantName: this.userName,
          sessionName: this.interviewSession,
        },
      }).then(res => {
          console.log(res);
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
              userSeq : this.userSeq
            },
          })
          this.leaveSession();
          window.open(routeData.href, "_blank")
        })
        .catch(err => {
          alert("방이 아직 개설되지 않았습니다.")
          console.log(err)
        })
      }
  },

  
  computed: {
  },
}
</script>

<style scoped>
#waitRoom {
  height: 100%;
  background-color: #ECEFF1;
}
.v-list {
  height: 85%;
  width: 100%;
  overflow-y: auto;
}
.v-dialog__content {
  position: absolute;
}
::v-deep .banner .v-input, ::v-deep .banner .v-input__control {
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
.screen-res {
  width: 272px;
  height: 153px;
}
</style>
