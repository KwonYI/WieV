<template>
  <div id="progress">
    <v-simple-table fixed-header height="58vh" class="mt-1 mb-1">
      <thead>
        <tr>
          <th class="text-center">No</th>
          <th class="text-center">날짜</th>
          <th class="text-center">시작 시간</th>
          <th class="text-center">직무</th>
          <th class="text-center">면접 유형</th>
          <th class="text-center">지원자</th>
          <th class="text-center">관리자</th>
          <th class="text-center">면접관</th>
        </tr>
      </thead>
      <v-dialog v-model="detailDialog" width="80%" @click:outside="detailDialog=false">
        <template v-slot:activator="{ on, attrs }">
          <tbody>
            <tr v-for="(item, index) in getProgressListCurrentRecruit" :key="index" class="text-center" v-bind="attrs" v-on="on"
              @click="findDetail(item)">
              <td>{{ index+1 }}</td>
              <td>{{ item.groupDate }}</td>
              <td>{{ item.groupStartTime }} : 00</td>
              <td>{{ item.groupCareerName }}</td>
              <td>
                <span v-for="(view, i) in item.interviewTypeList" :key="i">{{ view }} </span>
              </td>
              <td>
                <div v-if="item.groupApplicantList.length > 3">
                  <span v-for="(viewee, i) in slicedViewee(item.groupApplicantList)" :key="i">{{ viewee }} </span>
                  <br>
                  외 {{ item.groupApplicantList.length - 3 }}명
                </div>
                <div v-else>
                  <span v-for="(viewee, i) in slicedViewee(item.groupApplicantList)" :key="i">{{ viewee }} </span>
                </div>
              </td>
              <td>
                <div v-if="item.waitInterviewerList.length > 2">
                  <span v-for="(guide, i) in slicedGuide(item.waitInterviewerList)" :key="i">{{ guide.interviewerName }} </span>
                  <br>
                  외 {{ item.interviewerList.length - 2 }}명
                </div>
                <div v-else>
                  <span v-for="(guide, i) in slicedGuide(item.waitInterviewerList)" :key="i">{{ guide.interviewerName }} </span>
                </div>
              </td>
              <td>
                <div v-if="item.interviewerList.length > 3">
                  <span v-for="(viewer, i) in slicedViewer(item.interviewerList)" :key="i">{{ viewer.interviewerName }} </span>
                  <br>
                  외 {{ item.interviewerList.length - 3 }}명
                </div>
                <div v-else>
                  <span v-for="(viewer, i) in slicedViewer(item.interviewerList)" :key="i">{{ viewer.interviewerName }} </span>
                </div>
              </td>
            </tr>
          </tbody>
        </template>
        <v-card>
          <v-card-title class="headline main-bg-navy lighten-2">
            세부 그룹 정보
          </v-card-title>

          <v-card-text>
            <v-simple-table fixed-header class="mt-5 text-center">
              <thead>
                <tr>
                  <th class="text-center">No</th>
                  <th class="text-center">날짜</th>
                  <th class="text-center">시작 시간</th>
                  <th class="text-center">직무</th>
                  <th class="text-center">면접 유형</th>
                  <th class="text-center">지원자</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(detail, idx) in detailGroupItem.groupDetailList" :key="idx">
                  <td>{{ idx+1 }}</td>
                  <td>{{ detailGroupItem.groupDate }}</td>
                  <td>{{ detailGroupItem.groupStartTime }} : 00</td>
                  <td>{{ detailGroupItem.groupCareerName }}</td>
                  <td>
                    <span v-for="(view, i) in detail.detailOrder" :key="i">{{ i+1 }}.{{ view }} </span>
                  </td>
                  <td>
                    <span v-for="(viewee, i) in detail.groupDetailApplicant" :key="i">{{ viewee }} </span>
                  </td>
                </tr>
              </tbody>
            </v-simple-table>
            <v-divider></v-divider>
            <v-container>
              <v-row>
                <v-col cols="6">
                  <v-card class="text-center">
                    <h5 class="pa-1"><v-icon left>mdi-text-box-outline</v-icon>관리자 목록</h5>
                    <div v-for="(guide, i) in detailGroupItem.waitInterviewerList" :key="i" class="h6">
                      [{{ guide.interviewType }}]{{ guide.interviewerName }}
                      <v-col class="pa-1"></v-col>
                    </div>
                  </v-card>
                </v-col>
                <v-col cols="6">
                  <v-card class="text-center">
                    <h5 class="pa-1"><v-icon left>mdi-text-box-outline</v-icon>면접관 목록</h5>
                    <div v-for="(type, idx) in detailGroupItem.interviewTypeList" :key="idx" class="h6">
                      [{{ type }}]
                      <span v-for="(viewer, i) in detailGroupItem.interviewerList" :key="i">
                        <span v-if="viewer.interviewType === type">
                          {{ viewer.interviewerName }}
                        </span>
                      </span>
                      <v-col class="pa-1"></v-col>
                    </div>
                  </v-card>
                </v-col>
              </v-row>
            </v-container>
          </v-card-text>

          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="primary" text @click="detailDialog=false">닫기</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-simple-table>
    <v-toolbar dark color="#b0c4de" class="font-weight-bold black--text d-flex justify-content-end">
      <v-toolbar-title></v-toolbar-title>
      <div class="d-flex justify-end">
        <v-btn class="m-2" :loading="loading" :disabled="loading" 
          @click="[loader = 'loading', applicantSendMail()]">지원자 안내메일 전송</v-btn>
        <v-btn class="m-2" :loading="loading2" :disabled="loading2" 
          @click="[loader2 = 'loading2', interviewerSendMail()]">면접관 안내메일 전송</v-btn>          
        <v-btn class="m-2" v-on:click="deleteGroupAll()" color="red"><v-icon left>mdi-trash-can-outline</v-icon>면접일정 초기화</v-btn>
      </div>
    </v-toolbar>
  </div>
