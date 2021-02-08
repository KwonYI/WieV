<template>
  <div id="waitRoom">
    <!-- 대기실 페이지. 신분에 따라 보여지는 컴포넌트가 다르다.  -->

    <!--상단 바-->
    <v-app-bar class="d-flex justify-end" dense dark>
      <v-toolbar-title class="">
        {{ comName }}
        {{ re_year }}
        {{ re_flag }}
        {{ re_status }}
        <input
          class="btn"
          type="button"
          @click="audioOnOOff"
          :value="audioMsg"
        />
        <input
          class="btn"
          type="button"
          @click="videoOnOOff"
          :value="videoMsg"
        />
        <input
          class="btn"
          type="button"
          @click="leaveSession"
          value="방 나가기"
        />
      </v-toolbar-title>
    </v-app-bar>

    <v-container>
      <!--row1. : 공지사항 배너, 면접실 만들기 버튼-->
      <v-row> </v-row>

      <v-row>
        <v-col cols="9" v-for="(sub, index) in subscribers" :key="index">
          <v-col cols="3" v-if="index < 3">
            <user-video
              :stream-manager="sub"
              @click.native="updateMainVideoStreamManager(sub)"
            />
          </v-col>
          <v-col cols="6" v-else
            ><user-video
              :stream-manager="sub"
              @click.native="updateMainVideoStreamManager(sub)"
            />
          </v-col>
        </v-col>
        <v-col cols="3">
          <div>
            <v-list v-auto-bottom="messages">
              <div v-for="(msg, index) in messages" :key="index">
                {{ msg.from }} : {{ msg.text }}
              </div>
            </v-list>
          </div>
          <div>
            <v-text-field
              label="보낼 메세지를 입력하세요."
              v-model="text"
              @keyup.13="sendMessage"
            ></v-text-field>
          </div>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import { OpenVidu } from "openvidu-browser";
import UserVideo from "@/components/room/UserVideo";
import axios from "axios";

// import { mapGetters, mapMutations } from "vuex";

const SERVER_URL = process.env.VUE_APP_SERVER_URL;
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
      audioMsg: "소리 On",
      videoOn: true,
      videoMsg: "화면 On",
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
    };
  },
  created: function () {
    window.addEventListener("beforeunload", this.leaveSession);
    window.addEventListener("backbutton", this.leaveSession);

    // this.comName = this.$route.params.interview.comName;
    // this.re_year = this.$route.params.interview.recruitYear;
    // this.re_flag = this.$route.params.interview.recruitFlag;
    // this.re_status = this.$route.params.interview.recruitStatus;
    // this.sessionName = this.$route.params.interviewer.sessionName;
    // this.token = this.$route.params.interviewer.token;
    // this.userName = this.$route.params.interviewer.interviewerName;
    // this.type = this.$route.params.interviewer.type;

    this.comName = this.$route.query.comName;
    this.re_year = this.$route.query.re_year;
    this.re_flag = this.$route.query.re_flag;
    this.re_status = this.$route.query.re_status;
    this.sessionName = this.$route.query.sessionName;
    this.token = this.$route.query.token;
    this.userName = this.$route.query.userName;
    this.type = this.$route.query.type;
  },
  beforeDestroy() {
    window.removeEventListener("beforeunload", this.leaveSession);
    window.removeEventListener("backbutton", this.leaveSession);
  },

  mounted() {
    this.OV = new OpenVidu();
    this.session = this.OV.initSession();

    this.session.on("streamCreated", ({ stream }) => {
      console.log(
        "스트림 크리에이트 실행, 나를 제외한 방에 있는 다른 비디오 연결중"
      );
      const subscriber = this.session.subscribe(stream);
      this.subscribers.push(subscriber);
    });

    this.session.on("streamDestroyed", ({ stream }) => {
      const index = this.subscribers.indexOf(stream.streamManager, 0);
      if (index >= 0) {
        this.subscribers.splice(index, 1);
      }
    });

    this.session.on("signal:my-chat", (event) => {
      let message = { from: "", text: "" };
      message.from = event.from.data.split('":"')[1].slice(0, -8);
      message.text = event.data;

      this.messages.push(message);
    });

    this.session
      .connect(this.token, { clientData: this.userName })
      .then(() => {
        console.log("세션 커넥트 실행");
        let publisher = this.OV.initPublisher(undefined, {
          audioSource: undefined, // The source of audio. If undefined default microphone
          videoSource: undefined, // The source of video. If undefined default webcam
          publishAudio: true, // Whether you want to start publishing with your audio unmuted or not
          publishVideo: true, // Whether you want to start publishing with your video enabled or not
          resolution: "320x240", // The resolution of your video
          frameRate: 30, // The frame rate of your video
          insertMode: "BEFORE", // How the video is inserted in the target element 'video-container'
          mirror: false, // Whether to mirror your local video or not
        });

        this.mainStreamManager = publisher;
        this.publisher = publisher;

        this.session.publish(this.publisher);
        this.subscribers.push(this.publisher);
      })
      .catch((error) => {
        console.log(
          "There was an error connecting to the session:",
          error.code,
          error.message
        );
      });
    window.addEventListener("beforeunload", this.leaveSession);
  },

  methods: {
    // ...mapMutations([
    //   "addParticipants",
    //   "clearParticipants",
    //   "deleteParticipants",
    //   "clearCheckIn",
    //   "deleteCheckIn",
    // ]),

    sendMessage() {
      if (this.text === "") return;

      this.session
        .signal({
          data: this.text,
          to: [],
          type: "my-chat",
        })
        .then(() => {})
        .catch((error) => {
          console.error(error);
        });

      this.text = "";
    },

    leaveSession() {
      axios
        .get(`${SERVER_URL}/session/leaveSession`, {
          params: {
            sessionName: this.sessionName,
            token: this.token,
          },
        })
        .then(() => {
          if (this.session) this.session.disconnect();

          this.session = undefined;
          this.mainStreamManager = undefined;
          this.publisher = undefined;
          this.subscribers = [];
          this.OV = undefined;

          window.close();
        })
        .catch((err) => {
          console.log(err);
          console.log("방 나가기 실패!");
        });
    },

    updateMainVideoStreamManager(stream) {
      if (this.mainStreamManager === stream) return;
      this.mainStreamManager = stream;
    },

    audioOnOOff() {
      this.audioOn = !this.audioOn;
      if (this.audioOn === true) this.audioMsg = "소리 Off";
      else this.audioMsg = "소리 On";

      this.publisher.publishAudio(this.audioOn);
    },

    videoOnOOff() {
      this.videoOn = !this.videoOn;
      if (this.videoOn === true) this.videoMsg = "화면 Off";
      else this.videoMsg = "화면 On";

      this.publisher.publishVideo(this.videoOn);
    },
  },

  computed: {
    // ...mapGetters(["getParticipants", "getCheckIn"]),
  },
};
</script>

<style scoped>
#standbyRoom {
  background-color: white;
  /* width: 100vw; */
  height: 100vh;
}
.v-list {
  height: 500px;
  width: 300px;
  overflow-y: auto;
}
</style>