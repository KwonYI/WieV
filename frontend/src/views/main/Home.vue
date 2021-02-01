<template>
  <div id="home" class="main-bg-navy">
    <v-row justify="center" class="pt-10" no-gutters>
      <v-col cols="4" class="main-box text-center">
        <div class="d-flex flex-column justify-center text-white">
          <v-col class="wiev-box">
            <v-col class="text-h5 mb-3">View Everywhere</v-col>
            <v-col>비대면 화상면접 플랫폼</v-col>
            <span class="text-h3">WieV</span>
          </v-col>
          <!-- 미로그인 시 -->
          <v-col v-if="!isLogin" class="login-box mt-5">
            <v-col class="login-input mb-4">
              <v-simple-table dark dense style="background-color: transparent">
                <tbody>
                  <tr>
                    <td>이메일</td>
                    <td>
                      <v-text-field label="email" hide-details></v-text-field>
                    </td>
                  </tr>
                  <tr>
                    <td>비밀번호</td>
                    <td>
                      <v-text-field label="password" type="password" hide-details></v-text-field>
                    </td>
                  </tr>
                </tbody>
              </v-simple-table>
            </v-col>
            <v-col class="subtitle-1" @click="login">
              로그인
            </v-col>
            
            <v-col>
              <router-link :to="{ name: 'Signup' }">
                회원가입
              </router-link>
            </v-col>
          </v-col>
          <!-- 로그인 시, 면접스케줄 현황 -->
          <div v-else class="mt-10">로그인 완료</div>
          <div v-if="isManager">
            <h1>인사담당자 페이지</h1>
            <Schedule />
          </div>
          <div v-else-if="isLogin">
            <router-link :to="{ name: 'Main'}">
                대기실 이동하기
            </router-link>
          </div>
          <div style="height: 400px"></div>
        </div>
      </v-col>
    </v-row>
  </div>
</template>

<script>
  import { mapState } from "vuex"
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
        userEmail: '',
        // 로그인 - 유저 비밀번호
        userPassword: ''
      },
    }),
    computed: {
      ...mapState(["isLogin", "isManager"]),
    },
    methods: {
      login: function () {
        // 로그인 axios
        // [BACK에게 요청] 로그인 했을 때, 인담자인지 면접관인지 값을 받아와야 해요, 회사정보(com_seq)도 주세요.
        /*
        axios.post('로그인 URL', this.credentials)
          .then(res => {
            console.log(res.data)
            this.$set(this.$store.state, 'isLogin', true)
            this.$set(this.$store.state, 'userEmail', res.data.userEmail)
            if ('채용담당자') {
              this.$set(this.$store.state, 'isManager', true)
            }
            this.$store.dispatch('getRecruits', res.data.com_seq)
          })
          .catch(err => {
            console.log(err)
          })
        */

        // this.$store.state.isLogin = true;
        this.$set(this.$store.state, "isLogin", true)

        //일단은 테스트해야해서 isViewer를 false로 해놓음
        //인담자 페이지를 보고싶으면 아래 값을 true로 변경하면 됨
        this.$set(this.$store.state, "isManager", true)
        //######여기에는 로그인 하고나서 그 사람의 신분을 확인하는 로직이 들어가야 함######
        // ### back단에서 로그인 했을 때 isViewer(boolean) 인가 데이터를 주면 좋겠어요.
        //if(res.data.isViewer)가 아니면 면접관이므로 this.$router.push({name: 'Main' })
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