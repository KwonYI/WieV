<template>
  <div id="signup" class="mt-12 ml-16" @keyup.enter="signup">
    <div class="h4">회원정보 탈퇴</div>
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
            </tr>
          </tbody>
        </v-simple-table>
      </v-col>
      <v-col cols="12"></v-col>
      <div @click="userDelete" class="ma-8 pr-16">
          확인
      </div>
    </v-row>
  </div>
</template>

<script>

export default {
  name: "ProfileDeleteForm",
  components: {},
  data: () => ({
    userInfo: [
      { label: "비밀번호", type: "password", value: "hrPassword" },
    ],
    credentials: {
      hrPassword: "",
    },
  }),
  methods: {
    //비밀번호 같은지 다른지 확인

    userDelete: function() {
        if(this.$store.state.user.userPassword === this.credentials.hrPassword){
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
    }
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