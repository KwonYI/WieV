<template>
  <div id="VieweeRecruitItem">
    <!--지원자 메인페이지의 공고리스트-->

    <v-card class="mx-auto mt-10 mb-10 rounded-b-lg" max-width="1000">
      <v-container>
        <v-row class="d-flex align-center">
          <v-col cols="3">
            <div class="h2 mx-5">
              {{ intervieweeData.company.comName }}
            </div>
          </v-col>
          <v-col cols="3" class="d-flex flex-column align-center">
            <v-card-title
              >{{ intervieweeData.recruit.reYear }}
              {{ intervieweeData.recruit.reFlag }}
              {{ intervieweeData.recruit.reStatus }}
            </v-card-title>
            <v-card-subtitle>
              {{ intervieweeData.recruit.reStartDate }} ~
              {{ intervieweeData.recruit.reEndDate }}
            </v-card-subtitle>
          </v-col>
          <v-col cols="6" class="d-flex flex-column align-center">
            <v-card-subtitle class="red--text">
              * 면접이 여러 개 일 경우, 반드시 안내되는 순서로 입장 바랍니다.
              <br />* 대기실/면접실에서 새로고침시 창이 종료되니 주의하시기
              바랍니다.
            </v-card-subtitle>
            <v-card-title
              >{{ intervieweeData.user.applyName }}님의 입장 순서</v-card-title
            >
            <div class="d-flex">
              <v-card-subtitle
                v-for="(interview, index) in intervieweeData.interviews"
                :key="index"
                >{{ index + 1 }}. {{ interview.interviewType }} 면접 ▶
              </v-card-subtitle>
              <v-card-subtitle> 면접종료 </v-card-subtitle>
            </div>
          </v-col>
        </v-row>
        <v-row>
          <v-expand-transition>
            <div class="blue-grey darken-1 pb-5 rounded-b-lg">
              <v-card
                class="mx-auto m-3"
                max-width="1000"
                v-for="(interview, index) in intervieweeData.interviews"
                :key="index"
              >
                <v-card-text class="d-flex justify-space-around align-center">
                  <div class="text--primary">{{ index + 1 }}</div>

                  <div class="text--primary">
                    {{ interview.groupDate }}
                  </div>
                  <div class="text--primary">
                    {{ interview.groupStartTime }} : 00
                  </div>
                  <div class="text--primary">
                    {{ interview.partName }} - {{ interview.careerName }}
                  </div>
                  <div class="text--primary">
                    {{ interview.interviewType }}
                  </div>
                  <!-- <div class="text--primary">
                    {{ group.type_name }}
                  </div> -->
                  <v-btn
                    color="blue lighten-3 yellow--text"
                    @click="goSession(interview.waitSessionName)"
                  >
                    대기실 입장
                  </v-btn>
                  <!-- <div class="text--primary">
                    {{ group.check_terminate }}
                  </div> -->
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
  name: "VieweeRecruitItem",
  props: {
    intervieweeData: {
      type: Object,
    },
  },
  data: function () {
    return {
      // re_data: [1, 2, 3],
      // show: false,
      // com_name: "버즈글로벌",
      // re_year: 2021,
      // re_flag: "상반기",
      // re_status: "신입",
      // re_startdate: "2021-08-07",
      // group_date: "20-08-07",
      // group_start_time: "14:00",
      // ca_name: "마케팅",
      // apply_name: "김지원",
      // group_data: [
      //   //order_seqy 에 따라 배치되어야 합니다.
      //   { type_name: "PT면접", check_terminate: "면접 미완료" },
      //   { type_name: "직무면접", check_terminate: "면접 미완료" },
      //   { type_name: "그룹면접", check_terminate: "면접 미완료" },
      // ],
      applicantName: "",
    };
  },
  created: function () {},
  methods: {
    goSession(session) {
      console.log(this.intervieweeData.user.applyName);
      axios
        .get(`${SERVER_URL}/session/join`, {
          params: {
            applicantName: this.intervieweeData.user.applyName,
            sessionName: session,
          },
        })
        .then((res) => {
          console.log(res);
          // this.$router.push({
          //   name: "WaitRoom",
          //   params: { interview: this.interview, interviewer: res.data },
          // });
          let routeData = this.$router.resolve({
            name: "WaitRoom",
            query: {
              comName: this.intervieweeData.company.comName,
              re_year: this.intervieweeData.recruit.reYear,
              re_flag: this.intervieweeData.recruit.reFlag,
              re_status: this.intervieweeData.recruit.reStatus,
              sessionName: res.data.sessionName,
              token: res.data.token,
              userName: res.data.applicantName,
              type: res.data.type,
            },
          });
          window.open(routeData.href, "_blank");
        })
        .catch((err) => {
          console.log(err);
          alert("방이 아직 개설되지 않았습니다.");
        });
    },
  },
};
</script>

<style>
</style>