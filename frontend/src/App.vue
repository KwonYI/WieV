<template>
  <v-app>
    <!-- Navbar -->
    <v-card class="overflow-hidden" style="border-radius: 0">
      <v-app-bar
        absolute
        class="main-bg-navy"
        elevate-on-scroll
        dark
        scroll-target="#scrolling-techniques-7"
      >
        <!-- <v-app-bar-nav-icon></v-app-bar-nav-icon> -->
        <v-toolbar-title id="wiev">
          <img :src="images.logo" width="40" alt="logo">
          <router-link :to="{name: 'Home'}">
            WieV
          </router-link>
        </v-toolbar-title>

        <!-- 왼쪽 상단 위치 -->
        <v-toolbar-items v-if="isNotRoom" class="srv-btn">
          <v-btn plain style="font-size: 1rem">플랫폼 소개</v-btn>
          <v-btn plain style="font-size: 1rem">문의하기</v-btn>
        </v-toolbar-items>
        
        <!-- 왼쪽 상단 위치, 대기실/면접실 -->
        <v-toolbar-items v-else class="room-status">
          <v-row style="width: 500px" no-gutters>
            <v-col class="d-flex justify-center align-center">현재 면접자 수 : 8명</v-col>
            <v-col class="d-flex justify-center align-center">현재 시각 : 7/10(월) 오후 5:23</v-col>
          </v-row>
        </v-toolbar-items>

        <v-spacer></v-spacer>

        <div v-if="getAccessToken" class="h-100">
          <!-- 오른쪽 상단 위치, 로그인 후 -->
          <v-toolbar-items v-if="isNotRoom" class="align-center">
            <!-- 인사담당자일 경우 면접 스케줄 생성 버튼 -->
            <v-menu open-on-hover offset-y>
              <template v-slot:activator="{ on, attrs }">
                <v-btn
                  color="blue-grey darken-3"
                  dark
                  v-bind="attrs"
                  v-on="on"
                  style="height: 50% !important"
                  rounded
                >
                  Profile
                </v-btn>
              </template>
              <v-list>
                <v-list-item
                  v-for="(item, index) in menuBar"
                  :key="index"
                >
                  <v-list-item-title>
                    <router-link :to="{ name: item.link }">
                      {{ item.title }}
                    </router-link>
                  </v-list-item-title>
                </v-list-item>
              </v-list>
            </v-menu>

            <v-btn plain style="font-size: 1rem" @click="logout">로그아웃</v-btn>
          </v-toolbar-items>
          <!-- 오른쪽 상단 위치, 대기실/면접실 -->
          <v-toolbar-items v-else class="d-flex align-center body-1 mr-5">
            WieV Inc. 2021 하반기 신입 공채
          </v-toolbar-items>
        </div>

        
      </v-app-bar>

      <!-- Contents -->
      <v-sheet
        id="scrolling-techniques-7"
        class="overflow-y-auto"
        max-height="100vh"
      >
        <v-container
          class="p-0 m-0 pt-16"
          :class="[!accessToken || isHome ? 'main-bg-navy' : '']"
          style="min-height: 100vh; max-width: initial"
        >
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
  </v-app>
</template>

<script>
import { mapGetters, mapState } from "vuex"

export default {
  name: "App",
  components: {
  },
  data: () => ({
    Manager: "",
    menuBar: [
      { title: '기업 정보', link: 'Profile' }
    ],
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
        .then(() => this.$router.replace({ name: "Home" }))
    },
  },
  created: function () {
    this.Manager = this.$store.state.Manager
  },
}
</script>

<style>
@import url(https://cdn.jsdelivr.net/gh/moonspam/NanumSquare@1.0/nanumsquare.css);
@import "./assets/css/main.css";

* {
  font-family: "NanumSquare", sans-serif;
}

.v-parallax__image-container img {
  /* max-width: 100%; */
}

.v-parallax__content{
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
  margin-left: 10rem;
}
.router-active {
  text-decoration: none !important;
  color: inherit !important;
}
</style>