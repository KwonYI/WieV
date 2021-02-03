<template>
  <div id="waitRoom">
    <!-- 대기실 페이지. 신분에 따라 보여지는 컴포넌트가 다르다.  -->

    <!--상단 바-->
    <div>
      <v-app-bar color="deep-purple accent-4" dense dark>
        <div class="d-flex">
          <v-toolbar-title class="mx-3">현재 면접자 수 : __ 명</v-toolbar-title>
          <v-toolbar-title class="mx-3">현재 시간 : </v-toolbar-title>
        </div>

        <div class="float-right">
          <v-toolbar-title class="mx-3">
            {{ com_name }} {{ re_year }}{{ re_flag }} {{ re_status }}
          </v-toolbar-title>
        </div>
      </v-app-bar>
    </div>

    <!-- 바 밑에 내용물들.  -->
    <v-container>
      <!--row1. : 공지사항 배너, 면접실 만들기 버튼-->
      <v-row> </v-row>

      <!-- row2 : 관리자, 지원자, FAQ 잡동사니 row-->
      <v-row>
        <!-- row2[왼쪽] : 관리자 리스트-->
        <v-col cols="3">
          <!-- <ManagerList /> -->
        </v-col>

        <!-- row2[가운데] : 지원자 리스트-->
        <v-col cols="6">
          <!-- <VieweeList /> -->
        </v-col>
        <!-- row2[오른쪽] : 우측에 FAQ 채팅창 잡동사니 -->
        <v-col cols="3"></v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
//import axios from "axios"
import { OpenVidu } from "openvidu-browser"

import { mapState } from "vuex"

// import ManagerList from "@/components/room/ManagerList.vue"
// import VieweeList from "@/components/room/VieweeList.vue"
// import UserVideo from "@/components/room/UserVideo"

export default {
  name: "WaitRoom",
  components: {
    // UserVideo,
    // ManagerList,
    // VieweeList,
  },
  data: function() {
    return {
      participants: {
        김면접1: "manager",
        김지원1: "interviewer",
        박면접2: "manager",
        박지원2: "interviewer",
        송지원3: "interviewer",
      },

      com_name: "버즈글로벌",
      re_year: 2021,
      re_flag: "상반기",
      re_status: "신입",

      OV: undefined,
      session: undefined,
      mainStreamManager: undefined, // 메인 비디오
      publisher: undefined, // 연결 객체
      subscribers: [],

      // From SessionController
      sessionName: undefined,
      token: undefined,
      userName: undefined,
      type: undefined, // 대기실 관리자(manager) / 면접관(interviewer) / 면접자(interviewee)
    }
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

    this.session
      .connect(this.token, { clienData: this.userName })
      .then(() => {
        let publisher = this.OV.initPublisher(undefined, {
          audioSource: undefined, // The source of audio. If undefined default microphone
          videoSource: undefined, // The source of video. If undefined default webcam
          publishAudio: true, // Whether you want to start publishing with your audio unmuted or not
          publishVideo: true, // Whether you want to start publishing with your video enabled or not
          resolution: "640x480", // The resolution of your video
          frameRate: 30, // The frame rate of your video
          insertMode: "APPEND", // How the video is inserted in the target element 'video-container'
          mirror: false, // Whether to mirror your local video or not
        })

        this.mainStreamManager = publisher
        this.publisher = publisher

        this.session.publish(this.publisher)

        // if(type === "interviewee")
        // else
      })
      .catch((error) => {
        console.warn(
          "There was an error connecting to the session:",
          error.code,
          error.message
        )
      })

    window.addEventListener("beforeunload", this.leaveSession)
  },

  methods: {
    leaveSession() {
      if (this.session) this.session.disconnect()

      this.session = undefined
      this.mainStreamManager = undefined
      this.publisher = undefined
      this.subscribers = []
      this.OV = undefined

      window.removeEventListener("beforeunload", this.leaveSession)
    },

    updateMainVideoStreamManager(stream) {
      // 클릭시 객체 정보 반환
      if (this.mainStreamManager === stream) return
      this.mainStreamManager = stream
    },
  },

  created: function() {
    // if (this.viewerLogin) {
    //   this.$router.push({ name: "ViewerRecruitItem" })
    // }
  },

  computed: {
    ...mapState(["whoLogin"]),
  },
}
</script>

<style scoped>
#standbyRoom {
  background-color: white;
  /* width: 100vw; */
  height: 100vh;
}
</style>
