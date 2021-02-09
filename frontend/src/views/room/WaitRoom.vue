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
            <!-- 면접관 -->
            <v-col cols="4" class="d-flex flex-column justify-center align-center">
              <div v-for="i in 3" :key="i" class="brd screen-res">면접관 {{i}}</div>
            </v-col>
            <!-- 지원자 -->
            <v-col cols="8" class="d-flex flex-wrap justify-center align-center">
              <div v-for="i in 3" :key="i" class="brd screen-res">지원자 {{i}}</div>
            </v-col>
          </v-row>
        </v-col>

        <v-col cols="3">
          <!-- 우측 중앙 - 부가기능 탭 -->
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
              <div class="headline text-center">면접 안내</div>
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
    <v-container class="d-none">
      <!--row1. : 공지사항 배너, 면접실 만들기 버튼-->
      <v-row> </v-row>

      <!-- row2 : 관리자, 지원자, FAQ 잡동사니 row-->
      <v-row>
        <!-- row2[왼쪽] : 관리자 리스트-->
        <div v-for="sub in subscribers" :key="sub.stream.connection.connectionId">
          <v-col cols="3" v-if="JSON.parse(sub.stream.connection.data.split('%/%')[0])['type'] !== 'viewee' ">
             <user-video 
             :stream-manager="sub" 
             @click.native="updateMainVideoStreamManager(sub)"/>
          </v-col>
          <v-col class="d-flex justify-end" cols="6" v-else>
             <user-video 
             :stream-manager="sub" 
             @click.native="updateMainVideoStreamManager(sub)"/>
          </v-col>
        </div>

        <!-- <user-video v-for="(sub) in subscribers" 
        :key="sub.stream.connection.connectionId" 
        :stream-manager="sub" @click.native="updateMainVideoStreamManager(sub)"/> -->
        <!-- <div v-for="(sub) in subscribers" :key="sub.stream.connection.connectionId">
          <v-col cols="3" v-if="types[sub.stream.connection.connectionId] === 'manager'">
            <user-video :stream-manager="sub" @click.native="updateMainVideoStreamManager(sub)"/>
          </v-col>
          <v-col cols="6" class="d-flex justify-end" v-else>
            <user-video :stream-manager="sub" @click.native="updateMainVideoStreamManager(sub)"/>
          </v-col>
        </div> -->
        
        <!-- <v-col cols="3">
          <user-video :stream-manager="mainStreamManager" />
          <ManagerList />
        </v-col> -->
        
      </v-row>
    </v-container>
    
    <v-bottom-navigation dark class="main-bg-navy">
      <input class="btn" type="button" @click="audioOnOOff" :value="audioMsg" />
      <input class="btn" type="button" @click="videoOnOOff" :value="videoMsg" />
      <input class="btn" type="button" @click="leaveSession" value="방 나가기" />
    </v-bottom-navigation>
  </div>
</template>

<script>
import { OpenVidu } from "openvidu-browser"
import UserVideo from "@/components/room/UserVideo"
import axios from "axios"


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
      sessionName: undefined,
      token: undefined,
      userName: "",
      type: undefined, // 대기실 관리자(manager) / 면접관(interviewer) / 면접자(interviewee)

      // From Main.vue
      comName: undefined,
      re_year: undefined,
      re_flag: undefined,
      re_status: undefined,

      // 배너
      banner_dialog: false,

      // 면접 이동
      visible: false,
      viewee_list: ['김일번', '박이번', '신삼번', '강사번', '류오번', '이육번'],
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
    window.addEventListener("beforeunload", this.leaveSession)
    window.addEventListener("backbutton", this.leaveSession)

    let user_data = ['comName', 're_year', 're_flag', 're_status', 'sessionName', 'token', 'userName', 'type']

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
      this.subscribers.push(subscriber)
    })

    this.session.on("streamDestroyed", ({ stream }) => {
      const index = this.subscribers.indexOf(stream.streamManager, 0)
      if (index >= 0) {
        this.subscribers.splice(index, 1)
      }
    })

    this.session.on("signal:my-chat", (event) => {
      let message = { from: "", text: "" }
      message.from = event.from.data.split('":"')[1].slice(0, -8)
      message.text = event.data

      this.messages.push(message)
    })

    this.session.connect(this.token, { name: this.userName, type : this.type})
      .then(() => {
        let publisher = this.OV.initPublisher(undefined, {
          audioSource: undefined, // The source of audio. If undefined default microphone
          videoSource: undefined, // The source of video. If undefined default webcam
          publishAudio: true, // Whether you want to start publishing with your audio unmuted or not
          publishVideo: true, // Whether you want to start publishing with your video enabled or not
          resolution: "640x360", // The resolution of your video
          frameRate: 30, // The frame rate of your video
          insertMode: "APPEND", // How the video is inserted in the target element 'video-container'
          mirror: false, // Whether to mirror your local video or not
        })

        this.mainStreamManager = publisher
        this.publisher = publisher

        this.session.publish(this.publisher)
        this.subscribers.push(this.publisher)
      })
      .catch(err => {
        console.log("There was an error connecting to the session:", err.code, err.message)
      })

    window.addEventListener("beforeunload", this.leaveSession)
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
        .then(res => {
          console.log(res)
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
        .catch(err => console.log(err))
    },

    updateMainVideoStreamManager(stream) {
      if (this.mainStreamManager === stream) {
        return this.mainStreamManager = stream
      }
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
