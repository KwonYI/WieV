<template>
  <div id="viewees">
    <v-toolbar dark color="teal">
      <v-toolbar-title>지원자 찾기</v-toolbar-title>
      <v-autocomplete
        v-model="select"
        :loading="loading"
        :items="items"
        :search-input.sync="search"
        cache-items
        class="mx-4"
        flat
        hide-no-data
        hide-details
        label="지원자 이름 검색"
        solo-inverted
      ></v-autocomplete>
      <v-btn class="m-3"> 검색 </v-btn>
      <v-btn class="m-3"> DB업데이트 </v-btn>
    </v-toolbar>
    <v-simple-table fixed-header class="mt-5">
      <thead>
        <tr>
          <th class="text-center">No</th>
          <th class="text-center">직군</th>
          <th class="text-center">이름</th>
          <th class="text-center">연락처</th>
          <th class="text-center">생년월일</th>
          <th class="text-center">이메일</th>
          <th class="text-center">자기소개서</th>
          <th class="text-center">이력서</th>
        </tr>
      </thead>
      <tbody>
        <tr
          v-for="recruit in recruits[reno - 1]"
          :key="recruit.no"
          class="text-center"
        >
          <td>{{ recruit.no }}</td>
          <td>{{ recruit.ca }}</td>
          <td>{{ recruit.name }}</td>
          <td>{{ recruit.phone }}</td>
          <td>{{ recruit.birth }}</td>
          <td>{{ recruit.email }}</td>
          <td><v-btn>자기소개서</v-btn></td>
          <td><v-btn>이력서</v-btn></td>
        </tr>
      </tbody>
    </v-simple-table>
  </div>
</template>

<script>
export default {
  name: "Viewees",
  data: function () {
    return {
      search: null,
      select: null,
      items: [],
      reno: "",
      recruits: [
        [
          {
            no: 1,
            name: "김지원",
            email: "apply123@naver.com",
            phone: "010-0000-1234",
            ca: "SW개발",
            birth: "94-09-15",
          },
          {
            no: 2,
            name: "김지원",
            ca: "SW개발",
            email: "apply123@naver.com",
            phone: "010-0000-1234",
            password: "1234",
            birth: "94-09-15",
          },
          {
            no: 3,
            name: "김지원",
            ca: "마케팅",
            email: "apply123@naver.com",
            phone: "010-0000-1234",
            password: "1234",
            birth: "94-09-15",
          },
          {
            no: 4,
            name: "김지원",
            ca: "회로개발",
            email: "apply123@naver.com",
            phone: "010-0000-1234",
            password: "1234",
            birth: "94-09-15",
          },
        ],

        [
          {
            no: 5,
            name: "이지원",
            email: "apply123@naver.com",
            phone: "010-0000-1234",
            password: "1234",
            ca: "SW개발",
            birth: "94-09-15",
          },
          {
            no: 6,
            name: "이지원",
            email: "apply123@naver.com",
            phone: "010-0000-1234",
            password: "1234",
            ca: "SW개발",
            birth: "94-09-15",
          },
          {
            no: 7,
            name: "이지원",
            email: "apply123@naver.com",
            phone: "010-0000-1234",
            password: "1234",
            ca: "인사",
            birth: "94-09-15",
          },
          {
            no: 8,
            name: "이지원",
            email: "apply123@naver.com",
            phone: "010-0000-1234",
            password: "1234",
            ca: "VOC",
            birth: "94-09-15",
          },
        ],

        [
          {
            no: 9,
            name: "박지원",
            email: "apply123@naver.com",
            phone: "010-0000-1234",
            password: "1234",
            ca: "SW개발",
            birth: "94-09-15",
          },
          {
            no: 10,
            name: "박지원",
            email: "apply123@naver.com",
            phone: "010-0000-1234",
            password: "1234",
            ca: "QA",
            birth: "94-09-15",
          },
          {
            no: 11,
            name: "박지원",
            email: "apply123@naver.com",
            phone: "010-0000-1234",
            password: "1234",
            ca: "총무",
            birth: "94-09-15",
          },
          {
            no: 12,
            name: "박지원",
            email: "apply123@naver.com",
            phone: "010-0000-1234",
            password: "1234",
            ca: "리서치",
            birth: "94-09-15",
          },
        ],
      ],
    };
  },
  created: function () {
    // this.reno = this.$route.params.recruitNo;
    this.reno = this.$store.state.selectedRecruitNo;
    console.log("reno:", this.reno);
  },
};
</script>

<style>
</style>