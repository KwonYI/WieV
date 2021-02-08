<template>
  <div id="createset" class="mt-12 mx-14">
    <div class="h4">신규 면접스케줄 생성</div>
    <!-- 스케줄생성 Form -->
    <v-row justify="center" no-gutters>
      <v-col cols="10" class="mt-10">
        <!-- 시즌 정보 입력 -->
        <v-col>
          <v-row justify="space-around" no-gutters>
            <v-col
              v-for="(item, index) in seasonData"
              :key="index"
              :cols="item.col"
            >
              <v-select :items="item.data" :label="item.name" solo hide-details disabled>
              </v-select>
            </v-col>
            {{ recruitItem }}
          </v-row>
        </v-col>
        <!-- 스케줄 자동생성 입력 -->
        <v-col class="mt-5 mb-0 h5">그룹 생성기</v-col>
        <v-col>
          <v-row class="brd" style="justify-between" no-gutters>
            <v-col
              v-for="(item, index) in groupForm"
              :key="index"
              :cols="item.col"
              class="d-flex"
            >
              <v-col class="d-flex justify-center align-center pa-0">
                <span class="subtitle-1">{{ item.name }}</span>
              </v-col>
              <v-col v-if="item.data" cols="8" class="d-flex align-center">
                <v-autocomplete
                  v-model="formData[item.value]"
                  :items="item.data"
                  :label="item.name"
                  solo
                  :multiple="item.multiple"
                  hide-details
                  dense
                ></v-autocomplete>
              </v-col>
            </v-col>

            <!--
            <v-col class="d-flex justify-center align-center" cols="4">
              <v-dialog v-model="dialog" max-width="500px">
                <template v-slot:activator="{ on, attrs }">
                  <v-btn
                    color="secondary"
                    v-bind="attrs"
                    v-on="on"
                  >
                    그룹 설정
                  </v-btn>
                </template>
                <v-card>
                  <v-card-title>
                    그룹 설정
                  </v-card-title>
                  <v-card-text>
                    <v-row v-for="i in this.formData.groupNum" :key="i" no-gutters>
                      <v-col v-for="j in viewTypesNum" :key="j">
                        <v-select
                          :items="formData.viewTypes"
                          solo
                        ></v-select>
                      </v-col>
                    </v-row>
                  </v-card-text>
                </v-card>
              </v-dialog>
            </v-col>
            -->

            <v-col cols="3" class="d-flex justify-center align-center">
              <v-btn
                class="ma-2"
                :loading="loading"
                :disabled="loading"
                color="secondary"
                @click="[loader = 'loading', addSchedule()]"
              >
                면접일정 추가
              </v-btn>
            </v-col>
          </v-row>
        </v-col>
        
        <a-date-picker @change="change" />
        <a-time-picker format="HH:mm"/>
        
        <hr>
        <!-- 스케줄 테이블 -->
        <v-data-table
          :headers="schGroupTable.headers"
          :items="schGroupTable.schGroups"
          :expanded.sync="schGroupTable.expanded"
          single-expand
          item-key="schGroupNo"
          @click:row="(item, slot) => slot.expand(!slot.isExpanded)"
        >
          <!-- 스케줄 메인 row -->
          <template v-slot:item="{ item, expand, isExpanded }">
            <tr @click="expand(!isExpanded)">
              <td>{{ item.schGroupNo }}</td>
              <td>{{ item.schGroupDate }}</td>
              <td>{{ item.schGroupTime }}</td>
              <td>{{ item.schGroupCareer }}</td>
              <td>
                <span v-for="(view, i) in item.schGroupInterview" :key="i">{{ view }} </span>
              </td>
              <td>
                <span v-for="(viewee, i) in slicedViewee(item.schGroupViewee)" :key="i">{{ viewee }} </span>
              </td>
              <td>
                <span v-for="(guide, i) in slicedGuide(item.schGroupGuide)" :key="i">{{ guide }} </span>
              </td>
              <td>
                <span v-for="(viewer, i) in slicedViewer(item.schGroupViewer)" :key="i">{{ viewer }} </span>
              </td>
            </tr>
          </template>

          <!-- 세부그룹 확장 패널 -->
          <template v-slot:expanded-item="{ headers, item: groupItem }">
            <td :colspan="headers.length" class="pa-0">
              <!-- 세부그룹 테이블 -->
              <v-data-table
                :headers="schGroupTable.headers"
                :items="groupItem.schSmallGroups"
                item-key="schSmallGroupNo"
                hide-default-footer
              >
                <!-- <template v-slot:header="{ headers }">
                  <thead>
                    <tr :colspan="headers.length">
                    </tr>
                  </thead>
                </template> -->

                <template v-slot:body="{ items }">
                  <tbody>
                    <tr v-for="(item, i) in items" :key="i">
                      <td>{{ item.schSmallGroupNo }}</td>
                      <td>{{ groupItem.schGroupDate }}</td>
                      <td>{{ groupItem.schGroupTime }}</td>
                      <td>{{ groupItem.schGroupCareer }}</td>
                      <td>
                        <span v-for="(view, i) in item.schSmallGroupInterview" :key="i">{{ view }} </span>
                      </td>
                      <td>
                        <span v-for="(viewee, i) in slicedViewee(item.schSmallGroupViewee)" :key="i">{{ viewee }} </span>
                      </td>
                      <td>
                        <span v-for="(guide, i) in slicedGuide(groupItem.schGroupGuide)" :key="i">{{ guide }} </span>
                      </td>
                      <td>
                        <span v-for="(viewer, i) in slicedViewer(groupItem.schGroupViewer)" :key="i">{{ viewer }} </span>
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