</template>

<script>
  import {
    mapState,
    mapGetters
  } from "vuex"
  const SERVER_URL = process.env.VUE_APP_SERVER_URL
  import axios from "axios"
  import _ from "lodash"

  export default {
    name: "Progress",
    props: {
      recruitItem: [Object, String, Number],
    },
    data: function () {
      return {
        detailGroupItem: {},

        loader: null,
        loading: false,

        loader2:null,
        loading2:false,

        detailDialog: false
      }
    },
    created: function () {
      // console.log("getProgressListCurrentRecruit", this.getProgressListCurrentRecruit)
    },
    methods: {
      applicantSendMail: function () {
        this.loading=true
        // console.log(this.recruitItem.reSeq)
        axios.post(`${SERVER_URL}/applicant/send/` + this.recruitItem.reSeq)
          .then(res => {
            this.loading=false
            // console.log(res.data.message)
            if(res.data.message==="지원자 존재하지 않음")
            alert("면접 일정이 존재하지 않습니다.")
            else{
            alert("지원자 메일 전송 성공")
            }
          })
          .catch((err) => {
            this.loading=false
            console.log(err)
            alert("지원자 메일 전송 실패")
          })
          setTimeout(() =>{this.loading=false}, 3000000)
      },
      interviewerSendMail: function () {
        this.loading2=true
        // console.log(this.recruitItem.reSeq)
        axios.post(`${SERVER_URL}/interviewer/send/` + this.recruitItem.reSeq)
          .then(res => {
            this.loading2=false
            // console.log(res)
            if(res.data.message==="면접관 존재하지 않음")
            alert("면접 일정이 존재하지 않습니다.")
            else{
            alert("면접관 메일 전송 성공")
            }
          })
          .catch((err) => {
            this.loading2=false
            console.log(err)
            alert("면접관 메일 전송 실패")
          })
          setTimeout(() =>{this.loading2=false}, 3000000)
      },
      deleteGroupAll: function () {
        if (confirm('모든 면접 일정을 삭제하시겠습니까?') == true) {
          // console.log("삭제됨")
          axios.delete(`${SERVER_URL}/groupAll/delete/` + this.recruitItem.reSeq)
            .then(() => {
              alert(
                `[${this.recruitItem.reYear}년도 ${this.recruitItem.reFlag} ${this.recruitItem.reStatus}]공고의 면접 일정이 모두 삭제되었습니다.`
                )
              this.$store.dispatch("GET_PROGRESS_LIST", this.getUserComSeq)
              this.$store.dispatch("GET_VIEWER_LIST", this.getUserComSeq) //assigned 상태를 refresh해주기 위한 작업
              this.$store.dispatch("GET_VIEWEE_LIST", this.getUserComSeq) //assigned 상태를 refresh해주기 위한 작업
            })
            .catch(() => {
              alert("삭제할 면접 일정이 없습니다.")
            })
        }
      },
      clicked: function (value) {
        this.schGroupTable.expanded.push(value)
      },
      findDetail: function (item) {
        this.detailGroupItem = item
      },
      watch: {
      loader() {
        const l = this.loader
        this[l] = !this[l]

        setTimeout(() => (this[l] = false), 3000)

        this.loader = null
      },
      loader2() {
        const k  = this.loader2
        this[k] = !this[k]

        setTimeout(() => (this[k] = false), 3000)

        this.loader2 = null
      },      
    },
    },

    computed: {
      ...mapState(["recruitList", "recruitProgressList", "selectedRecruitNo"]),
      ...mapGetters(["getProgressListCurrentRecruit", "getUserComSeq"]),

      slicedViewee() {
        return (Viewee) => {
          return _.slice(Viewee, 0, 3)
        }
      },
      slicedGuide() {
        return (Guide) => {
          return _.slice(Guide, 0, 2)
        }
      },
      slicedViewer() {
        return (Viewer) => {
          return _.slice(Viewer, 0, 3)
        }
      }
    },
  }
</script>

<style>
/* table {
  height: 50vh;
} */

</style>