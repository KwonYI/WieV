<template>
  <v-app>
    <!-- Navbar -->
    <v-card class="" style="border-radius: 0">
      <v-app-bar absolute class="main-bg-navy" elevate-on-scroll dark scroll-target="#scrolling-techniques-7" style="z-index:99; position: sticky">
        <!-- <v-app-bar-nav-icon></v-app-bar-nav-icon> -->
        <v-toolbar-title id="wiev">
          <img :src="images.logo" width="40" alt="logo">
          <router-link v-if="isNotWaitRoomViewRoom" :to="{name: 'Home'}">
            WieV
          </router-link>
          <span v-else>
            WieV
          </span>


        </v-toolbar-title>

        <!-- 왼쪽 상단 위치 -->
        <v-toolbar-items v-if="isNotRoom" class="srv-btn">
          <v-btn v-if="isHome" plain style="font-size: 1rem" @click="$vuetify.goTo('#introduce')">플랫폼 소개</v-btn>
          <v-btn v-if="isHome" plain style="font-size: 1rem" @click="$vuetify.goTo('#contact')">문의하기</v-btn>
          <!-- <v-btn plain style="font-size: 1rem" @click="$vuetify.goTo(target, options)">문의하기</v-btn> -->
        </v-toolbar-items>

        <!-- 왼쪽 상단 위치, 대기실/면접실 -->
        <v-toolbar-items v-else class="room-status">
          <v-row style="width: 700px" no-gutters>
            <v-col class="d-flex justify-center align-center">
              <!-- <div class="text-subtitle-1 px-5 py-1" style="border: 1px solid">현재 면접자 수 : 8명</div> -->
            </v-col>
            <v-col class="d-flex justify-center align-center">
              <div class="text-subtitle-1 px-5 py-1" style="border: 1px solid">현재 시각 : <span id="clock" class="text-subtitle-1 py-1"></span>
              </div>
            </v-col>
          </v-row>
        </v-toolbar-items>

        <v-spacer></v-spacer>

        <div v-if="getAccessToken" class="h-100">
          <!-- 오른쪽 상단 위치, 로그인 후 -->
          <v-toolbar-items v-if="isNotRoom" class="align-center">
            <!-- 인사담당자일 경우 면접 스케줄 생성 버튼 -->
            <v-btn plain style="font-size: 1rem" :to="{ name: 'Profile' }">Profile</v-btn>
            <v-btn plain style="font-size: 1rem" @click="logout">Logout</v-btn>
          </v-toolbar-items>
          <!-- 오른쪽 상단 위치, 대기실/면접실 -->
          <v-toolbar-items v-else class="d-flex align-center body-1 mr-5">
            {{ user.userComName }} {{ recruit.recruitYear }} {{ recruit.recruitFlag }} {{ recruit.recruitStatus }} 채용
          </v-toolbar-items>
        </div>
        <div v-else class="h-100">
          <!-- 오른쪽 상단 위치, 로그인 후 -->
          <v-toolbar-items v-if="isNotRoom" class="align-center">
            <v-dialog v-model="dialogLogin" width="500" @click:outside="loginOutside">
              <template v-slot:activator="{ on, attrs }">
                <v-btn class="ma-3" text color="lighten-2" v-bind="attrs" v-on="on">
                  <div>
                    <v-icon>mdi-account-key</v-icon> Login
                  </div>
                </v-btn>
              </template>

              <v-card @keyup.enter="login">
                <v-card-title class="headline main-bg-navy lighten-2">
                  <div>LOGIN</div>
                </v-card-title>

                <v-card-text>
                  <v-container class="d-flex flex-column" fluid>
                    <v-simple-table fixed-header class="d-flex flex-column" style="overflow-y: hidden">
                      <template v-slot:default>
                        <tbody>
                          <tr>
                            <td class="pt-3">
                              <div>
                                <v-icon>mdi-email</v-icon> 이메일
                              </div>
                            </td>
                            <td>
                              <v-text-field label="email" v-model="userCredentials.userEmail"></v-text-field>
                            </td>
                          </tr>
                          <tr>
                            <td class="pt-3">
                              <div>
                                <v-icon>mdi-key</v-icon> 비밀번호
                              </div>
                            </td>
                            <td>
                              <v-text-field label="password" v-model="userCredentials.userPassword" type="password">
                              </v-text-field>
                            </td>
                          </tr>
                        </tbody>
                      </template>
                    </v-simple-table>
                  </v-container>
                </v-card-text>

                <v-card-actions>
                  <v-spacer></v-spacer>
                  <v-btn color="primary" text @click="login">로그인</v-btn>
                  <v-dialog v-model="dialogPassword" width="500" @click:outside="findPasswordOutside">
                    <template v-slot:activator="{ on, attrs }">
                      <v-btn class="ma-3" text color="primary" v-bind="attrs" v-on="on">비밀번호 찾기</v-btn>
                    </template>

                    <v-card @keyup.enter="findPassword">
                      <v-card-title class="headline main-bg-navy lighten-2">
                        <div>비밀번호 찾기</div>
                      </v-card-title>

                      <v-card-text>
                        <v-container class="d-flex flex-column" fluid>
                          <v-form ref="form" lazy-validation>
                            <v-simple-table fixed-header class="d-flex flex-column" style="overflow-y: hidden">
                              <template v-slot:default>
                                <tbody>
                                  <tr v-for="(item, index) in userInfo" :key="index">
                                    <td>
                                      <v-icon class="mr-2">{{ item.icon }}</v-icon>{{ item.label }}
                                    </td>
                                    <td>
                                      <v-text-field :label="item.label" :type="item.type" :class="item.longmsg"
                                        v-model="credentials[item.value]" :rules="item.rule"></v-text-field>
                                    </td>
                                  </tr>
                                </tbody>
                              </template>
                            </v-simple-table>
                          </v-form>
                        </v-container>
                      </v-card-text>

                      <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn color="primary" text @click="findPassword">임시 비밀번호 발급</v-btn>
                      </v-card-actions>
                    </v-card>
                  </v-dialog>
                  <v-btn color="primary" text @click="dialogLogin=false" :to="{ name: 'Signup' }">회원가입</v-btn>
                </v-card-actions>
              </v-card>
            </v-dialog>
            <v-btn color="white" text @click="dialogLogin=false" :to="{ name: 'Signup' }">
              <div>
                <v-icon>mdi-account-plus</v-icon> Sign Up
              </div>
            </v-btn>
          </v-toolbar-items>
        </div>
      </v-app-bar>


      <!-- Contents -->
      
      <v-responsive id="scrolling-techniques-7" class="" style="overflow: initial">
        <v-container class="p-0 m-0" style="max-width: initial">
          <div id="top"></div>
          <router-view transition transition-mode="out-in" />
        </v-container>

        <!-- <Introduce v-if="isHome"/>
        <Ask v-if="isHome"/> -->
        
         <!-- :style="isHome ? '' :  " -->
        <footer v-if="isFooterView" class="bg-dark py-4">
          <div class="container text-light">
            <div class="row">
              <div class="col-md-6 col-sm-12">&copy; WieV Inc. 2021 All rights reserved.</div>
              <div class="col-md-6 col-sm-12 text-right tf-design">Design - <a href="https://templateflip.com"
                  target="_blank">SSAFY A405</a></div>
            </div>
          </div>
        </footer>
      </v-responsive>
    </v-card>





    <!--이거 Home에 두면 부드럽게 안내려감.. 그래서 app.vue에 넣음-->

    <!-- <Ask/>
      <Introduce/>
 -->




