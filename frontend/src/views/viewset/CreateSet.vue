<template>
  <div id="createset">
    <v-container>
      <v-row>
        <div>
          <v-card class="mx-auto mt-10 rounded-b-lg d-flex justify-space-between align-center">
            <div class="d-flex">
              <v-icon large color="blue-grey darken-3" class="ml-5">mdi-calendar-multiple</v-icon>
              <v-card-title>
                [ {{recruitItem.reYear}}년도 {{recruitItem.reFlag}} {{recruitItem.reStatus}} ] 면접스케줄 목록
              </v-card-title>
            </div>
            <div>
              <v-dialog v-model="dialog" width="650" @click:outside="clickOutside">
                <template v-slot:activator="{ on, attrs }">
                  <v-btn rounded class="mr-5" color="blue lighten-1" outlined v-bind="attrs" v-on="on">
                    <v-icon left>mdi-plus-box-multiple</v-icon>면접그룹 생성
                  </v-btn>
                </template>

                <v-card @keyup.enter="addSchedule()">
                  <v-card-title class="headline main-bg-navy lighten-2">
                    <div>면접그룹 생성기 ( {{ recruitItem.reStartDate }} ~ {{ recruitItem.reEndDate }} )</div>
                    <v-spacer></v-spacer>
                    <v-btn rounded class="mr-5" color="#b0c4de" :loading="loading" :disabled="loading" @click="[loader = 'loading', addSchedule()]">
                      생성
                    </v-btn>
                  </v-card-title>

                  <v-card-text>
                    <v-container class="d-flex flex-column" fluid>
                      <v-form ref="form" lazy-validation>
                        <v-simple-table fixed-header class="d-flex flex-column" style="overflow-y: hidden">
                          <template v-slot:default>
                            <v-row class="brd" no-gutters>
                              <v-col v-for="(item, index) in groupForm" :key="index" :cols="item.col" class="d-flex">
                                <v-col  :cols="item.titleCol" class="d-flex justify-center align-center pa-0">
                                  <span class="subtitle-1">{{ item.name }}</span>
                                </v-col>
                                <v-col v-if="index === 0 || index === 1" :cols="item.inputCol" class="d-flex align-center">
                                  <v-autocomplete v-model="formData[item.value]" :items="item.data" :label="item.name" solo
                                    :multiple="item.multiple" @change="partChange($event)" hide-details dense></v-autocomplete>
                                </v-col>
                                <v-col v-if="index === 2" :cols="item.inputCol" class="d-flex align-center">
                                  <v-autocomplete v-model="formData[item.value]" :items="item.data" :label="item.name" solo
                                    :multiple="item.multiple" hide-details dense></v-autocomplete>
                                </v-col>
                                <v-col v-if="index > 2" :cols="item.inputCol" class="d-flex align-center">
                                  <v-text-field :label="item.name" :type="item.type" v-model="formData[item.value]" single-line></v-text-field>
                                </v-col>
                              </v-col>
                            </v-row>
                          </template>
                      </v-simple-table>
                    </v-form>
                    </v-container>
                  </v-card-text>

                <v-card-actions>
                  <v-spacer></v-spacer>
                </v-card-actions>
              </v-card>
            </v-dialog>
            <v-btn text class="mr-5" color="green lighten-1" :to="{ name: 'Menu' }">
              <v-icon left>mdi-application-export</v-icon>면접현황으로
            </v-btn>
            </div>
          </v-card>

          <v-card class="mx-auto mt-10 rounded-b-lg">
            <!-- 스케줄 테이블 -->
            <v-data-table
              :headers="schGroupTable.headers" 
              :items="getProgressListCurrentRecruit"
              :expanded.sync="schGroupTable.expanded"
              single-expand
              item-key="groupSeq"
              @click:row="(item, slot) => slot.expand(!slot.isExpanded)"
            >
              <!-- 스케줄 row -->
              <template v-slot:item="{ item, expand, isExpanded }">
                <tr @click="expand(!isExpanded)">
                  <td>{{ item.groupSeq }}</td>
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
                    <span v-for="(guide, i) in slicedGuide(item.waitInterviewerList)" :key="i">{{ guide.interviewerName }} </span>
                  </td>
                  <td>
                    <span v-for="(viewer, i) in slicedViewer(item.interviewerList)" :key="i">{{ viewer.interviewerName }} </span>
                  </td>
                </tr>
              </template>

              <!-- 세부그룹 확장 패널 -->
              <template v-slot:expanded-item="{ headers, item: groupItem }" id="detailTable">
                <td :colspan="headers.length" class="pa-0">
                  <!-- 세부그룹 테이블 -->
                  <v-data-table :headers="schGroupTable.headers" :items="groupItem.groupDetailList"
                    item-key="schSmallGroupNo" hide-default-footer style="background-color:lightgrey;">
                    <template v-slot:body="{ items }">
                      <tbody>
                        <tr v-for="(item, i) in items" :key="i" style="background-color:ivory;">
                          <td>{{ item.groupDetailSeq }}</td>
                          <td>{{ groupItem.groupDate }}</td>
                          <td>{{ groupItem.groupStartTime }} : 00</td>
                          <td>{{ groupItem.groupCareerName }}</td>
                          <td>
                            <span v-for="(view, i) in item.detailOrder" :key="i">{{ i+1 }}.{{ view }} </span>
                          </td>
                          <td>
                            <span v-for="(viewee, i) in item.groupDetailApplicant" :key="i">{{ viewee }} </span>
                          </td>
                          <td>
                            <span v-for="(guide, i) in slicedGuide(groupItem.waitInterviewerList)" :key="i">{{ guide.interviewerName }} </span>
                          </td>
                          <td>
                            <span v-for="(viewer, i) in slicedViewer(groupItem.interviewerList)" :key="i">{{ viewer.interviewerName }} </span>
                          </td>
                        </tr>
                        <tr>
                          <td v-for="(item, i) in 8" :key="i"></td>
                        </tr>
                      </tbody>
                    </template>
                  </v-data-table>
                </td>
              </template>
            </v-data-table>
          </v-card>
        </div>
      </v-row>
    </v-container>
  </div>

