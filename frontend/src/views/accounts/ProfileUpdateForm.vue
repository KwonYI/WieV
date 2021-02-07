<template>
  <div id="signup" class="mt-12 ml-16" @keyup.enter="signup">
    <div class="h4">회원정보 수정</div>
    <!-- 정보입력 Form -->
    <v-row no-gutters>
      <v-col cols="5" class="mt-5 ml-16">
        <v-simple-table class="info-table" dark>
          <tbody>
            <tr v-for="(item, index) in userInfo" :key="index">
              <td>{{ item.label }}</td>
              <td>
                <v-text-field
                  :label="item.label"
                  :type="item.type"
                  v-model="credentials[item.value]"
                ></v-text-field>
              </td>
              <v-btn v-if="index === 2" @click="password_certified">비밀번호 확인</v-btn>
            </tr>
          </tbody>
        </v-simple-table>
      </v-col>
      <v-col cols="12"></v-col>
      <span @click="update" class="ma-8 pr-16">
        수정하기
      </span>
    </v-row>
  </div>
</template>

<script>
// import axios from "axios"
import { mapState } from "vuex";

// const SERVER_URL = "https://localhost:8080/"
// const SERVER_URL = "https://i4a405.p.ssafy.io:8080"
// const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  name: "ProfileUpdateForm",
  components: {},
  data: () => ({
    userInfo: [
      { label: "이름", type: "text", value: "hrName" },
      { label: "비밀번호", type: "password", value: "hrPassword" },
      {
        label: "비밀번호 확인",
        type: "password",
        value: "hrPasswordConfirmation",
      },
      { label: "연락처", type: "text", value: "hrPhone" },
    ],
    credentials: {
      hrName: "",
      hrPassword: "",
      hrPasswordConfirmation: "",
      hrPhone: "",
    },
    isPasswordCertified:false,
  }),
  created: function () {
    this.$store
      .dispatch("GET_COMPANY_NAME_LIST")
      .then(() => console.log("회사 리스트 가져오기 완료"))
  },
  computed: {
      ...mapState(["companyNameList"]),
  },
  methods: {
    //비밀번호 같은지 다른지 확인
    password_certified: function() {
      
      let patternEngAtListOne = new RegExp(/[a-zA-Z]+/); // + for at least one
      let patternSpeAtListOne = new RegExp(/[~!@#$%^&*()_+|<>?:{}]+/); // + for at least one
      let patternNumAtListOne = new RegExp(/[0-9]+/); // + for at least one
      if (
        !patternEngAtListOne.test(this.credentials.hrPassword) ||
        !patternSpeAtListOne.test(this.credentials.hrPassword) ||
        !patternNumAtListOne.test(this.credentials.hrPassword) ||
        this.credentials.hrPassword.length < 8
      ) {
        alert(
          "비밀번호는 1개 이상의 특수문자, 대소문자 및 숫자를 포함하고 8자리 이상이여야 합니다."
        );
        this.isPasswordCertified = false
        return;
      }

   if(!this.credentials.hrPasswordConfirmation){
        alert("비밀번호 확인을 입력해주세요")
      }
      else if (this.credentials.hrPassword == this.credentials.hrPasswordConfirmation) {
        alert("확인되었습니다.")
        this.isPasswordCertified=true
      }
      else{
        alert("비밀번호가 다릅니다.")
        this.isPasswordCertified = false
      }
    },

    update: function() {
      if(this.isPasswordCertified==false){
        alert("비밀번호 확인을 진행하세요.")
        return
      }

      if(!this.credentials.hrName||!this.credentials.hrPassword||!this.credentials.hrPasswordConfirmation||!this.credentials.hrPhone){
        alert("정보를 모두 입력해주세요.")
        return
      }

      this.$store
        .dispatch("USER_UPDATE", this.credentials)
        .then(() => this.$router.replace({ name: "Profile" }))
    },
  },
}
</script>

<style scoped>
/* .info-table {
  background-color: transparent !important;
}

table tbody tr:hover {
  background: inherit !important;
}

td {
  border: none !important;
} */
</style>