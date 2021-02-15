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

        <!-- 메인 위 - 지원자들 화면, 클릭하면 큰 화면으로 보기 -->
        <v-row>
          <div v-if="!isViewee" class='d-flex justify-center'>
            <user-video v-for="sub in viewees" :key="sub.stream.connection.connectionId"
              class= 'screen-res'
              :stream-manager="sub" 
              :id = "sub.stream.connection.connectionId"
              @click.native="updateMain(sub)"
            />
          </div>
        </v-row>

        <!-- 메인 중앙 - 면접관, 지원자 화면 -->
        <v-row class="main-screen">
          <!-- 면접관 -->
          <v-col cols="4" class="viewer-box centering flex-column">
            <span v-if="!isViewee">
              <user-video :stream-manager="publisher" class="screen-res-sm"/>
            </span>
            <span v-for="sub in viewers" :key="sub.stream.connection.connectionId">
              <user-video
                class="screen-res-sm"
                :stream-manager="sub" 
              />
            </span>
          </v-col>
          <!-- 지원자 -->
          <v-col cols="8" class="viewee-box centering flex-wrap">
            <span v-if="isViewee">
              <user-video :stream-manager="publisher" class="screen-res-lr"/>
            </span>
            <span v-else>
              <user-video :stream-manager="mainStreamManager" class="screen-res-md"/>
            </span>
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
                v-model="messageToSession"
              ></v-textarea>
              <v-btn @click="sendToSession" text color="secondary">Send</v-btn>
            </template>
          </a-popover>
          <!-- 메시지 출력 -->
          <v-sheet color="white" height="100%" width="60%" elevation="3" class="d-flex justify-center align-center">
            <!-- <div>면접 준비 완료</div> -->
            <div>{{messageFromSession}}</div>
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
              v-for="(item, i) in currentApplicantList[clickedSeq]"
              :key="i"
            >
              <span v-if="item.quest !== '자기소개서'">
                질문 {{ i+1 }}
              </span>
              <span v-else>
                자기소개서
              </span>
            </v-tab>
          </v-tabs>
          <v-card-text class="">
            <v-tabs-items v-model="letterTab">
              <v-tab-item v-for="(item, i) in currentApplicantList[clickedSeq]" :key="i">
                {{ i+1 }}. {{ item.quest }} // {{clickedSeq}}번 지원자
                <v-divider></v-divider>
                <v-virtual-scroll
                  item-height="400"
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
import UserVideo from "@/components/room/UserVideo"
// import axios from "axios"

import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'

const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  name: "CaView",
  components: {
    UserVideo,
  },
  props: {
    groupTypeSeq :{
      type : String
    },
    isViewee: {
      type: Boolean,
    },
    viewees: {
      type: Array,
    },
    viewers: {
      type: Array,
    },
    currentApplicantList: {
      type: Object,
    },
    publisher: {
      type: Object,
    },
    mainStreamManager :{
      type : Object
    }
  },
  data: function () {
    return {
      // 인원 수 
      viewer: 2,
      viewee: 4,

      // 세션간 통신
      messageToSession : '',
      messageFromSession : '',

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
      
      clickedSeq : '',

      // questionList: [],
      // 탭
      length: 5,
      letterTab: null,
    }
  },
  created: function () {
    if(!this.isViewee){
      this.connect()
    }

    window.addEventListener("beforeunload", this.leaveSession)
    window.addEventListener("backbutton", this.leaveSession)
  },

  beforeDestroy() {
    window.removeEventListener("beforeunload", this.leaveSession)
    window.removeEventListener("backbutton", this.leaveSession)
  },

  mounted() {
    window.addEventListener("beforeunload", this.leaveSession)
  },

  methods: {

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
    },
    connect() {
      let socket = new SockJS(`${SERVER_URL}/ws-stomp`);
      this.stompClient = Stomp.over(socket);
      this.stompClient.connect(
        {},
        frame => {
          this.connected = true;
          console.log('소켓 연결 성공', frame);
          this.stompClient.subscribe("/send/"+this.groupTypeSeq, res => {
            this.messageFromSession = JSON.parse(res.body)['message']
          });
        },
        error => {
          // 소켓 연결 실패
          console.log('소켓 연결 실패', error);
          this.connected = false;
        }
      );        
    },
    sendToSession() {
      if (this.stompClient && this.stompClient.connected) {
        const msg = { 
          name: '',
          message: this.messageToSession 
        };
        this.stompClient.send("/receive/"+this.groupTypeSeq, JSON.stringify(msg), {});
      }
      this.messageToSession = '';
    },

    updateMain(stream) {
      this.clickedSeq = JSON.parse(stream.stream.connection.data.split('%/%')[0])['userSeq']
      this.$emit("updateMain", stream)
    },
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

.screen-res {
  width: 208px;
  height: 117px;
}
.screen-res-sm {
  width: 288px;
  height: 162px;
}
.screen-res-md {
  width: 640px;
  height: 360px;
}
.screen-res-lr {
  width: 720px;
  height: 405px;
}

::v-deep input::-webkit-outer-spin-button,
::v-deep input::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}
</style>