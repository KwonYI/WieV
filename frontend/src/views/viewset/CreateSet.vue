<template>
  <div id="createset" class="mt-12 mx-14">
    <div class="h4 text-center">
      [ {{ user.userComName }} {{ recruitItem.reYear }}년 {{ recruitItem.reFlag }} {{ recruitItem.reStatus }} ] 신규 면접 스케줄 생성
      <br><br>
      {{ recruitItem.reStartDate }} ~ {{ recruitItem.reEndDate }} 
      <br><br>
      <div class="h6">
        <router-link :to="{ name: 'Menu' }">
         면접 현황으로 돌아가기
        </router-link>
      </div>
    </div>
    <!-- 스케줄생성 Form -->
    <v-row justify="center" no-gutters>
      <v-col cols="10" class="mt-10">
        <!-- 스케줄 자동생성 입력 -->
        <v-card class="mx-auto mt-10 rounded-b-lg">
          <v-card-title>
            <div class="h5" style="margin: 10px">면접 그룹 생성기</div>
            <!-- <v-col cols="3" class="d-flex justify-center align-center"> -->
              <v-btn class="mr-5" :loading="loading" :disabled="loading" color="secondary"
                @click="[loader = 'loading', addSchedule()]">
                면접일정 추가
              </v-btn>
            <!-- </v-col> -->
          </v-card-title>
          <!-- <hr style="margin: 0px auto"> -->
          <v-row class="brd" no-gutters>
            <v-col v-for="(item, index) in groupForm" :key="index" :cols="item.col" class="d-flex">
              <v-col class="d-flex justify-center align-center pa-0">
                <span class="subtitle-1">{{ item.name }}</span>
              </v-col>
              <v-col v-if="index === 0" cols="8" class="d-flex align-center">
                <v-autocomplete v-model="formData[item.value]" :items="item.data" :label="item.name" solo
                  :multiple="item.multiple" @change="partChange($event)" hide-details dense></v-autocomplete>
              </v-col>
              <v-col v-if="index === 1 || index === 2" cols="8" class="d-flex align-center">
                <v-autocomplete v-model="formData[item.value]" :items="item.data" :label="item.name" solo
                  :multiple="item.multiple" hide-details dense></v-autocomplete>
              </v-col>
              <!-- <v-col v-if="index === 3" cols="6" class="d-flex align-center">
                <a-date-picker @change="change"  v-model="formData[item.value]"/>
              </v-col> -->
              <!-- <v-col v-if="index === 3 || index === 4" cols="6" class="d-flex align-center">
                <a-time-picker format="HH" v-model="formData[item.value]"/>
              </v-col> -->
              <v-col v-if="index > 2" cols="6" class="d-flex align-center">
                <v-text-field :label="item.name" :type="item.type" v-model="formData[item.value]" single-line></v-text-field>
              </v-col>
            </v-col>
          </v-row>
        </v-card>

        <hr>
        <!-- 스케줄 테이블 -->
        <!-- <v-data-table :headers="schGroupTable.headers" :items="schGroupTable.schGroups" -->
        <v-data-table
          :headers="schGroupTable.headers" :items="getProgressListCurrentRecruit"
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
              <td>{{ item.groupStartTime }}</td>
              <td>{{ item.groupCareerName }}</td>
              <td>
                <span v-for="(view, i) in item.interviewTypeList" :key="i">{{ view }} </span>
              </td>
              <td>
                <span v-for="(viewee, i) in slicedViewee(item.groupApplicantList)" :key="i">{{ viewee }} </span>
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
                item-key="schSmallGroupNo" hide-default-footer>
                <!-- <template v-slot:header="{ headers }">
                  <thead>
                    <tr :colspan="headers.length">
                    </tr>
                  </thead>
                </template> -->

                <template v-slot:body="{ items }">
                  <tbody>
                    <tr v-for="(item, i) in items" :key="i">
                      <td>{{ item.groupDetailSeq }}</td>
                      <td>{{ groupItem.groupDate }}</td>
                      <td>{{ groupItem.groupStartTime }}</td>
                      <td>{{ groupItem.groupCareerName }}</td>
                      <td>
                        <span v-for="(view, i) in item.detailOrder" :key="i">{{ view }} </span>
                      </td>
                      <td>
                        <span v-for="(viewee, i) in slicedViewee(item.groupDetailApplicant)" :key="i">{{ viewee }}
                        </span>
                      </td>
                      <td>
                        <span v-for="(guide, i) in slicedGuide(groupItem.waitInterviewerList)" :key="i">{{ guide.interviewerName }}
                        </span>
                      </td>
                      <td>
                        <span v-for="(viewer, i) in slicedViewer(groupItem.interviewerList)" :key="i">{{ viewer.interviewerName }}
                        </span>
                      </td>
                    </tr>
                  </tbody>
                </template>
              </v-data-table>
            </td>
          </template>

        </v-data-table>
      </v-col>
    </v-row>
  </div>
</template>

<script>
  import axios from 'axios'
  import _ from "lodash"
  import { mapGetters, mapState } from "vuex"

  // const SERVER_URL = "https://localhost:8080"
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
            col: 4,
            multiple: false,
            value: 'part'
          },
          {
            name: "직군",
            data: [],
            col: 4,
            multiple: false,
            value: 'career'
          },
          {
            name: "면접 유형",
            data: ["인성", "직무", "PT", "토론"],
            col: 4,
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
            col: 4,
            value: 'startTime'
          },
          {
            name: "종료 시간",
            type: "text",
            col: 4,
            value: 'endTime'
          },
          {
            name: "소요 시간",
            type: "number",
            col: 4,
            value: 'duration'
          },
          {
            name: "면접관 수",
            type: "number",
            col: 4,
            value: 'viewerNum'
          },
          {
            name: "지원자 수",
            type: "number",
            col: 4,
            value: 'vieweeNum'
          },
          {
            name: "세부 그룹 지원자 수",
            type: "number",
            col: 4,
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
          startDate: '',
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
          startDate: this.formData.startDate,
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
          })
          .catch(err => console.log(err))

        setTimeout(() =>this.$store.dispatch("GET_PROGRESS_LIST", this.getUserComSeq), 2000)


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



  td {
    text-align: center;
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