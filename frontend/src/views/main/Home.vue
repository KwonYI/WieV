<template>
  <div id="home">
    <v-row justify="center" class="pt-10" no-gutters>
      <v-col cols="8" class="main-box text-center">
        <div class="d-flex flex-column justify-center align-center text-white">
          <v-col class="wiev-box">
            <v-col :class="[getAccessToken ? '' : 'text-h5']"
              >View Everywhere</v-col
            >
            <v-col v-if="!getAccessToken" class="mt-3"
              >비대면 화상면접 플랫폼</v-col
            >
            <span :class="[getAccessToken ? 'text-h4' : 'text-h3']">WieV</span>
          </v-col>

          <!-- 로그인 시, 면접스케줄 현황 -->
          <v-col v-if="getAccessToken" class="mt-10">
            <!-- 로그인 완료 -->
            <div v-if="getUserViewWait === -1">
              <Schedule />
            </div>
            <div v-else>
              <router-link :to="{ name: 'Main', params: { isLogin: true } }">
                대기실 이동하기
              </router-link>
            </div>
          </v-col>
        </div>
      </v-col>
    </v-row>
  </div>
</template>

<script>
import { mapGetters } from "vuex";

import Schedule from "@/components/main/Schedule";

export default {
  name: "Home",
  components: {
    Schedule,
  },
  data: () => ({
    message: "",
  }),
  computed: {
    ...mapGetters(["getUser", "getAccessToken", "getUserViewWait"]),
  },
  methods: {
  },
}
</script>

<style scoped>
tr * {
  padding: 0;
  /* margin-bottom: 2rem; */
}

table tbody tr:hover {
  background: inherit !important;
}

td {
  border: none !important;
}
</style>