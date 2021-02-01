<template>
  <div id="signup" class="mt-12 ml-14 text-white">
    <div class="h4">회원가입</div>
    <!-- 정보입력 Form -->
    <v-row no-gutters>
      <v-col cols="5" class="mt-5 ml-6">
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
          dark
        ></v-checkbox>
      </v-col>
      <v-col cols="12"></v-col>
      <span @click="signup" class="text-white text-right ma-8 pr-16">가입하기</span>
    </v-row>

    <!-- Validation Form -->
    <!-- <v-simple-table>
      <template v-slot:default>
        <tbody>
          <tr v-for="item in userInfo" :key="item.label">
            <td>{{ item.label }}</td>
            <td>
              <template>
                <validation-observer ref="observer" v-slot="{ invalid }">
                  <form @submit.prevent="submit">
                    <validation-provider
                      v-slot="{ errors }"
                      name="Name"
                      rules="required|max:10"
                    >
                      <v-text-field
                        v-model="name"
                        :counter="10"
                        :error-messages="errors"
                        label="Name"
                        required
                      ></v-text-field>
                    </validation-provider>
                    <validation-provider
                      v-slot="{ errors }"
                      name="phoneNumber"
                      :rules="{
                        required: true,
                        digits: 7,
                        regex: '^(71|72|74|76|81|82|84|85|86|87|88|89)\\d{5}$',
                      }"
                    >
                      <v-text-field
                        v-model="phoneNumber"
                        :counter="7"
                        :error-messages="errors"
                        label="Phone Number"
                        required
                      ></v-text-field>
                    </validation-provider>
                    <validation-provider
                      v-slot="{ errors }"
                      name="email"
                      rules="required|email"
                    >
                      <v-text-field
                        v-model="email"
                        :error-messages="errors"
                        label="E-mail"
                        required
                      ></v-text-field>
                    </validation-provider>
                    <validation-provider
                      v-slot="{ errors }"
                      name="select"
                      rules="required"
                    >
                      <v-select
                        v-model="select"
                        :items="items"
                        :error-messages="errors"
                        label="Select"
                        data-vv-name="select"
                        required
                      ></v-select>
                    </validation-provider>
                    <validation-provider
                      v-slot="{ errors }"
                      rules="required"
                      name="checkbox"
                    >
                      <v-checkbox
                        v-model="checkbox"
                        :error-messages="errors"
                        value="1"
                        label="Option"
                        type="checkbox"
                        required
                      ></v-checkbox>
                    </validation-provider>

                    <v-btn class="mr-4" type="submit" :disabled="invalid">
                      submit
                    </v-btn>
                    <v-btn @click="clear"> clear </v-btn>
                  </form>
                </validation-observer>
              </template>
            </td>
          </tr>
        </tbody>
      </template>
    </v-simple-table> -->
  </div>
</template>

<script>
// import axios from 'axios'

export default {
  name: "Signup",
  components: {},
  data: () => ({
    userInfo: [
      { label: "이메일", type: "text", value: 'hrEmail'},
      { label: "비밀번호", type: "password", value: 'hrPassword'},
      { label: "비밀번호 확인", type: "password", value: 'hrPasswordConfirmation'},
      { label: "연락처", type: "text", value: 'hrPhone' },
      { label: "기업명", type: "text", value: 'comName' },
      { label: "기업 로고", type: "text", value: 'comLogo' },
      { label: "기업 주소", type: "text", value: 'comAddress' },
      { label: "기업 홈페이지", type: "text", value: 'comHomepage' },
    ],
    credentials: {
      hrEmail: '',
      hrPassword: '',
      hrPasswordConfirmation: '',
      hrPhone: '',
      comName: '',
      comLogo: '',
      comAddress: '',
      comHomepage: '',
      hrCertificated: '',
      agreed: false,
    },
  }),
  methods: {
    // 회원가입 axios
    signup: function () {
      /*
      if (this.credentials.agreed){
        axios.post('회원가입 URL', this.credentials)
          .then(res => {
            console.log(res)
            this.$router.push({ name: 'Login' })
          })
          .catch(err => {
            console.log(err)
            alert("회원가입 정보가 올바르지 않습니다.")
          })
      } else {
        alert("개인정보 이용 약관에 동의해야 합니다.")
      }
    */
    }
  }
}
</script>

<style scoped>
.info-table {
  background-color: transparent !important;
}
</style>

<!--<script>
import { required, digits, email, max, regex } from "vee-validate/dist/rules";
import {
  extend,
  ValidationObserver,
  ValidationProvider,
  setInteractionMode,
} from "vee-validate";

setInteractionMode("eager");

extend("digits", {
  ...digits,
  message: "{_field_} needs to be {length} digits. ({_value_})",
});

extend("required", {
  ...required,
  message: "{_field_} can not be empty",
});

extend("max", {
  ...max,
  message: "{_field_} may not be greater than {length} characters",
});

extend("regex", {
  ...regex,
  message: "{_field_} {_value_} does not match {regex}",
});

extend("email", {
  ...email,
  message: "Email must be valid",
});

export default {
  name: "Singup",
  components: {
    ValidationProvider,
    ValidationObserver,
  },
  data: () => ({
    name: "",
    phoneNumber: "",
    email: "",
    select: null,
    items: ["Item 1", "Item 2", "Item 3", "Item 4"],
    checkbox: null,
  }),

  methods: {
    submit() {
      this.$refs.observer.validate();
    },
    clear() {
      this.name = "";
      this.phoneNumber = "";
      this.email = "";
      this.select = null;
      this.checkbox = null;
      this.$refs.observer.reset();
    },
  },
};
</script>-->