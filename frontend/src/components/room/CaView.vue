<template>
  <div id="caview">
    <v-row style="height: 100%">

      <v-col cols="9" class="main-box">
        <!-- 메인 상단 - 면접실 이름, 타이머 -->
        <v-row class="main-banner">
          <!-- 면접실 이름 -->
          <v-col cols="2" class="d-flex align-center">
            <h5 class="mb-0 font-weight-bold">직무 면접실</h5>
          </v-col>
          <v-col cols="3"></v-col>
          <!-- 타이머 -->
          <v-col cols="7" class="centering justify-start">
            <v-card
              elevation="3"
              height="70%"
              class="centering"
              :class="isViewee ? 'd-none' : ''"
              tile
            >
              <span class="subtitle-1 px-4">
                남은 시간 {{ minutes }} : {{ seconds }}
              </span>
              <!-- <span v-for="(item, i) in timerBtn" :key="i">
                <v-btn v-if="item.if" color="#757575" height="100%" tile dark @click="item.click">
                  {{ item.name }}
                </v-btn>
              </span> -->
              <v-btn v-if="!timer" color="#757575" height="100%" tile dark @click="startTimer">
                시작
              </v-btn>
              <v-btn v-else color="#757575" height="100%" tile dark @click="stopTimer">
                정지
              </v-btn>
              <v-btn v-if="resetButton" color="#757575" height="100%" tile dark @click="resetTimer">
                리셋
              </v-btn>
              <v-btn v-if="!timer" color="#757575" height="100%" tile dark @click="editTimer">
                시간 설정
              </v-btn>
              
            </v-card>
            <v-col v-if="edit" cols="2" class="d-flex pa-0" style="height: 70%">
              <v-text-field
                v-model="inputMin"
                solo
                height="100%"
                max-width="70px"
                type="number"
                label="분"
                class="time-input"
                tile
                hide-details
              ></v-text-field>
              <v-text-field
                v-model="inputSec"
                solo
                height="100%"
                max-width="70px"
                type="number"
                label="초"
                class="time-input"
                tile
                hide-details
              ></v-text-field>
            </v-col>
          </v-col>
        </v-row>

        <!-- 메인 중앙 - 면접관, 지원자 화면 -->
        <v-row class="main-screen">
          <!-- 면접관 -->
          <v-col cols="4" class="viewer-box centering flex-column">
            <div v-for="i in viewer" :key="i" class="brd screen-res-sm">면접관 {{i}}</div>
            <!-- <!-- v-for="sub in subscribers" :key="sub.stream.connection.connectionId">
              <user-video
                v-if="JSON.parse(sub.stream.connection.data.split('%/%')[0])['type'] !== 'viewee'"
                :stream-manager="sub" 
                @click.native="updateMainVideoStreamManager(sub)"
              />
            </!--> -->
          </v-col>
          <!-- 지원자 -->
          <v-col v-if="!isViewee" cols="8" class="viewee-box centering flex-wrap">
            <div v-for="i in viewee" :key="i" :class="[viewee === 1 ? `screen-res-md` : `screen-res-sm`, 'brd']">지원자 {{i}}</div>
            <!-- <span v-for="sub in subscribers" :key="sub.stream.connection.connectionId">
            <div v-if="!isViewee">
              
            </div>
              <user-video
                v-if="JSON.parse(sub.stream.connection.data.split('%/%')[0])['type'] === 'viewee'"
                :stream-manager="sub" 
                @click.native="updateMainVideoStreamManager(sub)"
              />
            </span> -->
          </v-col>
          <v-col v-else cols="8" class="viewee-box centering flex-wrap">
            <div class="screen-res-md brd">나</div>
          </v-col>
        </v-row>
      </v-col>

      <!-- 우측 중앙 - 기능 탭 -->
      <v-col cols="3" class="sub-box">
        <!-- 대기실 - 면접실 메신저 -->
        <div :class="isViewee ? 'hidden' : ''" class="pb-2 mb-2 centering justify-space-between" style="height: 10%">
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
        <!-- 자기소개서 -->
        <v-card v-if="!isViewee" style="height: 90%">
          <v-tabs
            v-model="letterTab"
            slider-color="#304B61"
            class="letterTab"
            center-active
          >
            <v-tab
              v-for="(item, i) in letterList"
              :key="i"
            >
              질문 {{ i+1 }}
            </v-tab>
          </v-tabs>
          <v-card-text class="">
            <v-tabs-items v-model="letterTab">
              <v-tab-item v-for="(item, i) in letterList" :key="i">
                {{ i+1 }}. {{ item.quest }}
                <v-divider></v-divider>
                <v-virtual-scroll
                  item-height="250"
                  :items="[item]"
                >
                  {{ item.answer }}
                </v-virtual-scroll>
              </v-tab-item>
            </v-tabs-items>
            <v-textarea
              label="Memo"
              outlined
              rows="2"
              row-height="3"
              style="margin-top: .5rem;"
              no-resize
              hide-details
            ></v-textarea>
          </v-card-text>
        </v-card>
        <v-card v-else style="height: 90%"></v-card>
        <!-- 평가 -->

      </v-col>

    </v-row>
  </div>
</template>


