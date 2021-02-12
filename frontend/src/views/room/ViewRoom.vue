<template>
  <div id="viewroom">
    <!-- 
    화면 해상도
    <div class="brd" style="width: 400px; height: 225px"></div>
    <div class="brd" style="width: 480px; height: 270px"></div>
    <div class="brd" style="width: 640px; height: 360px"></div>
    <div class="brd" style="width: 960px; height: 540px"></div> -->

    <!-- 면접 유형에 따른 구분 -->
    <v-container class="room">
      <!-- 그룹형 면접실 -->
      <GrView v-if="roomType === 'gr'" />
      <!-- PT형 면접실 -->
      <PTView v-else-if="roomType === 'pt'" />
      <!-- 일반형 면접실 -->
      <CaView v-else />
    </v-container>

    <!-- 화면 공유 기능(미구현) -->
    <!-- <div id="screen"></div> -->
    
    <!-- 메인 하단 - 환경설정 -->
    <v-bottom-navigation dark class="main-bg-navy mt-auto">
      <input class="btn" type="button" @click="audioOnOOff" :value="audioMsg" />
      <input class="btn" type="button" @click="videoOnOOff" :value="videoMsg" />
      <input class="btn" type="button" @click="leaveSession" value="방 나가기" />
    </v-bottom-navigation>
  </div>
</template>

<script>
import { OpenVidu } from "openvidu-browser"
// import UserVideo from "@/components/room/UserVideo"
import CaView from "@/components/room/CaView"
import PTView from "@/components/room/PTView"
import GrView from "@/components/room/GrView"
import axios from "axios"


const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  name: "ViewRoom",
  components: {
    // UserVideo,
    CaView,
    PTView,
    GrView
  },
  data: function () {
    return {
      roomType: '',

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
  },

  beforeDestroy() {
    window.removeEventListener("beforeunload", this.leaveSession)
    window.removeEventListener("backbutton", this.leaveSession)
  },

  mounted() {
    this.OV = new OpenVidu()
    this.session = this.OV.initSession()

    // 신규 생성된 Stream 동기화
    this.session.on("streamCreated", ({ stream }) => {
      const subscriber = this.session.subscribe(stream)
      this.subscribers.push(subscriber)
    })

    // Stream 삭제
    this.session.on("streamDestroyed", ({ stream }) => {
      const index = this.subscribers.indexOf(stream.streamManager, 0)
      if (index >= 0) {
        this.subscribers.splice(index, 1)
      }
    })

    // 채팅 기능 -> 세션 동기화
    this.session.on("signal:my-chat", (event) => {
      let message = { from: "", text: "" }
      message.from = event.from.data.split('":"')[1].slice(0, -7)
      message.text = event.data

      this.messages.push(message)
    })

    // 신규 Stream 생성 및 퍼블리싱
    this.session.connect(this.token, { name: this.userName, type : this.type})
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
        this.subscribers.push(this.publisher)
      })
      .catch(err => {
        console.log("There was an error connecting to the session:", err.code, err.message)
      })

    window.addEventListener("beforeunload", this.leaveSession)
  },

  methods: {
    // 채팅 메시지 전송
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
          if (this.session) this.session.disconnect()
          this.session = undefined
          this.mainStreamManager = undefined
          this.publisher = undefined
          this.subscribers = []
          this.OV = undefined
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
#viewroom {
  height: 100%;
  display: flex;
  flex-direction: column;
  background-color: #ECEFF1;
}
.room {
  flex: 1;
  align-items: center;
  max-width: 95%;
}
</style>
