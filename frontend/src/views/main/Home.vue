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
              <router-link :to="{ name: 'Main' }">
                대기실 이동하기
              </router-link>
            </div>
          </v-col>

          <!-- 미로그인 시 -->
          <v-col v-else cols="5" class="login-box mt-5" @keyup.enter="login">
            <v-col class="login-input mb-4">
              <v-simple-table dark style="background-color: transparent">
                <!-- <form>
                  <v-form-group>
                    <label for="userEmail">이메일</label>
                    <v-form-input id="userEmail" v-model="credentials.userEmail" required></v-form-input>
                  </v-form-group>
                  <v-form-group>
                    <label for="userPassword">비밀번호</label>
                    <v-form-input id="userPassword" v-model="credentials.userPassword" required></v-form-input>
                  </v-form-group>
                </form> -->
                <tbody>
                  <tr>
                    <td class="pt-3">이메일</td>
                    <td>
                      <v-text-field
                        label="email"
                        v-model="credentials.userEmail"
                        hide-details
                      >
                      </v-text-field>
                    </td>
                  </tr>
                  <tr>
                    <td class="pt-3">비밀번호</td>
                    <td>
                      <v-text-field
                        label="password"
                        v-model="credentials.userPassword"
                        type="password"
                        hide-details
                      >
                      </v-text-field>
                    </td>
                  </tr>
                </tbody>
              </v-simple-table>
            </v-col>
            <v-col class="subtitle-1" @click="login"> 로그인 </v-col>

            <v-col>
              <router-link :to="{ name: 'Signup' }"> 회원가입 </router-link>
            </v-col>
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
    credentials: {
      userEmail: "",
      userPassword: "",
    },
    message: "",
  }),
  computed: {
    ...mapGetters(["getUser", "getAccessToken", "getUserViewWait"]),
  },
  methods: {
    login: function () {
      this.$store.dispatch("LOGIN", this.credentials).then(() => {
        this.$router.replace(this.$router.currentRoute);
        this.credentials.userEmail = "";
        this.credentials.userPassword = "";
      });
    },
  },
};
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