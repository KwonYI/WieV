<template>
  <div id="ViewRoom">
    <!-- 면접실 페이지. 신분에 따라 보여지는 컴포넌트가 다르다.  -->
    <h2>테스트용 면접방</h2>
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

    <div id="screen"></div>
    <!-- 바 밑에 내용물들.  -->
    <v-container>
      <!--row1. : 공지사항 배너, 면접실 만들기 버튼-->
      <v-row> </v-row>

      <!-- row2 : 관리자, 지원자, FAQ 잡동사니 row-->
      <v-row>
        <!-- row2[왼쪽] : 관리자 리스트-->
        <div v-for="(sub) in subscribers" :key="sub.stream.connection.connectionId">
          <!-- <v-col cols="3" v-if="JSON.parse(sub.stream.connection.data.split('%/%')[0])['type'] === 'manager' "> -->
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
        <v-col cols="3">
          <!-- <user-video :stream-manager="mainStreamManager" /> -->
        </v-col>
        <!-- row2[오른쪽] : 우측에 FAQ 채팅창 잡동사니 -->
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


const SERVER_URL = process.env.VUE_APP_SERVER_URL;

export default {
  name: "WaitRoom",
  components: {
    UserVideo,
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
<<<<<<< HEAD
      
      //지원자 리스트
      applicantList:[],
=======
      userSeq : undefined,
>>>>>>> 3afdc75c11a4453b61f0718d5d854d1b2f1d93fd
    };
  },
  created: function () {
    window.addEventListener("beforeunload", this.leaveSession);
    window.addEventListener("backbutton", this.leaveSession);

    this.comName = this.$route.query.comName;
    this.re_year = this.$route.query.re_year;
    this.re_flag = this.$route.query.re_flag;
    this.re_status = this.$route.query.re_status;
    this.sessionName = this.$route.query.sessionName;
    this.token = this.$route.query.token;
    this.userName = this.$route.query.userName;
    this.type = this.$route.query.type;
    this.userSeq = this.$route.query.userSeq;

    //면접방에 해당하는 지원자 갖고오기
    console.log("방세션이름"+this.sessionName)
     axios.get(`${SERVER_URL}/applicant/getListBySessionName/` +this.sessionName)
        .then(res => {
          //console.log(res.data)
          this.applicantList=res.data
          console.log(this.applicantList)
          for (const apply of this.applicantList) {
            console.log(apply["apply-Resume1"])
          }
        })
        .catch((err) => {
            console.log(err)
            alert("세션 이름에 따른 지원자 갖고오기 실패")
          })

    // this.session.on("streamCreated", ({ stream }) => {
    //   const subscriber = this.session.subscribe(stream);
    //   this.subscribers.push(subscriber);
    // });
  },
  beforeDestroy() {
    window.removeEventListener("beforeunload", this.leaveSession);
    window.removeEventListener("backbutton", this.leaveSession);
  },

  mounted() {
    this.OV = new OpenVidu();
    this.session = this.OV.initSession();

    this.session.on("streamCreated", ({ stream }) => {
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
      message.from = event.from.data.split('":"')[1].slice(0, -7);
      message.text = event.data;

      this.messages.push(message);
    });

    this.session
      .connect(this.token, { name: this.userName, type : this.type, userSeq : this.userSeq})
      .then(() => {
        let publisher = this.OV.initPublisher(undefined, {
          audioSource: undefined, // The source of audio. If undefined default microphone
          videoSource: undefined, // The source of video. If undefined default webcam
          publishAudio: true, // Whether you want to start publishing with your audio unmuted or not
          publishVideo: true, // Whether you want to start publishing with your video enabled or not
          resolution: "320x240", // The resolution of your video
          frameRate: 30, // The frame rate of your video
          insertMode: "APPEND", // How the video is inserted in the target element 'video-container'
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
        .then((res) => {
          console.log(res);
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
      console.log("클릭한사람정보")
      console.log(stream)
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
