<template>
  <div id="schedule">
    <!--스케줄 (공고 리스트) 컴포넌트 // 나중에 라우터로 변경 될 수 있다. -->
    <h3>반갑습니다. {{com_name}}님</h3>

    <div>
      <!--데이터 테이블 <v-data-table :headers="headers" :items="recruits" class="elevation-1">
        <template v-slot:[`item.re_flag`]="{ item }">
          <v-chip :color="getColor(item.re_flag)" dark>
            {{ item.calories }}
          </v-chip>
        </template>
      </v-data-table> -->

      <v-dialog v-model="dialog" persistent max-width="600px">
        <template v-slot:activator="{ on, attrs }">
          <v-btn color="primary" dark v-bind="attrs" v-on="on">
            공고 추가하기
          </v-btn>
        </template>
        <v-card>
          <v-card-title>
            <span class="headline">[{{com_name}}] 신규 공고 등록</span>
          </v-card-title>
          <v-card-text>
            <v-container>
              <v-row>
                <v-col cols="12" sm="6" md="4">
                  <v-select :items="[2021, 2020]" label="년도" required v-model="new_recruit.reYear"></v-select>
                </v-col>
                <v-col cols="12" sm="6" md="4">
                  <v-select :items="['상반기', '하반기']" label="시즌" required v-model="new_recruit.reFlag"></v-select>
                </v-col>
                <v-col cols="12" sm="6" md="4">
                  <v-select :items="['신입', '경력']" label="채용 형태" required v-model="new_recruit.reStatus"></v-select>
                </v-col>

                <v-col cols="12" sm="6">
                  <v-text-field v-model="dateRangeText" label="공고 면접 기간" prepend-icon="mdi-calendar" readonly>
                  </v-text-field>
                  <div>면접 시작일 : {{ dates[0] }}</div>
                  <div>면접 종료일 : {{ dates[1] }}</div>
                </v-col>
                <v-col cols="12" sm="6">
                  <v-date-picker v-model="dates" range></v-date-picker>
                  
                </v-col>

              
              </v-row>
            </v-container>
            <small>*indicates required field</small>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" text @click="dialog = false">
              Close
            </v-btn>
            <v-btn color="blue darken-1" text @click="createRecruit">
              Save
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>


      <v-simple-table fixed-header class="mt-5">
        <thead>
          <tr>
            <th class="text-center">시즌</th>
            <th class="text-center">분류</th>
            <th class="text-center">기간</th>
            <th class="text-center">관리</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="recruit in recruitList" :key="recruit.reSeq" class="text-center">
            <td>{{ recruit.reYear }} {{ recruit.reFlag }}</td>
            <td>{{ recruit.reStatus }}</td>
            <td>{{ recruit.reStartDate }} ~ {{recruit.reEndDate }}</td>
            <td>
              <v-btn>관리하기</v-btn>
            </td>
            <!--여기에 recruitMenu라우터 연결해야 합니다.-->
          </tr>
        </tbody>
      </v-simple-table>

    </div>

  </div>

</template>

<script>
  import {
    mapState
  } from "vuex";

  export default {
    name: "Schedule",
    data: function () {
      return {
        com_name: "버즈글로벌",
        dialog: false,

        dates: ['2021-01-01', '2021-01-01'],
        new_recruit :{
          reYear:'',
          reFlag:'',
          reStatus:'',
          reStartDate:'',
          reEndDate:'',
        }

      }
    },

    methods: {
      createRecruit: function () {
        this.dialog = false;
        this.new_recruit.reStartDate = this.dates[0]
        this.new_recruit.reEndDate = this.dates[1]
        console.log("createRecruit 실행!")
        console.log("새로추가된공고정보들:",this.new_recruit)

        



        //여기에 axios.post 요청으로, DB에 새로운 공고를 저장할 수 있도록 합니다. 
        // 새로 저장된 공고의 정보 (seq포함)를 가져오고, state 에 저장합니다. 
        //this.$store.dispatch('addRecruit', 응답으로 받은 데이터 res.data)

      }

    },
    created: function () {

    },
    computed: {
      ...mapState(["recruitList"]),
      dateRangeText() {
        return this.dates.join(' ~ ')
      },
    },
  }
</script>

<style>
</style>