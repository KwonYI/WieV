<template>
  <div id="main">
    <!-- 면접관/관리자와 면접자의 메인 페이지. 신분에 따라서 보여지는 컴포넌트가 다르다. -->
    <v-container>
      <div v-if="isLogin === true">
        <h1>면접관 페이지</h1>
        <!--@@@@@@@@@@@@@@@@@@@@@여기에 컴포넌트들 for문으로 등록해야함 -->
        <!--면접관의 경우 컴포넌트를 여러 개 사용한다. 다양한 공고의 면접관/관리자 로 들어갈 수 있기 때문이다.  -->
        <ViewerRecruitItem v-if="isViewer" :interview="interview" :user="user" />
        <VieweeRecruitItem v-else :interview="interview" :user="user" />
      </div>
      <div v-else>
        <!-- <div class="d-flex flex-column align-center">
          <v-img :src="com_logo" max-height="300" max-width="300"></v-img>
        </div> -->
        <VieweeRecruitItem :intervieweeData="intervieweeData" />
      </div>

    </v-container>
  </div>
</template>

<script>
// import { mapState} from "vuex"
import { mapGetters } from "vuex"
import axios from "axios"

const SERVER_URL = process.env.VUE_APP_SERVER_URL

import ViewerRecruitItem from "@/components/main/ViewerRecruitItem"
import VieweeRecruitItem from "@/components/main/VieweeRecruitItem"
// import Schedule from '@/components/main/Schedule'

export default {
  name: "Main",
  components: {
    ViewerRecruitItem,
    VieweeRecruitItem,
    // Schedule,
  },
  data: function () {
    return {
      isLogin: false,
      user: {},
      interview: {},

      intervieweeData: {},

      com_logo:
        "https://r1.community.samsung.com/t5/image/serverpage/image-id/616190iB4F850C825C2D0CD/image-size/large?v=1.0&px=999",
      // 면접관, 지원자 구분
      isViewer: false,
    }
  },
  methods: {},
  created: function () {
    // if (this.viewerLogin) {
    //   this.$router.push({ name: "ViewerRecruitItem" })
    // }
    this.isLogin = this.$route.params.isLogin
    if (this.isLogin === true) {
      this.user = this.getUser
      axios.get(`${SERVER_URL}/interviewer/getMyInterview`, {
          params: {
            userComName: this.user.userComName,
            interviewerEmail: this.user.userEmail,
          },
        })
        .then((res) => {
          this.interview = res.data.interview;
        })
        .catch((err) => {
          console.log(err);
        });
    } else {
      axios
        .get(`${SERVER_URL}/applicant/mypage/` + this.$route.query.Id)
        .then((res) => {
          console.log(res);
          this.intervieweeData = res.data;
        })
        .catch((err) => {
          console.log(err);
        });
    }
  },
  mounted() {},
  computed: {
    ...mapGetters(["getUser"]),
  },
}
</script>

<style></style>