</template>

<script>
  import axios from 'axios'
  import _ from "lodash"
  import { mapGetters, mapState } from "vuex"

  const SERVER_URL = process.env.VUE_APP_SERVER_URL

  export default {
    name: "CreateSet",
    data() {
      return {
        dialog: false,
        // arr : this.getProgressListCurrentRecruit,

        // recruitItem: this.$route.params.recruitItem,
        recruitItem: {},

        // 일정 생성 폼
        groupForm: [{
            name: "부서",
            data: [],
            col: 6,
            titleCol: 6,
            inputCol: 6,
            multiple: false,
            value: 'part'
          },
          {
            name: "직군",
            data: [],
            col: 6,
            titleCol: 6,
            inputCol: 6,
            multiple: false,
            value: 'career'
          },
          {
            name: "면접 유형",
            data: ["인성", "직무", "PT", "토론"],
            col: 12,
            titleCol: 3,
            inputCol: 9,
            multiple: true,
            value: 'viewTypes'
          },
          // {
          //   name: "면접 시작 날짜",
          //   col: 3,
          //   value: 'startDate'
          // },
          {
            name: "시작 시간",
            type: "text",
            col: 6,
            titleCol: 6,
            inputCol: 6,
            value: 'startTime'
          },
          {
            name: "종료 시간",
            type: "text",
            col: 6,
            titleCol: 6,
            inputCol: 6,
            value: 'endTime'
          },
          {
            name: "소요 시간",
            type: "number",
            col: 6,
            titleCol: 6,
            inputCol: 6,
            value: 'duration'
          },
          {
            name: "면접관 수",
            type: "number",
            col: 6,
            titleCol: 6,
            inputCol: 6,
            value: 'viewerNum'
          },
          {
            name: "지원자 수",
            type: "number",
            col: 6,
            titleCol: 6,
            inputCol: 6,
            value: 'vieweeNum'
          },
          {
            name: "세부 그룹 지원자 수",
            type: "number",
            col: 6,
            titleCol: 6,
            inputCol: 6,
            value: 'vieweePerGroup'
          },

        ],
        // 일정 생성 V-Model
        formData: {
          // 부서
          part: '',
          // 직군
          career: '',
          // 면접 유형
          interviewTypeList: [],
          // 시작 날짜, 7/10
          // startDate: '',
          // 시작 시간
          startTime: '',
          // 종료 날짜(안 써도 됨)
          // endDate: '',
          // 종료 시간
          endTime: '',
          // 면접 그룹당 면접관 수, int
          viewerNum: 0,
          // divideInterviewer: 0.1,
          // 면접 그룹당 지원자 수, int
          vieweeNum: 0,
          // divideInterviewee: 0.1,
          // 면접 그룹당 소요 시간, int
          duration: 0,
          // divideTime: '',
          // 블라인드 여부
          // blindView: false,
          // 면접 세부그룹 당 지원자 수, int
          vieweePerGroup: 0,
          // divideDetailNum: 0.1,
          // 면접 그룹당 세부 그룹 수(자동 계산)
          // groupNum: 0
        },


        // 일정 데이터
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
              width: '6%'
            },
            {
              text: '시작 시간',
              align: 'center',
              value: 'schGroupTime',
              width: '7%'
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
              width: '10%'
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
              width: '10%'
            },
          ],
        },
        loader: null,
        loading: false,
      }
    },
    methods: {
      // 면접 스케줄 추가
      addSchedule: function () {
        let groupData = {
          part: this.formData.part,
          career: this.formData.career,
          interviewTypeList: this.formData.viewTypes,
          // startDate: this.formData.startDate,
          startTime: this.formData.startTime,
          endTime: this.formData.endTime,
          divideInterviewer: this.formData.viewerNum,
          divideNumber: this.formData.vieweeNum,
          divideTime: this.formData.duration,
          divideDetailNum: this.formData.vieweePerGroup
        }

        console.log("보내질groupData", groupData)
        axios.post(`${SERVER_URL}/groupAll/divide/` + this.recruitItem.reSeq, groupData)
          .then(res => {
            console.log("추가된면접일정", res.data)
            this.dialog = false
          })
          .catch(err => {
            console.log(err)
          })

        setTimeout(() =>this.$store.dispatch("GET_PROGRESS_LIST", this.getUserComSeq), 2000)

        this.formData.part = ""
        this.formData.career = ""
        this.formData.viewTypes = []
        // this.formData.startDate = ""
        this.formData.startTime = ""
        this.formData.endTime = ""
        this.formData.viewerNum = 0
        this.formData.vieweeNum = 0
        this.formData.duration = 0
        this.formData.vieweePerGroup = 0
      },
      clicked: function (value) {
        this.schGroupTable.expanded.push(value)
      },
      partChange: function (event) {
        for (var i = 0; i < this.$store.state.partCareerList.length; i++) {
          const element = this.$store.state.partCareerList[i]
          if(element.partName === event){
            this.groupForm[1].data = element.careerName
          }
        }
      },
      clickOutside: function() {
        this.formData.part = ""
        this.formData.career = ""
        this.formData.viewTypes = []
        // this.formData.startDate = ""
        this.formData.startTime = ""
        this.formData.endTime = ""
        this.formData.viewerNum = 0
        this.formData.vieweeNum = 0
        this.formData.duration = 0
        this.formData.vieweePerGroup = 0

        this.dialog = false
      }
    },
    watch: {
      loader() {
        const l = this.loader
        this[l] = !this[l]

        setTimeout(() => (this[l] = false), 3000)

        this.loader = null
      },
      makeGroup() {
        _.toInteger()
        this.formData.groupNum = _.ceil(this.formData.vieweeNum / this.formData.vieweePerGroup)
      },

    },
    computed: {

      ...mapState(["user"]),
      ...mapGetters(["getUserComSeq", "getProgressListCurrentRecruit", "getPartCareerListLength", "getPartCareerList"]),


      makeGroup() {
        return [this.formData.vieweePerGroup, this.formData.vieweeNum]
      },
      viewTypesNum() {
        return _.findLastIndex(this.formData.viewTypes) + 1
      },
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
    created() {
      for (var i = 0; i < this.$store.state.partCareerList.length; i++) {
        const element = this.$store.state.partCareerList[i];
        this.groupForm[0].data.push(element.partName)
      }

      this.recruitItem = this.$store.state.storeRecruitItem
    },
    beforeDestroy() {
      this.$store.state.partCareerList = []
    },
  }
</script>

<style scoped>
  /* .schTable::before {
    content: '';
    width: 100%;
    height: 100%;
    position: absolute;
    left: 0;
    top: 0;
    color: black;
  } */

/* .td {
  padding: 0px;
  margin: 0px;
} */

  td {
    text-align: center;
  }


  #createset{
    background-color: aliceblue;
    height: 100%;
  }

  /* table.table {
  margin:0 auto;
  width: 98%;
  max-width: 98%;
}
.datatable-cell-wrapper {
  width: inherit;
  position: relative;
  z-index: 4;
  padding: 10px 24px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.datatable__expand-content .card__text {
  z-index: 3;
  position: relative;
}
.datatable-container {
  position: absolute;
  background-color: white;
  top: -50px;
  left: -14px;
  right: -14px;
  bottom: 0;
  z-index: 2;
  border:1px solid #ccc;
  box-shadow: 0 4px 5px 0 rgba(0,0,0,0.15), 0 1px 10px 0 rgba(0,0,0,0.15), 0 2px 4px -1px rgba(0,0,0,0.2);
} */
</style>