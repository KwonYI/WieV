<template>
  <div id="signup" class="text-white" @keyup.enter="signup">
    <!-- 정보입력 Form -->
    <!-- <v-row no-gutters> -->
      <!-- v-model="recFormValid" -->
      <div class="h3 text-center mb-5">회원가입</div>

      <v-form ref="form" lazy-validation>
        <v-col md="6" offset-md="3">
          <v-simple-table class="info-table" dark>
            <tbody>
              <tr v-for="(item, index) in userInfo" :key="index">
                <td>{{ item.label }}</td>
                <td v-if="index === 6"> <!-- index 6번 select box -->
                <v-autocomplete
                  v-model="credentials[item.value]"
                  :items="companyNameList"
                  :label="item.name"
                  hide-details
                  dense
                ></v-autocomplete>
                </td>
                <td v-else>
                  <v-text-field
                    :label="item.label"
                    :type="item.type"
                    :rules="item.label === '비밀번호 확인' ? passwordConfirmation : item.rule"
                    :class="item.longmsg"
                    v-model="credentials[item.value]"
                    single-line
                  ></v-text-field>
                </td>
                <td>
                  <v-btn v-if="index === 0" @click="send">인증 메일 발송 </v-btn>
                  <v-btn v-if="index === 1" @click="certified">인증 확인 </v-btn>
                </td>
              </tr>
            </tbody>
          </v-simple-table>
          <br />
          <span>개인정보 및 서비스 이용약관</span>
          <v-card elevation="2" outlined>
            명령·규칙 또는 처분이 헌법이나 법률에 위반되는 여부가 재판의 전제가 된
            경우에는 대법원은 이를 최종적으로 심사할 권한을 가진다. 재의의 요구가
            있을 때에는 국회는 재의에 붙이고, 재적의원과반수의 출석과 출석의원
            3분의 2 이상의 찬성으로 전과 같은 의결을 하면 그 법률안은 법률로서
            확정된다. 대통령·국무총리·국무위원·행정각부의 장·헌법재판소
            재판관·법관·중앙선거관리위원회 위원·감사원장·감사위원 기타 법률이 정한
            공무원이 그 직무집행에 있어서 헌법이나 법률을 위배한 때에는 국회는
            탄핵의 소추를 의결할 수 있다. 국무위원은 국무총리의 제청으로 대통령이
            임명한다. 연소자의 근로는 특별한 보호를 받는다. 외국인은 국제법과
            조약이 정하는 바에 의하여 그 지위가 보장된다. 국가는 여자의 복지와
            권익의 향상을 위하여 노력하여야 한다. 탄핵결정은 공직으로부터 파면함에
            그친다. 그러나, 이에 의하여 민사상이나 형사상의 책임이 면제되지는
            아니한다. 모든 국민은 주거의 자유를 침해받지 아니한다. 주거에 대한
            압수나 수색을 할 때에는 검사의 신청에 의하여 법관이 발부한 영장을
            제시하여야 한다.
          </v-card>
          <v-checkbox
            v-model="credentials.agreed"
            label="위 약관에 동의합니다."
            :rules="[value => !!value || '약관동의는 필수 항목입니다.']"
            dark
          ></v-checkbox>
          <div class="d-flex justify-end"><v-btn @click="signup" large id="signUpButton" class="mb-5">가입하기</v-btn></div>
        </v-col>
      </v-form> 
      
    <!-- </v-row> -->
  </div>
</template>

<script>
import axios from "axios"
import { mapState } from "vuex";

const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  name: "Signup",
  components: {},
  computed: {
    ...mapState(["companyNameList"]),
    passwordConfirmation() {
      return [
        value => value === this.credentials.hrPassword || '비밀번호가 일치하지 않습니다.' 
      ]
    }
  },
  data: () => ({
    userInfo: [
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
        label: "이메일 인증번호",
        type: "text",
        value: "hrEmailCode",
        rule: [],
        longmsg: ''
      },
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
        label: "비밀번호",
        type: "password",
        value: "hrPassword",
        rule: [
          value => !!value || '비밀번호는 필수 항목입니다.',
          value => /^(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$/.test(value) || '비밀번호는 반드시 1개 이상의 문자, 숫자, 특수문자를 포함해야 합니다.'
        ],
        longmsg: 'longmsg'
      },
      {
        label: "비밀번호 확인", 
        type: "password",
        value: "hrPasswordConfirmation",
        rule: [],
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
      {
        label: "기업명",
        type: "text",
        value: "comName",
        rule: [
          value => !!value || '기업명은 필수 항목입니다.',
        ],
        longmsg: ''
      },
    ],
    credentials: {
      hrEmail: "",
      hrEmailCode: "",
      hrName: "",
      hrPassword: "",
      hrPasswordConfirmation: "",
      hrPhone: "",
      comName: "",
      hrCertified: "",
      agreed: false,
    },
    certifiedNum: "",
    isCertified: false,
    isPasswordCertified:false,
  }),
  created: function () {
    this.$store
      .dispatch("GET_COMPANY_NAME_LIST")
      .then(() => console.log("회사 리스트 가져오기 완료"))
  },
  methods: {
    // 메일 발송
    send: function() {
      
      let regexp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i
      if (!regexp.test(this.credentials.hrEmail)) {
        alert("이메일 형식으로 입력해주세요.")
        return
      }

      if (this.credentials.hrEmail) {
        axios
          .post(`${SERVER_URL}/hr/send`, this.credentials)
          .then((res) => {
            this.certifiedNum = res.data
            alert("메일 보내기 성공")
          })
          .catch((err) => {
            console.log(err)
            alert("메일 보내기 실패")
          })
      }
    },

    //메일 인증 확인
    certified: function() {
      console.log("입력:" + this.credentials.hrEmailCode)
      console.log("인증번호:" + this.certifiedNum)
      if(!this.credentials.hrEmailCode){
        alert("인증번호를 입력해주세요")
      }
      else if (this.credentials.hrEmailCode == this.certifiedNum) {
        this.isCertified = true
        alert("인증 완료")
      } else {
        alert("인증 실패")
        this.isCertified = false
      }
    },

    signup: function() {
      if (this.$refs.form.validate()) {
        if (this.isCertified == false) {
          alert("메일 인증을 진행하세요.")
        } else {
          let signupForm = {
            hrEmail: this.credentials.hrEmail,
            hrName: this.credentials.hrName,
            hrPassword: this.credentials.hrPassword,
            hrPhone: this.credentials.hrPhone,
            comName: this.credentials.comName
          }
          console.log(signupForm)

          axios.post(`${SERVER_URL}/hr/signup`, signupForm)
            .then(res => {
              console.log(res)
              alert("회원가입 성공")
              this.$router.push({ name: "Home" })
            })
            .catch((err) => {
              console.log(err)
              alert("회원가입 정보가 올바르지 않습니다.")
            })
        }
      }
    }
  }
}
</script>

<style scoped>
#signup{
  /* color: black !important; */
  background-color: #304B61;
}

#signUpButton {
  width: 20px;
}

.info-table {
  background-color: transparent !important;
}

table tbody tr:hover {
  background: inherit !important;
}

td {
  border: none !important;
}

::v-deep .longmsg > .v-input__control {
  width: min-content;
}

</style>