<template>
  <div id="signup" class="mt-12 ml-16" @keyup.enter="signup">
    <div class="h4">회원정보 수정</div>
    <!-- 정보입력 Form -->
    <v-row no-gutters>
        <v-form
        ref="form"
        v-model="recFormValid"
        lazy-validation
        >
      <v-col cols="5" class="mt-5 ml-16">
        <v-simple-table class="info-table" dark>
          <tbody>
            <tr v-for="(item, index) in userInfo" :key="index">
              <td>{{ item.label }}</td>
              <td>
                <v-text-field
                  :label="item.label"
                  :type="item.type"
                  :class="item.longmsg"
                  v-model="credentials[item.value]"
                  :rules="item.rule"
                ></v-text-field>
              </td>
            </tr>
            <v-btn @click="findPassword">비밀번호 찾기</v-btn>
          </tbody>
        </v-simple-table>
      </v-col>
        </v-form>
    </v-row>
  </div>
</template>

<script>
import axios from "axios"
const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  name: "FindPassword",
  components: {},
  data: () => ({
    userInfo: [
       {
        label: "이름",
        type: "text",
        value: "hrName",
        rule: [
          value => !!value || '이름은 필수 항목입니다.',
        ],
        longmsg: ''
      },
       {
        label: "이메일",
        type: "text",
        value: "hrEmail",
        rule: [
          value => !!value || '이메일은 필수 항목입니다.',
          value => /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/.test(value) || '이메일 형식이 유효하지 않습니다.',
        ],
        longmsg: ''
      },
       {
        label: "연락처",
        type: "text",
        value: "hrPhone",
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
  }),
  created: function () {

  },
  computed: {
 
  },
  methods: {
    findPassword: function() {
        if (this.$refs.form.validate()) {

         let signupForm = {
            hrEmail: this.credentials.hrEmail,
            hrName: this.credentials.hrName,
            hrPhone: this.credentials.hrPhone
          }

          axios.post(`${SERVER_URL}/hr/findPassword`,signupForm)
            .then(res => {
              console.log(res)
              alert("입력한 이메일로 새로운 비밀번호를 전송했습니다.")
              this.$router.push({ name: "Home" })
            })
            .catch((err) => {
              console.log(err)
              alert("가입된 정보가 존재하지 않습니다.")
            })   
        }  
    },
  },
}
</script>

<style scoped>
 .info-table {
  background-color: transparent !important;
}

table tbody tr:hover {
  background: inherit !important;
}

td {
  border: none !important;
} 


</style>