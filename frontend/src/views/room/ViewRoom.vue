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
      <GrView 
        v-if="roomType === 'gr'" 
        :groupTypeSeq = 'groupTypeSeq'
        :isViewee = 'isViewee' 
        :viewees = 'viewees' 
        :viewers = 'viewers'
        :currentApplicantList = 'currentApplicantList'
        :publisher = 'publisher'
        :mainStreamManager = 'mainStreamManager'
        :messages = 'messages'
        @updateMain="updateMain"
        @sendMessage="sendMessage"
      />
      <!-- PT형 면접실 -->
      <PTView 
        v-else-if="roomType === 'pt'" 
        :groupTypeSeq = 'groupTypeSeq'
        :isViewee = 'isViewee' 
        :viewees = 'viewees' 
        :viewers = 'viewers'
        :currentApplicantList = 'currentApplicantList'
        :publisher = 'publisher'
        :mainStreamManager = 'mainStreamManager'
        :messages = 'messages'
        @updateMain="updateMain"
        @sendMessage="sendMessage"
      />
      <!-- 일반형 면접실 -->
      <CaView 
        v-else
        :groupTypeSeq = 'groupTypeSeq'
        :isViewee = 'isViewee' 
        :viewees = 'viewees' 
        :viewers = 'viewers'
        :currentApplicantList = 'currentApplicantList'
        :publisher = 'publisher'
        :mainStreamManager = 'mainStreamManager'
        :messages = 'messages'
        @updateMain="updateMain"
        @sendMessage="sendMessage"
      />
    </v-container>

    <!-- 화면 공유 기능(미구현) -->
    <!-- <div id="screen"></div> -->
    
    <!-- 메인 하단 - 환경설정 -->
    <v-bottom-navigation dark class="main-bg-navy mt-auto">
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
import { OpenVidu } from "openvidu-browser"
import CaView from "@/components/room/CaView"
import PTView from "@/components/room/PTView"
import GrView from "@/components/room/GrView"
import axios from "axios"

