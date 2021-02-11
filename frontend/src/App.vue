<template>
  <v-app>
    <!-- Navbar -->
    <v-card class="overflow-hidden" style="border-radius: 0">
      <v-app-bar absolute class="main-bg-navy" elevate-on-scroll dark scroll-target="#scrolling-techniques-7">
        <!-- <v-app-bar-nav-icon></v-app-bar-nav-icon> -->
        <v-toolbar-title id="wiev">
          <img :src="images.logo" width="40" alt="logo">
          <router-link :to="{name: 'Home'}">
            WieV
          </router-link>
        </v-toolbar-title>

        <!-- 왼쪽 상단 위치 -->
        <v-toolbar-items v-if="isNotRoom" class="srv-btn">
          <v-btn plain style="font-size: 1rem" href="#work">플랫폼 소개</v-btn>
          <v-btn plain style="font-size: 1rem" href="#contact">문의하기</v-btn>
        </v-toolbar-items>

        <!-- 왼쪽 상단 위치, 대기실/면접실 -->
        <v-toolbar-items v-else class="room-status">
          <v-row style="width: 700px" no-gutters>
            <v-col class="d-flex justify-center align-center">
              <div class="text-subtitle-1 px-5 py-1" style="border: 1px solid">현재 면접자 수 : 8명</div>
            </v-col>
            <v-col class="d-flex justify-center align-center">
              <div class="text-subtitle-1 px-5 py-1" style="border: 1px solid">현재 시각 : 7/10(월) 오후 5:23</div>
            </v-col>
          </v-row>
        </v-toolbar-items>

        <v-spacer></v-spacer>

        <div v-if="getAccessToken" class="h-100">
          <!-- 오른쪽 상단 위치, 로그인 후 -->
          <v-toolbar-items v-if="isNotRoom" class="align-center">
            <!-- 인사담당자일 경우 면접 스케줄 생성 버튼 -->
            <v-btn plain style="font-size: 1rem">
              <router-link :to="{ name: 'Profile' }">
                Profile
              </router-link>
            </v-btn>


            <v-btn plain style="font-size: 1rem" @click="logout">로그아웃</v-btn>
          </v-toolbar-items>
          <!-- 오른쪽 상단 위치, 대기실/면접실 -->
          <v-toolbar-items v-else class="d-flex align-center body-1 mr-5">
            WieV Inc. 2021 하반기 신입 공채
          </v-toolbar-items>
        </div>


      </v-app-bar>

      <!-- Contents -->
      <v-sheet id="scrolling-techniques-7" class="overflow-y-auto" max-height="100vh" style="height: 100%">
        <v-container class="p-0 m-0 pt-16" :class="[!accessToken || isHome ? 'main-bg-navy-img' : '']"
          style="min-height: 100vh; max-width: initial; height: 100%">
          
          <router-view></router-view>
        </v-container>
        <!-- <v-parallax
          dark
          class="mt-16"
          :src="require('@/assets/bg_image.png')"
          style="min-height: 100vh"
        >
        </v-parallax> -->


      </v-sheet>
    </v-card>

<!--이거 Home에 두면 부드럽게 안내려감.. 그래서 app.vue에 넣음-->
    <Introduce v-if="isHome"/>
    <Ask v-if="isHome"/>

     <footer class="bg-dark py-4 mt-5 tf-footer">
    <div class="container text-light">
      <div class="row">
        <div class="col-md-6 col-sm-12">&copy; Wiev. All rights reserved.</div>
        <div class="col-md-6 col-sm-12 text-right tf-design">Design - <a href="https://templateflip.com"
            target="_blank">SSAFY A405</a></div>
      </div>
    </div>
  </footer>





  </v-app>
</template>

<script>
  import {
    mapGetters,
    mapState
  } from "vuex"
  // import parallax from "@/assets/js/parallax.js"

  import Ask from '@/components/main/Ask'
  import Introduce from '@/components/main/Introduce'

  export default {
    name: "App",
    components: {
      Ask, Introduce
        
    },
    data: () => ({
      Manager: "",
      // menuBar: [
      //   { title: '기업 정보', link: 'Profile' }
      // ],
      images: {
        logo: require('@/assets/images/logo.png')

      }
    }),
    computed: {
      ...mapState(["accessToken", 'user']),
      ...mapGetters(["getAccessToken"]),
      isNotRoom() {
        if (['WaitRoom', 'ViewRoom', 'Main'].includes(this.$route.name)) {
          return false
        } else {
          return true
        }
      },
      isHome() {
        if (this.$route.name === 'Home') {
          return true
        } else {
          return false
        }
      }
    },
    methods: {
      logout: function () {
        this.$store.dispatch("LOGOUT")
          .then(() => this.$router.replace({
            name: "Home"
          }))
      },

      //개행메소드
      handleNewLine(str) {
        return String(str).replace(/(?:\r\n|\r|\n)/g, "</br>");
      }
    },
    created: function () {
      this.Manager = this.$store.state.Manager
      console.log("this.", this.$route.name)
    },
  }
</script>

<style>
  @import url(https://cdn.jsdelivr.net/gh/moonspam/NanumSquare@1.0/nanumsquare.css);
  @import "./assets/css/weivmain.css";
  /* @import "./assets/css/main.css"; */
  /* @import "./assets/css/bootstrap.min.css"; */


  * {
    font-family: "NanumSquare", sans-serif;
  }

  body {
    height: 100%;
  }

  .v-parallax__image-container img {
    /* max-width: 100%; */
  }

  .v-parallax__content {
    padding: 0 !important;
  }
</style>

<style scoped>
  * {
    box-sizing: border-box;
  }

  #wiev {
    margin-left: 1rem;
    font-size: 1.5rem;
    font-weight: 700;
  }

  .srv-btn {
    font-size: 1.9rem;
    margin-left: 1rem;
  }

  .room-status {
    margin-left: 7rem;
  }
  

</style>