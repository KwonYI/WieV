<template>
  <div id="home" class="main-bg-navy">
    <v-row justify="center" class="pt-10" no-gutters>
      <v-col cols="8" class="main-box text-center">
        <div class="d-flex flex-column justify-center text-white">
          <v-col class="wiev-box">
            <v-col class="text-h5 mb-3">View Everywhere</v-col>
            <v-col>비대면 화상면접 플랫폼</v-col>
            <span class="text-h3">WieV</span>
          </v-col>

          <!-- 로그인 시, 면접스케줄 현황 -->
          <div v-if="getAccessToken" class="mt-10">
            <!-- 로그인 완료 -->
            <div v-if="getUserViewWait === -1">
              <h1>인사담당자 페이지</h1>
              <Schedule />
            </div>
            <div v-else>
              <router-link :to="{ name: 'Main'}">
                  대기실 이동하기
              </router-link>
            </div>
          </div>

          <!-- 미로그인 시 -->
          <v-col v-else class="login-box mt-5">
            <v-col class="login-input mb-4">
              <v-simple-table dark dense style="background-color: transparent">
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
                    <td>이메일</td>
                    <td>
                      <v-text-field label="email" v-model="credentials.userEmail" hide-details></v-text-field>
                    </td>
                  </tr>
                  <tr>
                    <td>비밀번호</td>
                    <td>
                      <v-text-field label="password" v-model="credentials.userPassword" hide-details></v-text-field>
                    </td>
                  </tr>
                </tbody>
              </v-simple-table>
            </v-col>
            <v-col class="subtitle-1"  @click="login">
              로그인
            </v-col>
            
            <v-col>
              <router-link :to="{ name: 'Signup' }">
                회원가입
              </router-link>
            </v-col>
          </v-col>
          
          
          <div style="height: 400px"></div>
        </div>
      </v-col>
    </v-row>
  </div>
</template>

<script>
  import { mapGetters } from "vuex"
  // import axios from 'axios' 

  import Schedule from "@/components/main/Schedule"

  export default {
    name: "Home",
    components: {
      Schedule,
    },
    data: () => ({
      credentials: {
        // 로그인 - 유저 이메일
        userEmail: "",
        // 로그인 - 유저 비밀번호
        userPassword: "",
      },
      message: "",
    }),
    computed: {
      ...mapGetters(["getUser", "getAccessToken", "getUserViewWait"]),
    },
    methods: {
      login: function () {
        this.$store
        .dispatch("LOGIN", this.credentials)
        .then(() => {this.$router.replace(this.$router.currentRoute) 
        this.credentials.userEmail = ""
        this.credentials.userPassword = ""
        
        
        })
        .catch(({ message }) => (this.msg = message))
        
      },
      
    },
  }
</script>

<style scoped>
  #home {
    height: 100;
  }

  .login-input {
    height: 6rem;
  }

  tr * {
    padding: 0;
  }

  td {
    font-size: 2px;
  }
</style>