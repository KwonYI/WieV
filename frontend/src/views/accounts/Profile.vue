<template>
  <div id="profile">
    <v-container>
      <v-row class="h-100">
        <v-col col="6">
          <v-card class="mx-auto my-12" style="height:400px">
            <v-card-title class="main-bg-navy">
              <h2 style="color:white">
                <v-icon style="color:white" large>mdi-office-building</v-icon> 소속 회사
              </h2>
            </v-card-title>
            <v-img :src="user.userComLogo" max-height="150" max-width="300" class="m-4"></v-img>
            <v-card-text>
              <h4><v-icon large>mdi-office-building-outline</v-icon> 회사명 : {{ user.userComName }}</h4>
              <h4><v-icon large>mdi-map-marker</v-icon> 주소 : {{ user.userComAddress }}</h4>
              <h4><v-icon large>mdi-link-variant</v-icon> 홈페이지 : {{ user.userComHomepage  }}</h4>
            </v-card-text>
          </v-card>
        </v-col>

        <v-col col="6">
          <v-card class="mx-auto my-12" style="height:400px">
            <v-card-title class="main-bg-navy" >
              <h2 style="color:white">
                <v-icon style="color:white" large>mdi-account-box</v-icon> 내 정보
              </h2>
            </v-card-title>
            <br>
            <v-card-text>
              <h4><v-icon large>mdi-email</v-icon> 이메일 : {{ user.userEmail }}</h4>
              <h4><v-icon large>mdi-account</v-icon> 이름 : {{ user.userName }}</h4>
              <h4><v-icon large>mdi-cellphone</v-icon> 연락처 : {{ user.userPhone }}</h4>
            </v-card-text>
            <div v-if="user.userViewWait === -1" class="d-flex justify-center">
              <v-dialog v-model="dialogUpdate" width="500">
                <template v-slot:activator="{ on, attrs }">
                  <v-btn class="ma-3" text color="blue lighten-2" v-bind="attrs" v-on="on">
                  <div>
                    <v-icon>mdi-account-cog</v-icon> 회원정보 수정
                  </div>
                </v-btn>
                </template>
    
                <v-card>
                  <v-card-title class="headline main-bg-navy lighten-2">
                    <div>회원정보 수정</div>
                  </v-card-title>
          
                  <v-card-text>
                    <v-container class="d-flex flex-column" fluid>
                      <v-simple-table fixed-header class="d-flex flex-column" style="overflow-y: hidden">
                        <template v-slot:default>
                          <tbody>
                            <tr v-for="(item, index) in updateUserInfo" :key="index">
                              <td>{{ item.label }}</td>
                              <td>
                                <v-text-field
                                  :label="item.label"
                                  :type="item.type"
                                  v-model="updateCredentials[item.value]"
                                ></v-text-field>
                              </td>
                              <v-btn v-if="index === 2" @click="password_certified">비밀번호 확인</v-btn>
                            </tr>
                          </tbody>
                        </template>
                      </v-simple-table>
                    </v-container>
                  </v-card-text>

                  <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="primary" text @click="update">
                      확인
                    </v-btn>
                  </v-card-actions>
                </v-card>
              </v-dialog>
              <v-dialog v-model="dialogDelete" width="500">
                <template v-slot:activator="{ on, attrs }">
                  <v-btn class="ma-3" text color="red lighten-2" v-bind="attrs" v-on="on">
                  <div>
                    <v-icon>mdi-delete</v-icon> 회원정보 탈퇴
                  </div>
                </v-btn>
                </template>
    
                <v-card>
                  <v-card-title class="headline main-bg-navy lighten-2">
                    <div>회원정보 탈퇴</div>
                  </v-card-title>
          
                  <v-card-text>
                    <v-container class="d-flex flex-column" fluid>
                      <v-simple-table fixed-header class="d-flex flex-column" style="overflow-y: hidden">
                        <template v-slot:default>
                          <tbody>
                            <tr v-for="(item, index) in deleteUserInfo" :key="index">
                              <td>{{ item.label }}</td>
                              <td>
                                <v-text-field
                                  :label="item.label"
                                  :type="item.type"
                                  v-model="deleteCredentials[item.value]"
                                ></v-text-field>
                              </td>
                            </tr>
                          </tbody>
                        </template>
                      </v-simple-table>
                    </v-container>
                  </v-card-text>

                  <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="red" text @click="userDelete">
                      확인
                    </v-btn>
                  </v-card-actions>
                </v-card>
              </v-dialog>
            </div>
          </v-card>
          <!-- <div class="text-center">
            <v-btn rounded color="primary" dark> 웹 화면 테스트 </v-btn>
          </div> -->
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import { mapState } from "vuex";