<!--이거 Home에 두면 부드럽게 안내려감.. 그래서 app.vue에 넣음-->

  </v-app>
</template>

<script>
import { mapGetters, mapState } from "vuex"
import axios from "axios"
// import Ask from '@/components/main/Ask'
// import Introduce from '@/components/main/Introduce'
const SERVER_URL = process.env.VUE_APP_SERVER_URL

// import Router from 'vue-router'
// import goTo from 'vuetify/es5/services/goto'

export default {
  name: "App",
  components: {
    // Ask, Introduce
  },
  data: function () {
    return {
      dialogLogin: false,
      dialogPassword: false,

      Manager: "",
      images: {
        logo: require('@/assets/images/logo.png')
      },
      userCredentials: {
        userEmail: "",
        userPassword: "",
      },

      userInfo: [
        {
          label: "이름",
          type: "text",
          value: "hrName",
          icon: "mdi-account",
          rule: [
            value => !!value || '이름은 필수 항목입니다.',
          ],
          longmsg: ''
        },
        {
          label: "이메일",
          type: "text",
          value: "hrEmail",
          icon: "mdi-email",
          rule: [
            value => !!value || '이메일은 필수 항목입니다.',
            value => /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/.test(
              value) || '이메일 형식이 유효하지 않습니다.',
          ],
          longmsg: ''
        },
        {
          label: "연락처",
          type: "text",
          value: "hrPhone",
          icon: "mdi-cellphone",
          rule: [
            value => !!value || '연락처는 필수 항목입니다.',
            value => /^\d{3}-\d{3,4}-\d{4}$/.test(value) || '연락처 형식이 유효하지 않습니다.'
          ],
          longmsg: ''
        },
      ],
      credentials: {
        hrName: "",
        hrEmail: "",
        hrPhone: "",
      },

 
    }
  },
  computed: {
    ...mapState(["accessToken", 'user', 'recruit']),
    ...mapGetters(["getAccessToken"]),
    isNotRoom() {
      if (['WaitRoom', 'ViewRoom', 'Main'].includes(this.$route.name)) {
        return false
      } else {
        return true
      }
    },

    isNotWaitRoomViewRoom() {
      if (['WaitRoom', 'ViewRoom'].includes(this.$route.name)) {
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
    },
    isFooterView() {
       if (['Main', 'Home'].includes(this.$route.name)) {
        return true
      } else {
        return false
      }
    }
  },
  methods: {
    findPassword: function () {
      if (this.$refs.form.validate()) {

        let signupForm = {
          hrEmail: this.credentials.hrEmail,
          hrName: this.credentials.hrName,
          hrPhone: this.credentials.hrPhone
        }

        axios.post(`${SERVER_URL}/hr/findPassword`, signupForm)
          .then(() => {
            // console.log(res)
            alert("입력한 이메일로 새로운 비밀번호를 전송했습니다.")
            this.$router.push({
              name: "Home"
            })
          })
          .catch((err) => {
            console.log(err)
            alert("가입된 정보가 존재하지 않습니다.")
          })

        this.credentials.hrName = ""
        this.credentials.hrEmail = ""
        this.credentials.hrPhone = ""
        this.dialogPassword = false
      }

    },
    findPasswordOutside: function() {
      this.credentials.hrName = ""
      this.credentials.hrEmail = ""
      this.credentials.hrPhone = ""
      this.dialogPassword = false
    },

    login: function () {
      this.$store.dispatch("LOGIN", this.userCredentials)
        .then(() => {
          // this.$router.push(this.$router.currentRoute) 
          this.$router.push({
            name: 'Home'
          })
          this.userCredentials.userEmail = ""
          this.userCredentials.userPassword = ""
        })

      this.dialogLogin = false
    },
    loginOutside: function() {
      this.dialogLogin = false
      this.userCredentials.userEmail = ''
      this.userCredentials.userPassword = ''
    },

    logout: function () {
      this.$store.dispatch("LOGOUT")
        .then(() => this.$router.replace({
          name: "Home"
        }))
    },
    //개행메소드
    handleNewLine(str) {
      return String(str).replace(/(?:\r\n|\r|\n)/g, "</br>");
    },
  },
  created: function () {
    this.Manager = this.$store.state.Manager
    console.log("this.", this.$route.name)

    setInterval(function () {     
      var timer = new Date()
      var month=timer.getMonth()+1
      var day=timer.getDate()
      var h = timer.getHours()
      var m = timer.getMinutes()
      var s = timer.getSeconds()
      // console.log(month)
      document.getElementById('clock').innerHTML = month +"/"+ day +"&nbsp&nbsp" + h + "시" + m + "분&nbsp&nbsp" + s
    }, 1000)
  },
  updated() {
    
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

  .v-transition {
    transition: opacity .1s ease-out;
  }
    
  .v-enter, .v-leave {
    opacity: 0;
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