<template>
  <div id="ViewerRecruitItem">
    <!--면접관 메인페이지의 공고리스트-->
    <v-card class="mx-auto mt-10 mb-10 rounded-b-lg" max-width="1000">
      <v-container>
        <v-row>
          <v-col cols="3" class="d-flex align-center">
            <div class="h2 mx-5">{{ interview.comName }}</div>
          </v-col>

          <v-col cols="9">
            <v-card-title>
              {{ interview.recruitYear }}
              {{ interview.recruitFlag }}
              {{ interview.recruitStatus }}
            </v-card-title>
            <v-card-subtitle>
              {{ interview.recruitStartDate }} ~
              {{ interview.recruitEndDate }}
            </v-card-subtitle>
          </v-col>
        </v-row>
        <v-row>
          <!-- <v-divider></v-divider>  -->
          <v-expand-transition>
            <!-- <div v-show="show"> -->
            <div class="blue-grey darken-1 pb-5 rounded-b-lg">
              <v-card class="mx-auto m-3" max-width="1000">
                <v-card-text class="d-flex justify-space-around align-center">
                  <!-- <div class="text--primary">{{ this.index }}</div> -->

                  <div class="text--primary">
                    {{ interview.groupDate }}
                  </div>
                  <!-- 시간이 조금 이상하게 생겼다 -->
                  <div class="text--primary">
                    {{ interview.groupStartTime }} : 00
                  </div>
                  <div class="text--primary">
                    {{ interview.partName }} - {{ interview.careerName }} 직군
                  </div>
                  <div class="text--primary">
                    {{ interview.interviewType }} 면접
                  </div>
                  <v-btn
                    color="blue-grey darken-1"
                    @click="goWaitSession"
                  >
                    <span style="color: #FFF1C3">대기실 입장</span>
                  </v-btn>

                  <v-btn
                    color="blue-grey darken-1"
                    @click="goInterviewSession"
                  >
                    <span style="color: #FFF1C3">면접실 입장</span>
                    <!-- 
                    :disabled="inWait === true"
                    :disabled="inInterview === true"
                     -->
                  </v-btn>
                </v-card-text>
              </v-card>
            </div>
          </v-expand-transition>
        </v-row>
      </v-container>
    </v-card>
  </div>
</template>

<script>
import axios from "axios";
const SERVER_URL = process.env.VUE_APP_SERVER_URL;

export default {
  name: "ViewerRecruitItem",
  props: {
    interview: {
      type: Object,
    },
    user: {
      type: Object,
    },
    groupTypeSeq :{
      type: Number
    }
  },
  data: function () {
    return {
    };
  },
  created() {
    console.log("현재 유저를 보여줍니다(ViewerRecruit)", this.user)
  },
  methods: {
    goWaitSession() {
      axios.get(`${SERVER_URL}/session/create`, {
          params: {
            interviewerWait: this.user.userViewWait,
            interviewerName: this.user.userName,
            sessionName: this.interview.waitSessionName,
          },
        })
          .then(res => {
            console.log(res)
            // this.$router.push({
            //   name: "WaitRoom",
            //   params: { interview: this.interview, interviewer: res.data },
            // });
            let routeData = this.$router.resolve({
              name: "WaitRoom",
              query: {
                comName: this.interview.comName,
                re_year: this.interview.recruitYear,
                re_flag: this.interview.recruitFlag,
                re_status: this.interview.recruitStatus,
                token: res.data.token,
                userName: res.data.interviewerName,
                userSeq : this.user.userSeq,
                type: res.data.type,
                sessionName: res.data.sessionName,
                interviewSession : this.interview.interviewSessionName,
                interviewType : this.interview.interviewType,
                groupTypeSeq : this.groupTypeSeq
              },
            })
            window.open(routeData.href, "_blank")
          })
          .catch(err => {
            if (this.user.userViewWait == 0) {
              console.log(err)
              alert("방 입장 실패")
            } else {
              alert("방이 아직 개설되지 않았습니다.")
            }
        })
    },

    goInterviewSession() {
      axios.get(`${SERVER_URL}/session/create`, {
          params: {
            interviewerWait: this.user.userViewWait,
            interviewerName: this.user.userName,
            sessionName: this.interview.interviewSessionName,
          },
        })
          .then(res => {
            console.log(res);
            let routeData = this.$router.resolve({
              name: "ViewRoom",
              query: {
                comName: this.interview.comName,
                re_year: this.interview.recruitYear,
                re_flag: this.interview.recruitFlag,
                re_status: this.interview.recruitStatus,
                token: res.data.token,
                userName: res.data.interviewerName,
                userSeq : this.user.userSeq,
                type: res.data.type,
                sessionName: res.data.sessionName,
                interviewType : this.interview.interviewType,
                groupTypeSeq : this.groupTypeSeq
              },
            })
            window.open(routeData.href, "_blank")
          })
          .catch(err => {
            if (this.user.userViewWait == 0) {
              console.log(err)
              alert("방 입장 실패")
            } else {
              alert("방이 아직 개설되지 않았습니다.")
            }
          })
    },
  },
  computed: {
  },
}
</script>

<style></style>