export default {
  name: "Profile",
  data: function () {
    return {
      dialogUpdate: false,
      dialogDelete: false,

      updateUserInfo: [
        { label: "이름", type: "text", value: "hrName" },
        { label: "비밀번호", type: "password", value: "hrPassword" },
        {
          label: "비밀번호 확인",
          type: "password",
          value: "hrPasswordConfirmation",
        },
        { label: "연락처", type: "text", value: "hrPhone" },
      ],
      updateCredentials: {
        hrName: "",
        hrPassword: "",
        hrPasswordConfirmation: "",
        hrPhone: "",
      },
      isPasswordCertified:false,

      deleteUserInfo: [
        { label: "비밀번호", type: "password", value: "hrPassword" },
      ],
      deleteCredentials: {
        hrPassword: "",
      },
    }
  },
  created: function () {
  },
  computed: {
    ...mapState(['user']),
  },
  methods: {
    //비밀번호 같은지 다른지 확인
    password_certified: function() {
      
      let patternEngAtListOne = new RegExp(/[a-zA-Z]+/); // + for at least one
      let patternSpeAtListOne = new RegExp(/[~!@#$%^&*()_+|<>?:{}]+/); // + for at least one
      let patternNumAtListOne = new RegExp(/[0-9]+/); // + for at least one

      if (
        !patternEngAtListOne.test(this.updateCredentials.hrPassword) ||
        !patternSpeAtListOne.test(this.updateCredentials.hrPassword) ||
        !patternNumAtListOne.test(this.updateCredentials.hrPassword) ||
        this.updateCredentials.hrPassword.length < 8
      ) {
        alert(
          "비밀번호는 1개 이상의 특수문자, 대소문자 및 숫자를 포함하고 8자리 이상이여야 합니다."
        );
        this.isPasswordCertified = false
        return;
      }

      if(!this.updateCredentials.hrPasswordConfirmation){
        alert("비밀번호 확인을 입력해주세요")
      }
      else if (this.updateCredentials.hrPassword == this.updateCredentials.hrPasswordConfirmation) {
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

      if(!this.updateCredentials.hrName||!this.updateCredentials.hrPassword||!this.updateCredentials.hrPasswordConfirmation||!this.updateCredentials.hrPhone){
        alert("정보를 모두 입력해주세요.")
        return
      }

      this.$store
        .dispatch("USER_UPDATE", this.updateCredentials)
        .then(() => this.$router.replace({ name: "Profile" }))

      this.dialogUpdate = false
    },

    userDelete: function() {
      console.log(this.$store.state.user.userPassword)
      console.log(this.deleteCredentials.hrPassword)
      if(this.$store.state.user.userPassword === this.deleteCredentials.hrPassword){
        this.$store
          .dispatch("USER_DELETE")
          .then(() => {
            // console.log("회원탈퇴")
            this.$router.replace({ name: "Home" })
          });
      }
      else {
        alert("비밀번호를 확인해주세요.");
      }

      this.dialogDelete = false
    }
  },
};
</script>

<style scoped>
#profile{
  background-color: aliceblue;
  height: 100%;
}

#profileRow {
  height: 30vh;
}
</style>