const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  name: "ViewRoom",
  components: {
    CaView,
    PTView,
    GrView
  },
  data: function () {
    return {
      isViewee: false,
      roomType: '',

      OV: undefined,
      session: undefined,
      mainStreamManager: undefined, // 내 비디오
      publisher: undefined, // 연결 객체
      
      // 면접관, 지원자들만 담아두는 리스트
      viewers : [],
      viewees : [],

      // 채팅
      text: "",
      messages: [],

      // 화면, 소리, 화면 공유
      audioOn: true,
      videoOn: true,

      // From SessionController
      sessionName: undefined,
      token: undefined,
      userName: "",
      type: undefined, // 대기실 관리자(manager) / 면접관(viewer) / 면접자(viewee)

      // From Main.vue
      comName: undefined,
      re_year: undefined,
      re_flag: undefined,
      re_status: undefined,
      userSeq : undefined,
      interviewType : undefined,
      groupTypeSeq : undefined,
      
      // 전체 지원자 리스트, 방에 들어온 지원자 리스트
      allApplicantList:[],
      currentApplicantList : {},      

      // 배너
      banner_dialog: false,

      // 면접 이동
      visible: false,

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

    let user_data = ['comName', 're_year', 're_flag', 're_status', 'token', 'userName', 'userSeq', 'type', 'sessionName', 'interviewType', 'groupTypeSeq']

    for (const data of user_data) {
      this[data] = this.$route.query[data]
    }

    if(this.type === 'viewee'){
      this.isViewee = true;
    }else{
      this.isViewee = false;
    }

    if(this.interviewType === 'PT'){
      this.roomType = 'pt';
    }else if(this.interviewType === '토론'){
      this.roomType = 'gr';
    }else{
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

    this.session.on("streamCreated", ({ stream }) => {

      //면접방에 해당하는 지원자 갖고오기
      axios
      .get(`${SERVER_URL}/applicant/getListBySessionName/` + this.sessionName)
      .then(res => {
        this.allApplicantList = res.data

        const subscriber = this.session.subscribe(stream);
        let info = JSON.parse(subscriber.stream.connection.data.split('%/%')[0])

        if(info['type'] === 'viewee'){
          this.viewees.push(subscriber)

          if(!this.mainStreamManager){
            this.mainStreamManager = subscriber
          }

          for (const apply of this.allApplicantList) {
            if(apply["apply-Seq"]==info["userSeq"]){
              
              this.currentApplicantList[info["userSeq"]] = [
                {
                  'quest' : '자기소개서1',
                  'answer' : apply["apply-Resume1"]
                },
                {
                  'quest' : '자기소개서2',
                  'answer' : apply["apply-Resume2"]
                },
                {
                  'quest' : '자기소개서3',
                  'answer' : apply["apply-Resume3"]
                },
                {
                  'quest' : '자기소개서4',
                  'answer' : apply["apply-Resume4"]
                },
              ]
            }
          }
        }else{
          this.viewers.push(subscriber)
        }
      })
      .catch((err) => {
          console.log(err)
          alert("세션 이름에 따른 지원자 갖고오기 실패")
      })
    });

    // Stream 삭제
    this.session.on("streamDestroyed", ({ stream }) => {

      let info = JSON.parse(stream.connection.data.split('%/%')[0])

      if(info['type'] === 'viewee'){
        const index = this.viewees.indexOf(stream.streamManager, 0);
        if (index >= 0) {
          this.viewees.splice(index, 1);
        }

        delete this.currentApplicantList[info["userSeq"]] 

        if(this.mainStreamManager.stream === stream){
          if(this.viewees.length !== 0){
            this.mainStreamManager = this.viewees[0]
          }else{
            this.mainStreamManager = null
          }
        }

      }else{
        const index = this.viewers.indexOf(stream.streamManager, 0);
        if (index >= 0) {
          this.viewers.splice(index, 1);
        }
      }

      console.log("viewee : ",this.viewees, " viewer : ", this.viewers)
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
    // })

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

    // 채팅 기능 -> 세션 동기화
    this.session.on("signal:my-chat", (event) => {
      let message = { from: "", text: "" }
      message.from = event.from.data.split('":"')[1].slice(0, -7)
      message.text = event.data

      this.messages.push(message)
    })

    this.session.connect(this.token, { name: this.userName, type : this.type, userSeq : this.userSeq})
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

        this.session.publish(this.publisher);

        let info = JSON.parse(this.publisher.stream.connection.data.split('%/%')[0])
        console.log("내 이름은 ", info['name'], " 이고", info['type'], "야")

        // if(this.type === 'viewee'){
        //   this.viewees.push(this.publisher)
        // }else{
        //   this.viewers.push(this.publisher)
        // }
      })
      .catch((error) => {
        console.log(
          "There was an error connecting to the session:", error.code, error.message);
      });

    window.addEventListener("beforeunload", this.leaveSession);
    window.addEventListener("backbutton", this.leaveSession)
  },

  methods: {
    leaveSession() {
      axios
        .get(`${SERVER_URL}/session/leaveSession`, {
          params: {
            sessionName: this.sessionName,
            token: this.token,
          },
        })
        .then(() => {
          if (this.session) {
            this.session.disconnect();
            this.session = undefined;
            this.mainStreamManager = undefined;
            this.publisher = undefined;
            this.viewers = [],
            this.viewees = [],
            this.OV = undefined;
          }

          window.close();
        })
        .catch((err) => {
          console.log(err);
        });
    },

    audioOnOOff() {
      this.audioOn = !this.audioOn;

      this.publisher.publishAudio(this.audioOn)
    },

    videoOnOOff() {
      this.videoOn = !this.videoOn;

      this.publisher.publishVideo(this.videoOn)
    },

    updateMain(stream){
      this.mainStreamManager = stream;
    },
    
    sendMessage(text) {
      this.session.signal({ data: text, to: [], type: "my-chat" })
        .then()
        .catch(err => console.error(err))
    },
  },

  computed: {
  },
}
</script>

<style scoped>
#viewroom {
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
/* .screen-res-sm {
  width: 288px;
  height: 162px;
}
.screen-res {
  width: 240px;
  height: 135px;
}
.screen-res-md {
  width: 640px;
  height: 360px;
} */
</style>
