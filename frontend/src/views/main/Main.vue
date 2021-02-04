<template>
  <div id="main">
    <!-- 면접관/관리자와 면접자의 메인 페이지. 신분에 따라서 보여지는 컴포넌트가 다르다. -->
    <v-container>
      <div>
        <h1>면접관 페이지</h1>
        <!--@@@@@@@@@@@@@@@@@@@@@여기에 컴포넌트들 for문으로 등록해야함 -->
        <!--면접관의 경우 컴포넌트를 여러 개 사용한다. 다양한 공고의 면접관/관리자 로 들어갈 수 있기 때문이다.  -->
        <ViewerRecruitItem :interview="interview" :user="user" />
      </div>

      <!--
      <div v-else-if="whoLogin === 'viewee'">
        <div class="d-flex flex-column align-center">
          <v-img :src="com_logo" max-height="300" max-width="300"></v-img>
        </div>
        <VieweeRecruitItem />
      </div>
      -->
    </v-container>
  </div>
</template>

<script>
// import { mapState} from "vuex"
import { mapGetters } from "vuex"
import axios from "axios"

// const SERVER_URL = "https://localhost:8080/"
// const SERVER_URL = "https://i4a405.p.ssafy.io:8080"
const SERVER_URL = process.env.VUE_APP_SERVER_URL

import ViewerRecruitItem from "@/components/main/ViewerRecruitItem"
// import VieweeRecruitItem from "@/components/main/VieweeRecruitItem"
// import Schedule from '@/components/main/Schedule'

export default {
  name: "Main",
  components: {
    ViewerRecruitItem,
    // VieweeRecruitItem,
    // Schedule,
  },
  data: function() {
    return {
      user: {},
      interview: {},

      // temp_list: [1, 2],

      com_logo:
        "https://r1.community.samsung.com/t5/image/serverpage/image-id/616190iB4F850C825C2D0CD/image-size/large?v=1.0&px=999",
    }
  },
  methods: {},
  created: function() {
    // if (this.viewerLogin) {
    //   this.$router.push({ name: "ViewerRecruitItem" })
    // }
    this.user = this.getUser
    // 면접관 이메일 가려서 보내고싶은디
    axios
      .get(`${SERVER_URL}/interviewer/getMyInterview`, {
        params: {
          userComName: this.user.userComName,
          interviewerEmail: this.user.userEmail,
        },
      })
      .then((res) => {
        this.interview = res.data.interview
      })
      .catch((err) => {
        console.log(err)
      })
  },
  mounted() {},
  computed: {
    ...mapGetters(["getUser"]),
  },
}
</script>

<style></style>
