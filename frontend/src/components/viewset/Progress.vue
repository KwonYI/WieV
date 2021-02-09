<template>
  <div id="progress">
    현재 채용공고 번호 : {{ recruitItem.reSeq }}


    <div class="d-flex justify-end">
     

      <v-btn class="m-2" v-on:click="applicantSendMail()"><v-icon left>mdi-email-send-outline</v-icon>지원자 안내메일 전송</v-btn>
      <v-btn class="m-2" v-on:click="interviewerSendMail()"><v-icon left>mdi-email-send-outline</v-icon>면접관 안내메일 전송</v-btn>
      <v-btn class="m-2" v-on:click="deleteGroupAll()"><v-icon left>mdi-trash-can-outline</v-icon>면접일정 초기화</v-btn>
    </div>


    <hr>
    <!-- 스케줄 테이블 -->

    <v-data-table :headers="schGroupTable.headers" :items="getProgressListCurrentRecruit"
      :expanded.sync="schGroupTable.expanded" single-expand item-key="groupSeq"
      @click:row="(item, slot) => slot.expand(!slot.isExpanded)">
      <!-- 스케줄 row -->
      <template v-slot:item="{ item, expand, isExpanded }">
        
        <tr @click="expand(!isExpanded)">
          <td>{{ item.groupSeq }}</td>
          <td>{{ item.groupDate }}</td>
          <td>{{ item.groupStartTime }}:00</td>
          <td>{{ item.groupCareerName }}</td>
          <td>
            <span v-for="(view, i) in item.interviewTypeList" :key="i">{{ view }} </span>
          </td>
          <td>
            <span v-for="(viewee, i) in (item.groupApplicantList)" :key="i">{{ viewee }} </span>
          </td>
          <td>
            <span v-for="(guide, i) in (item.waitInterviewerList)" :key="i">{{ guide.interviewerName }} </span>
          </td>
          <td>
            <span v-for="(viewer, i) in (item.interviewerList)" :key="i">{{ viewer.interviewerName }} </span>
          </td>
        </tr>

      </template>
        

      



      <!-- 세부그룹 확장 패널 -->
      <template v-slot:expanded-item="{ headers, item: groupItem }" id="detailTable" >
        <td :colspan="headers.length" class="pa-0">
          <!-- 세부그룹 테이블 -->
          <v-data-table :headers="schGroupTable.headers" :items="groupItem.groupDetailList" item-key="schSmallGroupNo"
            hide-default-footer style="background-color:aliceblue;" >

            <template v-slot:body="{ items }">
              <tbody >
              <!-- <tbody background-color="blue-grey lighten-4"> -->
                <tr v-for="(item, i) in items" :key="i" style="background-color:ivory;" >
                  <td>{{ item.groupDetailSeq }}</td>
                  <td>{{ groupItem.groupDate }}</td>
                  <td>{{ groupItem.groupStartTime }}:00</td>
                  <td>{{ groupItem.groupCareerName }}</td>
                  <td>
                    <span v-for="(view, i) in item.detailOrder" :key="i">{{i+1}}.{{ view }} </span>
                  </td>
                  <td>
                    <span v-for="(viewee, i) in (item.groupDetailApplicant)" :key="i">{{ viewee }}
                    </span>
                  </td>
                  <td>
                    <span v-for="(guide, i) in (groupItem.waitInterviewerList)"
                      :key="i">[{{guide.interviewType}}]{{ guide.interviewerName }},
                    </span>
                  </td>
                  <td>
                    <span v-for="(viewer, i) in (groupItem.interviewerList)"
                      :key="i">[{{viewer.interviewType}}]{{ viewer.interviewerName }},
                    </span>
                  </td>
                  
                </tr>
                <tr>
                  <td v-for="(item, i) in 8" :key="i"><hr></td>
                </tr>
                
              </tbody>
              
              
            </template>
            

          </v-data-table>
        </td>


      </template>

      


    </v-data-table>
    









  </div>
</template>

<script>
  import {
    mapState,
    mapGetters
  } from "vuex"
  const SERVER_URL = "https://localhost:8080"
  import axios from "axios"
  import _ from "lodash"

  export default {
    name: "Progress",
    props: {
      recruitItem: [Object, String, Number],
    },
    data: function () {
      return {
        schGroupTable: {
          expanded: [],
          headers: [{
              text: 'No.',
              align: 'center',
              sortable: false,
              value: 'schGroupNo',
              width: '1%'
            },
            {
              text: '날짜',
              align: 'center',
              value: 'schGroupDate',
              width: '5%'
            },
            {
              text: '시간',
              align: 'center',
              value: 'schGroupTime',
              width: '5%'
            },
            {
              text: '직무',
              align: 'center',
              value: 'schGroupCareer',
              width: '5%'
            },
            {
              text: '면접 유형',
              align: 'center',
              value: 'schGroupInterview',
              width: '10%'
            },
            {
              text: '지원자',
              align: 'center',
              value: 'schGroupViewee',
              width: '20%'
            },
            {
              text: '대기실',
              align: 'center',
              value: 'schGroupGuide',
              width: '10%'
            },
            {
              text: '면접실',
              align: 'center',
              value: 'schGroupViewer',
              width: '15%'
            },
          ],

        },


      }
    },
    created: function () {
      console.log("getProgressListCurrentRecruit", this.getProgressListCurrentRecruit)

    },
    methods: {
      applicantSendMail: function () {
        console.log(this.recruitItem.reSeq)
        axios.post(`${SERVER_URL}/applicant/send/` + this.recruitItem.reSeq)
          .then(res => {
            console.log(res)
            alert("지원자 메일 전송 성공")
            this.$router.push({
              name: "Home"
            })
          })
          .catch((err) => {
            console.log(err)
            alert("지원자 메일 전송 실패")
          })
      },
      interviewerSendMail: function () {
        console.log(this.recruitItem.reSeq)
        axios.post(`${SERVER_URL}/interviewer/send/` + this.recruitItem.reSeq)
          .then(res => {
            console.log(res)
            alert("면접관 메일 전송 성공")
            this.$router.push({
              name: "Home"
            })
          })
          .catch((err) => {
            console.log(err)
            alert("면접관 메일 전송 실패")
          })
      },
      deleteGroupAll: function () {
        if (confirm('모든 면접 일정을 삭제하시겠습니까?') == true) {
          console.log("삭제됨")
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




    },
    computed: {
      ...mapState(["recruitList", "recruitProgressList", "selectedRecruitNo"]),
      ...mapGetters(["getProgressListCurrentRecruit", "getUserComSeq"]),

      slicedViewee() {
        return (Viewee) => {
          return _.slice(Viewee, 0, 5)
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
</style>