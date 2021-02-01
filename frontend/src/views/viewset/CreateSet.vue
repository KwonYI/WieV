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
          <v-row class="brd" justify="space-around" no-gutters>
            <v-col
              v-for="(item, index) in groupForm"
              :key="index"
              :cols="item.col"
              class="d-flex"
            >
              <v-col class="d-flex justify-end align-center pa-0">
                <div class="mr-3">{{ item.name }}</div>
              </v-col>
              <v-col v-if="item.data" cols="8" class="d-flex align-center">
                <v-autocomplete
                  :items="item.data"
                  :label="item.name"
                  solo
                  :multiple="item.multiple"
                  hide-details
                  dense
                ></v-autocomplete>
              </v-col>
            </v-col>
          </v-row>
        </v-col>
        <v-btn
          class="ma-2"
          :loading="loading"
          :disabled="loading"
          color="secondary"
          @click="loader = 'loading'"
        >
          그룹 스케줄 생성
        </v-btn>
        <hr>
        <!-- 스케줄 -->
        <v-col>스케줄</v-col>
      </v-col>
    </v-row>
  </div>
</template>

<script>
export default {
  name: "CreateSet",
  data() {
    return {
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
      groupForm: [
        {
          name: "직군",
          data: ["SW 개발", "마케팅", "해외영업"],
          col: 3,
          multiple: false,
        },
        { name: "진행 일정", data: [], col: 5, multiple: false },
        { name: "시작 시간", data: ["1시", "2시"], col: 3, multiple: false },
        {
          name: "면접 종류",
          data: ["직무", "인성", "PT", "토론"],
          col: 4,
          multiple: true,
        },
        { name: "면접당", col: 1 },
        { name: "지원자 수", data: ["1명", "2명"], col: 3, multiple: false },
        {
          name: "소요 시간",
          data: ["1시간", "2시간"],
          col: 3,
          multiple: false,
        },
      ],
      loader: null,
      loading: false,
    };
  },
  watch: {
    loader() {
      const l = this.loader;
      this[l] = !this[l];

      setTimeout(() => (this[l] = false), 3000);

      this.loader = null;
    },
  },
};
</script>

<style scoped>
.brd {
  border: 0.5px solid lightgray;
}
</style>