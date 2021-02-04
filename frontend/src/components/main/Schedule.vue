<template>
  <div id="schedule">
    <!--스케줄 (공고 리스트) 컴포넌트 // 나중에 라우터로 변경 될 수 있다. -->
    <div class="h4 mb-10">반갑습니다. [{{user.userComName}}] - {{user.userName}}님</div>

    <div>

      <v-dialog v-model="dialog" persistent max-width="600px">
        <template v-slot:activator="{ on, attrs }">
          <v-btn dark v-bind="attrs" v-on="on">
            공고 추가하기
          </v-btn>
        </template>
        <v-card>
          <v-card-title>
            <span class="headline">[{{com_name}}] 신규 공고 등록</span>
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
                      :items="[2021, 2022, 2023]"
                      :rules="[v => !!v || '연도값은 필수입니다.']"
                      label="Year"
                      required
                      v-model="new_recruit.reYear"
                    >
                    </v-autocomplete>
                  </v-col>
                  <v-col cols="12" sm="6" md="4">
                    <v-autocomplete
                      :items="['상반기', '하반기']"
                      :rules="[v => !!v || '시즌값은 필수입니다.']"
                      label="Season"
                      required
                      v-model="new_recruit.reFlag"
                    >
                    </v-autocomplete>
                  </v-col>
                  <v-col cols="12" sm="6" md="4">
                    <v-autocomplete
                    :items="['신입', '경력', '계약']"
                    :rules="[v => !!v || '채용 형태 값은 필수입니다.']"
                    label="Type"
                    required
                    v-model="new_recruit.reStatus"
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
                    <div>면접 시작일 : {{ dates[0] }}</div>
                    <div>면접 종료일 : {{ dates[1] }}</div>
                  </v-col>
                  <v-col cols="12" sm="6">
                    <v-date-picker v-model="dates" range>
                    </v-date-picker>
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
      <v-simple-table fixed-header dark height="300px" class="rec-list mt-5">
        <thead>
          <tr>
            <th class="rec-header">No</th>
            <th class="rec-header">시즌</th>
            <th class="rec-header">분류</th>
            <th class="rec-header">기간</th>
            <th class="rec-header">관리</th>
            <th class="rec-header">삭제</th>
          </tr>
        </thead>
        <tbody>
          <!-- reSeq 내림차순으로 정렬된 상태입니다.(최근 만든 순으로 보여줌)  -->
          <tr v-for="(recruit, index) in getRecruitListLately" :key="index" class="text-center">
            <td>{{ index+1 }}</td>
            <!-- <td>{{ recruit.reSeq }}</td> -->
            <td>{{ recruit.reYear }} {{ recruit.reFlag }}</td>
            <td>{{ recruit.reStatus }}</td>
            <td>{{ recruit.reStartDate }} ~ {{recruit.reEndDate }}</td>
            <td>
              <!-- <v-btn :to="{ name: 'Progress', params: { recruitNo: recruit.reSeq } }" >관리하기</v-btn> -->
              <v-btn @click="goToProgress(recruit)">관리하기</v-btn>
            </td>
            <td>
              <v-btn @click="goToProgress(recruit.reSeq)">삭제</v-btn>
            </td>

          </tr>
        </tbody>
      </v-simple-table>

    </div>

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
        com_name: "WieV Inc.",
        dialog: false,
        myReno: '',

        dates: ['2021-01-01', '2021-01-01'],
        
        recFormValid: true,
        rangeRules: [
          () => this.dates[0] <= this.dates[1] || '면접 기간이 올바르지 않습니다.'
        ],
        new_recruit: {
          reYear: '',
          reFlag: '',
          reStatus: '',
          reStartDate: '',
          reEndDate: '',
        },
      }
    },

    methods: {
      createRecruit: function () {
        if (this.$refs.form.validate()) {
          this.dialog = false
          // this.dialog = false
          // console.log("createRecruit 실행!")
          // console.log("새로추가된공고정보들:",this.new_recruit)

          this.new_recruit.reStartDate = this.dates[0]
          this.new_recruit.reEndDate = this.dates[1]

          this.$store
            .dispatch("INSERT_RECRUIT", this.new_recruit)
            .then(() => console.log("insertRecruit"))

          //여기에 axios.post 요청으로, DB에 새로운 공고를 저장할 수 있도록 합니다. 
          // 새로 저장된 공고의 정보 (seq포함)를 가져오고, state 에 저장합니다. 
          //this.$store.dispatch('addRecruit', 응답으로 받은 데이터 res.data)
        }
      },
      goToProgress: function (recruit) {
        this.myReno = recruit.reSeq
        this.$store.state.selectedRecruitNo = recruit.reSeq
        console.log("goToProgres!!", this.$store.state.selectedRecruitNo)
        this.$router.push({
          name: 'Progress',
          params: {
            recruitNo: recruit.reSeq,
            recruitInfo: recruit
          }
        })
      },


    },

    created: function () {
      this.$store
        .dispatch("GET_RECRUIT_LIST")
        .then(() => console.log("getRecruitList"))
      console.log(this.$store.state.recruitList);
    },
    computed: {
      ...mapState(["recruitList", "user"]),
      ...mapGetters(["getRecruitListLately"]),

      dateRangeText() {
        return this.dates.join(' ~ ')
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
</style>