<script>
import { OpenVidu } from "openvidu-browser"
// import UserVideo from "@/components/room/UserVideo"
import axios from "axios"


const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  name: "CaView",
  components: {
    // UserVideo,
  },
  data: function () {
    return {
      isViewee: false,

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

      // 인원 수 
      viewer: 2,
      viewee: 4,

      // 타이머
      // timerBtn: [
      //   { name: '시작', if: !this.timer, click: this.startTimer },
      //   { name: '정지', if: this.timer, click: this.stopTimer },
      //   { name: '리셋', if: this.resetButton, click: this.resetTimer },
      //   { name: '시간 설정', if: !this.timer, click: this.editTimer },
      // ],

      timer: null,
      inputMin: 0,
      inputSec: 0,
      time: 0,
      resetButton: false,
      edit: false,

      // 탭
      letterList: [
        {
          'quest' : '애국가',
          'answer' : '동해 물과 백두산이 마르고 닳도록 하느님이 보우하사 우리나라 만세. 무궁화 삼천리 화려 강산. 대한 사람, 대한으로 길이 보전하세.'
        },
        {
          'quest' : '미국 국가',
          'answer' : 'O say, can you see, by the dawn’s early light, What so proudly we hailed at the twilight’s last gleaming, Whose broad stripes and bright stars, through the perilous fight, O’er the ramparts we watched, were so gallantly streaming? And the rockets’ red glare, the bombs bursting in air, Gave proof through the night that our flag was still there. O say, does that star-spangled banner yet wave. O’er the land of the free and the home of the brave?'
        },
        {
          'quest' : '몽골 국가',
          'answer' : 'Дархан манай тусгаар улс, Даяар Монголын ариун голомт. Далай их дээдсийн гэгээн үйлс, Дандаа энхжиж, үүрд мөнхөжинө. Хамаг дэлхийн шударга улстай. Хамтран нэгдсэн эвээ бэхжүүлж. Хатан зориг, бүхий л чадлаараа. Хайртай Монгол орноо мандуулъя. Өндөр төрийн минь сүлд ивээж. Өргөн түмний минь заяа түшиж. Үндэс язгуур, хэл соёлоо. Үрийн үрдээ өвлөн бадраая'
        },
        {
          'quest' : '원주율 1000자리',
          'answer' : '3.14159265358979323846264338327950288419716939937510582097494459230781640628620899862803482534211706798214808651328230664709384460955058223172535940812848111745028410270193852110555964462294895493038196442881097566593344612847564823378678316527120190914564856692346034861045432664821339360726024914127372458700660631558817488152092096282925409171536436789259036001133053054882046652138414695194151160943305727036575959195309218611738193261179310511854807446237996274956735188575272489122793818301194912'
        },
        {
          'quest' : '1차 공통 프로젝트 우승팀은?',
          'answer' : 'WieV! WieV!'
        },
      ],
      length: 5,
      letterTab: null,
      

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
    },

    startTimer: function() {
      //1000ms = 1 second
      this.timer = setInterval(() => this.countdown(), 1000)
      this.resetButton = true
      this.edit = false
    },
    stopTimer: function() {
      clearInterval(this.timer)
      this.timer = null
      this.resetButton = true
    },
    resetTimer: function() {
      this.time = this.totalTime
      clearInterval(this.timer)
      this.timer = null
      this.resetButton = false
    },
    editTimer: function() {
      this.edit = !this.edit
    },
    padTime: function(time){
      return (time < 10 ? '0' : '') + time
    },
    countdown: function() {
      this.time--
    }
  },

  computed: {
    totalTime() {
      return Number(this.inputMin * 60) + Number(this.inputSec)
    },
    minutes: function() {
      const minutes = Math.floor(this.time / 60)
      return this.padTime(minutes)
    },
    seconds: function() {
      const seconds = this.time - (this.minutes * 60)
      return this.padTime(seconds)
    },
    questLen() {
      return this.letterList.length
    }
  },

  watch: {
    totalTime() {
      this.time = this.totalTime
    },
    
    length (val) {
      this.tab = val - 1
    },
  }
}
</script>

<style scoped>
#caview {
  height: 100%;
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
.viewer-box {
  padding: 0;
}
.viewee-box {
  padding: 0;
  align-content: center;
}

.letterTab {
  height: 5%;
}
::v-deep .letterTab > .v-item-group {
  height: 100%;
}
.v-tab {
  min-width: initial;
  font-size: 0.7rem;
  padding: 0 0.3rem;
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

.time-input {
  height: 100%;
  border-radius: initial;
}
::v-deep .time-input > .v-input__control {
  min-height: initial;
  height: 100%;
}
::v-deep .time-input > .v-input__slot {
  padding: 0;
}
::v-deep .v-item-group > .v-slide-group__prev, ::v-deep .v-item-group > .v-slide-group__next {
  display: none;
}

::v-deep .v-input__slot > fieldset {
  height: 100%;
  top: 0;
}
::v-deep fieldset > legend {
  position: absolute;
  top: -5px;
  background: white;
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

.screen-res-sm {
  width: 288px;
  height: 162px;
}
.screen-res-md {
  width: 640px;
  height: 360px;
}


::v-deep input::-webkit-outer-spin-button,
::v-deep input::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}
</style>