const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  name: "CreateSet",
  data() {
    return {
      dialog: false,

      recruitItem: this.$route.params.recruitItem,

      // 시즌 데이터
      seasonData: [

        {
          name: "시즌",
          data: ["2020 하반기", "2021 상반기", "2021 하반기"],
          col: 2,
        },
        { name: "유형", data: ["공채", "수시", "상시"], col: 2 },
        { name: "분류", data: ["신입", "경력", "계약", "인턴"], col: 2 },
        { name: "기간", data: [], col: 3 },
      ],
      // 일정 생성 폼
      groupForm: [
        {
          name: "부서",
          data: ["전략기획실", "IT영업", "브랜드마케팅"],
          col: 3,
          multiple: false,
          value: 'part'
        },
        {
          name: "직군",
          data: ["SW 개발", "마케팅", "전략기획"],
          col: 3,
          multiple: false,
          value: 'career'
        },
        {
          name: "면접 유형",
          data: ["직무", "인성", "PT", "토론"],
          col: 5,
          multiple: true,
          value: 'viewTypes'
        },
        {
          name: "시작",
          col: 2
        },
        {
          name: "날짜",
          data: ['7/22'],
          col: 2,
          multiple: false,
          value: 'startDate'
        },
        {
          name: "시간",
          data: ["13", "14"],
          col: 2,
          multiple: false,
          value: 'startTime'
        },
        {
          name: "종료",
          col: 2
        },
        {
          name: "날짜",
          data: [],
          col: 2,
          multiple: false,
          value: 'endDate'
        },
        {
          name: "시간",
          data: ["19", "20"],
          col: 2,
          multiple: false,
          value: 'endTime'
        },
        {
          name: "면접 그룹",
          col: 2
        },
        {
          name: "면접관 수",
          data: [2, 3, 4],
          col: 3,
          multiple: false,
          value: 'viewerNum'
        },
        {
          name: "지원자 수",
          data: [8, 9, 10, 11, 12],
          col: 3,
          multiple: false,
          value: 'vieweeNum'
        },
        {
          name: "소요 시간",
          data: [1, 2, 3, 4],
          col: 3,
          multiple: false,
          value: 'duration'
        },
        {
          name: "면접 세부 그룹",
          col: 2,
        },
        {
          name: "지원자 수",
          data: [1, 2, 3, 4, 5, 6],
          col: 3,
          mutiple: false,
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
        viewTypes: [],
        // 시작 날짜, 7/10
        startDate: '',
        // 시작 시간
        startTime: '',
        // 종료 날짜(안 써도 됨)
        // endDate: '',
        // 종료 시간
        endTime: '',
        // 면접 그룹당 면접관 수, int
        viewerNum: 0.1,
        // divideInterviewer: 0.1,
        // 면접 그룹당 지원자 수, int
        vieweeNum: 0.1,
        // divideInterviewee: 0.1,
        // 면접 그룹당 소요 시간, int
        duration: '',
        // divideTime: '',
        // 블라인드 여부
        // blindView: false,
        // 면접 세부그룹 당 지원자 수, int
        vieweePerGroup: 0.1,
        // divideDetailNum: 0.1,
        // 면접 그룹당 세부 그룹 수(자동 계산)
        // groupNum: 0
      },
      // 일정 데이터
      schGroupTable: {
        expanded: [],
        headers: [
          {
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
        // 면접 그룹
        schGroups: [
          {
            // 면접 그룹 seq
            schGroupNo: 1,
            // 면접 그룹 시작 날짜
            schGroupDate: '7/10',
            // 면접 그룹 시작 시간
            schGroupTime: '14:00',
            // 면접 그룹 직군
            schGroupCareer: '마케팅',
            // 면접 그룹 면접 종류
            schGroupInterview: ['직무', 'PT', '그룹'],
            // 면접 그룹 지원자(면접자)
            schGroupViewee: ['김일번', '박이번', '유삼번', '이사번', '권오번', '천육번', '강칠번', '하팔번'],
            // 면접 그룹 대기관
            schGroupGuide: ['강안내'],
            // 면접 그룹 면접관
            schGroupViewer: ['김면접', '이채용', '신평가'],
            // 면접 세부 그룹
            schSmallGroups: [
              {
                schSmallGroupNo: 1,
                // 세부그룹 면접 순서
                schSmallGroupInterview: ['직무', 'PT', '그룹'],
                // 세부 그룹의 지원자(면잡자)
                schSmallGroupViewee: ['김일번', '유삼번', '천육번', '강칠번'],
              },
              {
                schSmallGroupNo: 2,
                schSmallGroupInterview: ['그룹', '직무', 'PT'],
                schSmallGroupViewee: ['박이번', '이사번', '권오번', '하팔번'],
              }
            ]
          }
        ]
      },
      

      loader: null,
      loading: false,
    }
  },
  methods: {
    // 면접 스케줄 추가
    addSchedule: function () {
      // axios.post(`https://localhost:8080/groupAll/divide` + this.recruitNo, this.formData)
      let groupData = {
        part: this.formData.part,
        career: this.formData.career,
        interviewTypeList: this.formData.viewTypes,
        startDate: this.formData.startDate,
        startTime: this.formData.startTime,
        endTime: this.formData.endTime,
        divideInterviewer: this.formData.viewerNum,
        divideInterviewee: this.formData.vieweeNum,
        divideTime: this.formData.duration,
        divideDetailNum: this.formData.vieweePerGroup
      }

      axios.post(`${SERVER_URL}/groupAll/divide/` + this.recruitItem.reSeq, groupData)
        .then(res => {
          console.log(res)
        })
        .catch(err => console.log(err))
    },
    clicked: function (value) {
      this.schGroupTable.expanded.push(value)
    },
    change: function () {
      
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
      this.formData.groupNum = _.ceil(this.formData.vieweeNum/this.formData.vieweePerGroup)
    },
    
  },
  computed: {
    makeGroup() {
      return [this.formData.vieweePerGroup, this.formData.vieweeNum]
    },
    viewTypesNum() {
      return _.findLastIndex(this.formData.viewTypes) + 1
    },
    slicedViewee() {
      return (Viewee) => { return _.slice(Viewee, 0, 5) }
    },
    slicedGuide() {
      return (Guide) => { return _.slice(Guide, 0, 2) }
    },
    slicedViewer() {
      return (Viewer) => { return _.slice(Viewer, 0, 3) }
    }
  },
  created () {
    
  }
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