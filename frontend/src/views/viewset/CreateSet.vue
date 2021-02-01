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
              <v-select :items="item.data" :label="item.name" solo hide-details>
              </v-select>
            </v-col>
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
            <v-col class="d-flex justify-center align-center" cols="4">
              <v-dialog v-model="dialog" max-width="500px">
                <!-- 그룹 설정 버튼 -->
                <template v-slot:activator="{ on, attrs }">
                  <v-btn
                    color="secondary"
                    v-bind="attrs"
                    v-on="on"
                  >
                    그룹 설정
                  </v-btn>
                </template>
                <!-- 그룹 설정 Modal -->
                <v-card>
                  <v-card-title>
                    그룹 설정
                  </v-card-title>
                  <v-card-text>
                    <v-row v-for="i in this.formData.groupNum" :key="i" no-gutters>
                      <!-- <v-col v-for=""></v-col> -->
                    </v-row>
                  </v-card-text>
                </v-card>
              </v-dialog>
            </v-col>
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
        
        
        <hr>
        <!-- 스케줄 -->
        
        <v-data-table
          :headers="scheduleTable.headers"
          :items="scheduleTable.schedules"
        >

        </v-data-table>
        
      </v-col>
    </v-row>
  </div>
</template>

<script>
export default {
  name: "CreateSet",
  data() {
    return {
      dialog: false,
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
          name: "면접 종류",
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
          data: [],
          col: 2,
          multiple: false,
          value: 'startDate'
        },
        {
          name: "시간",
          data: ["1시", "2시"],
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
          data: ["7시", "8시"],
          col: 2,
          multiple: false,
          value: 'endTime'
        },
        {
          name: "면접당",
          col: 2
        },
        {
          name: "면접관 수",
          data: ["2명", "3명"],
          col: 3,
          multiple: false,
          value: 'viewerNum'
        },
        {
          name: "지원자 수",
          data: [8, 10],
          col: 3,
          multiple: false,
          value: 'vieweeNum'
        },
        {
          name: "소요 시간",
          data: ["1시간", "2시간"],
          col: 3,
          multiple: false,
          value: 'duration'
        },
        {
          name: "그룹당",
          col: 2,
        },
        {
          name: "지원자 수",
          data: [1, 2, 3, 4],
          col: 3,
          mutiple: false,
          value: 'vieweePerGroup'
        },

      ],
      // 일정 생성 V-Model
      formData: {
        part: '',
        career: '',
        viewTypes: '',
        startDate: '',
        startTime: '',
        endDate: '',
        endTime: '',
        viewerNum: '',
        vieweeNum: 0,
        duration: '',
        vieweePerGroup: 0,
        groupNum: 0
      },
      // 일정 데이터
      scheduleTable: {
        headers: [
          {
            text: 'No.',
            align: 'start',
            sortable: false,
            value: 'scheduleNo',
          },
          { text: '날짜', value: 'scheduleDate' },
          { text: '시간', value: 'scheduleTime' },
          { text: '직무', value: 'scheduleJob' },
          { text: '면접 유형', value: 'scheduleInterview' },
          { text: '지원자', value: 'scheduleViewee' },
          { text: '대기실', value: 'scheduleGuide' },
          { text: '면접실', value: 'scheduleViewer' },
        ],
        schedules: [
          {
            scheduleNo: 1,
            scheduleDate: '7/10',
            scheduleTime: '14:00',
            scheduleJob: '마케팅',
            scheduleInterview: '직무, PT, 그룹',
            scheduleViewee: '',
            scheduleGuide: '',
            scheduleViewer: ''
          }
        ]
      },
      loader: null,
      loading: false,
    }
  },
  methods: {
    addSchedule: function () {
      console.log()
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
      this.formData.groupNum = parseInt(this.formData.vieweeNum/this.formData.vieweePerGroup)
    }
  },
  computed: {
    makeGroup() {
      return this.formData.vieweePerGroup
    }
  }
};
</script>

<style scoped>
.brd {
  border: 0.5px solid lightgray;
}
.v-input input {
  font-size: 10px;
}
</style>