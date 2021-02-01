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
          <router-link :to="{name: 'Home'}">
            WieV
          </router-link>
        </v-toolbar-title>

        <!-- 왼쪽 상단 위치 -->
        <v-toolbar-items class="srv-btn">
          <v-btn plain style="font-size: 1rem">플랫폼 소개</v-btn>
          <v-btn plain style="font-size: 1rem">문의하기</v-btn>
        </v-toolbar-items>

        <v-spacer></v-spacer>

        <!-- 오른쪽 상단 위치 -->
        <div v-if="getAccessToken">
          <v-toolbar-items class="align-center">
            <!-- 인사담당자일 경우 면접 스케줄 생성 버튼 -->
            <v-chip v-if="getAccessToken" class="mr-10" color="#FFF1C3" outlined>
              <router-link :to="{ name: 'CreateSet'}">
                면접스케줄 생성
              </router-link>
            </v-chip>
            <span>
              <router-link class="title" :to="{ name: 'Menu' }">
                WeiV Inc.
              </router-link>
            </span>
            <v-btn plain style="font-size: 1rem" @click="logout">
              로그아웃
            </v-btn>
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
          :class="{ 'main-bg-navy': accessToken === null }"
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
    <!-- <div v-if="Manager">
      <RecruitMenu />
    </div> -->
  </v-app>
</template>

<script>
// import RecruitMenu from "@/views/viewset/RecruitMenu.vue";
import { mapGetters, mapState } from "vuex"

export default {
  name: "App",
  components: {
    // RecruitMenu,
  },
  data: () => ({
    Manager: "",
  }),
  computed: {
    ...mapState(["accessToken"]),
    ...mapGetters(["getAccessToken"]),
  },
  methods: {
    logout: function () {
      this.$store
        .dispatch("LOGOUT")
        .then(() => this.$router.replace({ name: "Home" }));
      // this.$store.state.isLogin = false;
      // // this.$store.state.isViewer = false;
      // this.$router.push({ name: "Home" });
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
</style>

<style scoped>
* {
  box-sizing: border-box;
}
#wiev {
  margin-left: 1rem;
  font-size: 1.5rem;
  font-weight: 700;
  /* color: white; */
}
.srv-btn {
  font-size: 1.9rem;
  margin-left: 1rem;
}
.router-active {
    text-decoration: none !important;
    color: inherit !important;
}
</style>