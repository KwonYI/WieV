<template>
  <div id="schedule">
    <!--스케줄 (공고 리스트) 컴포넌트 // 나중에 라우터로 변경 될 수 있다. -->
    <div class="h4 mb-10">반갑습니다. [{{user.userComName}}] - {{user.userName}}님
    </div>

    <!-- <div> -->
      <v-dialog v-model="dialog" persistent max-width="600px" @click:outside="onClickOutside">
        <template v-slot:activator="{ on, attrs }">
          <v-btn large v-bind="attrs" rounded v-on="on" class="amber lighten-3 font-weight-black">
            <v-icon left>mdi-calendar-plus</v-icon>공고 추가하기
          </v-btn>
        </template>
        <v-card>
          <v-card-title>
            <span class="headline">[{{ user.userComName }}] 신규 공고 등록</span>
          </v-card-title>
          <v-card-text>
            <v-form
              ref="form"
              v-model="recFormValid"
              lazy-validation
            >
              <v-container>
                <v-row>
                  <v-col cols="12" sm="6" md="4">
                    <v-autocomplete
                      v-model="reYear"
                      :items="formData.reYear"
                      :rules="[v => !!v || '연도는 필수 항목입니다.']"
                      label="Year"
                      required
                    >
                    </v-autocomplete>
                  </v-col>
                  <v-col cols="12" sm="6" md="4">
                    <v-autocomplete
                      v-model="reFlag"
                      :items="formData.reFlag"
                      :rules="[v => !!v || '시즌은 필수 항목입니다.']"
                      label="Season"
                      required
                    >
                    </v-autocomplete>
                  </v-col>
                  <v-col cols="12" sm="6" md="4">
                    <v-autocomplete
                      v-model="reStatus"
                      :items="formData.reStatus"
                      :rules="[v => !!v || '채용 형태는 필수 항목입니다.']"
                      label="Type"
                      required
                    >
                    </v-autocomplete>
                  </v-col>

                  <v-col cols="12" sm="6">
                    <v-text-field
                      v-model="dateRangeText"
                      :rules="rangeRules"
                      label="면접 기간" prepend-icon="mdi-calendar"
                      readonly
                      required
                    >
                    </v-text-field>
                    <div>면접 시작일 : {{ formData.reDates[0] }}</div>
                    <div>면접 종료일 : {{ formData.reDates[1] }}</div>
                  </v-col>
                  <v-col cols="12" sm="6">
                    <v-date-picker v-model="formData.reDates" range></v-date-picker>
                  </v-col>
                </v-row>
              </v-container>
            </v-form>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" text @click="dialog = false">
              Close
            </v-btn>
            <v-btn
              color="blue darken-1"
              text
              @click="createRecruit"
            >
              Save
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
      <hr>
      <div>
        최근 등록된 순으로 보여집니다. 
      </div>
      
      <v-simple-table fixed-header dark height="300px" class="rec-list mt-5 ">
        <thead>
          <tr>
            <th class="rec-header">No</th>
            <th class="rec-header">시즌</th>
            <th class="rec-header">분류</th>
            <th class="rec-header">기간</th>
            <th class="rec-header">관리</th>
          </tr>
        </thead>
              <tbody >
            <!-- reSeq 내림차순으로 정렬된 상태입니다.(최근 만든 순으로 보여줌)  -->
            <tr v-for="(recruit, index) in getRecruitListLately" :key="index" class="text-center  font-weight-bold" @click="goToProgress(recruit, index)">
              
                <td class="blurEffect">{{ getRecruitListCount - index }}</td>
                <!-- <td>{{ recruit.reSeq }}</td> -->
                <td class="blurEffect">{{ recruit.reYear }} {{ recruit.reFlag }}</td>
                <td class="blurEffect">{{ recruit.reStatus }}</td>
                <td class="blurEffect">{{ recruit.reStartDate }} ~ {{recruit.reEndDate }}</td>         
                <td class="blurEffect"><v-btn light rounded color="indigo lighten-3" class=" font-weight-bold">관리하기</v-btn></td>         
              <!-- <td> -->
                <!-- <v-btn :to="{ name: 'Progress', params: { recruitNo: recruit.reSeq } }" >관리하기</v-btn> -->
                <!-- <v-btn @click="goToProgress(recruit)">관리하기</v-btn> -->
              <!-- </td> -->

            </tr>
            </tbody>
        
      </v-simple-table>

    <!-- </div> -->

  </div>

</template>

<script>
  import {
    mapState,
    mapGetters
  } from "vuex";

  export default {
    name: "Schedule",
    data: function () {
      return {
        // com_name: "버즈글로벌",
        dialog: false,

        formData: {
          reYear: [2021, 2022, 2023],
          reFlag: ['상반기', '하반기'],
          reStatus: ['신입', '경력', '인턴', '계약'],
          reDates: ['2021-01-01', '2021-01-01'],
        },
        
        recFormValid: true,
        rangeRules: [
          () => this.formData.reDates[0] <= this.formData.reDates[1] || '면접 기간이 올바르지 않습니다.'
        ],

        reYear: null,
        reFlag: null,
        reStatus: null,
      }
    },

    methods: {
      createRecruit: function () {
        // console.log(this.$refs.form.value)
        if (this.$refs.form.validate()) {
          this.dialog = false

          let recruitForm = {
            reYear: this.reYear,
            reFlag: this.reFlag,
            reStatus: this.reStatus,
            reStartDate: this.formData.reDates[0],
            reEndDate: this.formData.reDates[1],
          }
          // console.log(recruitForm)

          this.$store.dispatch("INSERT_RECRUIT", recruitForm)
            .then(() => console.log("insertRecruit"))

        //여기에 axios.post 요청으로, DB에 새로운 공고를 저장할 수 있도록 합니다. 
        // 새로 저장된 공고의 정보 (seq포함)를 가져오고, state 에 저장합니다. 
        //this.$store.dispatch('addRecruit', 응답으로 받은 데이터 res.data)

          // this.$refs.form.reset()
        // }
        }
      },
      onClickOutside: function() {
        this.reYear = ''
        this.reFlag = ''
        this.reStatus = ''
        this.formData.reDates = ['2021-01-01', '2021-01-01']

        this.dialog = false;
      },

      goToProgress: function (recruit, index) {
        this.$store.state.selectedRecruitNo = recruit.reSeq
        this.$store.state.storeRecruitItem = recruit
        this.$store.state.selectedRecruitIndex = index
        console.log("goToProgres!!", this.$store.state.storeRecruitItem)
        this.$router.push({
          // name: 'Progress',
          name: 'Menu',
          params: {
            recruitIndex: index,
            recruitNo: recruit.reSeq,
            recruitItem: recruit
          }
        })
      },

      // mouseIn: function () {
      //   // this.backgroundColor = this.colorGrey
      // },
    },

    

    created: function () {
      this.$store
        .dispatch("GET_RECRUIT_LIST")
        .then(() => console.log("getRecruitList"))
      console.log(this.$store.state.recruitList);
    },
    computed: {
      ...mapState(["recruitList", "user"]),
      ...mapGetters(["getRecruitListLately", "getRecruitListCount"]),

      dateRangeText() {
        return this.formData.reDates.join(' ~ ')
      },

    },
  }
</script>

<style scoped>
.rec-list {
  background: transparent !important;
}
.rec-header {
  text-align: center !important;
  background: #304B61 !important;
}
table tbody tr:hover {
  background: grey !important;
  opacity: 0.9;
}
</style>