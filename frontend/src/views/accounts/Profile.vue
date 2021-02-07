<template>
  <div id="profile">
    <v-container>
      <v-row class="h-100">
        <v-col col="6">
          <div>
            <h2 class="text-center m-5">{{ user.userComName }}</h2>
            <v-img :src="user.userComLogo" max-height="150" max-width="250" class="m-4"></v-img>
            <h4>주소 : {{ user.userComAddress }}</h4>
            <h4>홈페이지 : {{ user.userComHomepage  }}</h4>
          </div>
        </v-col>

        <v-col col="6">
          <div class="">
            <h2 class="text-center m-5">Profile</h2>
            <v-btn class="ma-2" text icon color="red lighten-2">
              <!-- <v-icon>mdi-cog</v-icon> -->
            </v-btn>
            <div class="">
              <h4>이메일 : {{ user.userEmail }}</h4>
              <h4>이름 : {{ user.userName }}</h4>
              <h4>연락처 : {{ user.userPhone }}</h4>
              
            </div>
          </div>

          <!-- <div class="text-center">
            <v-btn rounded color="primary" dark> 웹 화면 테스트 </v-btn>
          </div> -->
        </v-col>
      </v-row>
      <v-row v-if="user.userViewWait === -1">
        <div>
          <router-link :to="{ name: 'ProfileUpdateForm' }">
          수정하기
          </router-link>
        </div>
        <div @click="userDelete">탈퇴하기</div>
      </v-row>
    </v-container>
  </div>
</template>

<script>

import {
    mapState,
  } from "vuex";


export default {
  name: "Profile",
  data: function () {
    return {
      com_name: "버즈글로벌",
      com_logo:
        "https://r1.community.samsung.com/t5/image/serverpage/image-id/616190iB4F850C825C2D0CD/image-size/large?v=1.0&px=999",
      com_address: "서울특별시 강남구 테헤란로",
      com_homepage: "www.naver.com",
      hr_email: "elsa@buzglobal.co.kr",
      hr_phone: "01074965575",
      hr_create_date: "2021-01-27",
      hr_certified: "인증완료",
    }
  },
  created: function () {
    // this.reno = this.$route.params.recruitNo
    this.reno = this.$store.state.selectedRecruitNo
    console.log("reno:", this.reno)
  },

  computed: {
    ...mapState(['user']),

  },
  methods: {
    userDelete: function() {
      this.$store
        .dispatch("USER_DELETE")
        .then(() => {
          console.log("회원탈퇴")
          this.$router.replace({ name: "Home" })
        });
    }
  },
};
</script>

<style>
#profileRow {
  height: 30vh;
}
</style>