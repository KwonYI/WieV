<template>
  <div id="progress">
    <v-simple-table fixed-header class="mt-5">
      <thead>
        <tr>
          <th class="text-center">날짜</th>
          <th class="text-center">시작시간</th>
          <th class="text-center">직무</th>
          <th class="text-center">면접유형</th>
          <th class="text-center">지원자</th>
          <th class="text-center">대기관</th>
          <th class="text-center">면접관</th>
          <th class="text-center">안내메일</th>
        </tr>
      </thead>
      <tbody>
        <tr
          v-for="recruit in recruits[reno - 1]"
          :key="recruit.no"
          class="text-center"
        >
          <td>{{ recruit.group_date }}</td>
          <td>{{ recruit.group_start_time }}</td>
          <td>{{ recruit.ca }}</td>
          <td>{{ recruit.types }}</td>
          <td>{{ recruit.applicants }}</td>
          <td>{{ recruit.view_wait }}</td>
          <td>{{ recruit.interviewers }}</td>
          <td>
            <v-checkbox color="indigo"></v-checkbox>
          </td>
        </tr>
      </tbody>
    </v-simple-table>
    <div class="d-flex justify-end">
    <v-btn>안내메일전송</v-btn>
     </div>
  </div>
</template>

<script>
export default {
  name: "Progress",
  data: function () {
    return {
      reno: "",
      recruits: [
        [
          {
            no: 1,
            group_date: "07/20",
            group_start_time: "10:00",
            ca: "SW개발",
            types: "직무, PT, 토론",
            applicants: "김지원 외 49",
            view_wait: "김대기 외 5",
            interviewers: "김면접 외 14",
            infomail: "",
          },
          {
            no: 2,
            group_date: "07/20",
            group_start_time: "10:00",
            ca: "마케팅",
            types: "직무, PT, 토론",
            applicants: "김지원 외 49",
            view_wait: "김대기 외 5",
            interviewers: "김면접 외 14",
            infomail: "",
          },
          {
            no: 3,
            group_date: "07/20",
            group_start_time: "10:00",
            ca: "회로개발",
            types: "직무, PT, 토론",
            applicants: "김지원 외 49",
            view_wait: "김대기 외 5",
            interviewers: "김면접 외 14",
            infomail: "",
          },
          {
            no: 4,
            group_date: "07/20",
            group_start_time: "16:00",
            ca: "SW개발",
            types: "직무, PT, 토론",
            applicants: "김지원 외 49",
            view_wait: "김대기 외 5",
            interviewers: "김면접 외 14",
            infomail: "",
          },
        ],

        [
          {
            no: 5,
            group_date: "11/13",
            group_start_time: "08:00",
            ca: "SW개발",
            types: "직무, PT, 토론",
            applicants: "김지원 외 49",
            view_wait: "김대기 외 5",
            interviewers: "김면접 외 14",
            infomail: "",
          },
          {
            no: 6,
            group_date: "11/13",
            group_start_time: "08:00",
            ca: "총무",
            types: "직무, PT, 토론",
            applicants: "김지원 외 49",
            view_wait: "김대기 외 5",
            interviewers: "김면접 외 14",
            infomail: "",
          },
          {
            no: 7,
            group_date: "11/13",
            group_start_time: "08:00",
            ca: "회로개발",
            types: "직무, PT, 토론",
            applicants: "김지원 외 49",
            view_wait: "김대기 외 5",
            interviewers: "김면접 외 14",
            infomail: "",
          },
          {
            no: 8,
            group_date: "11/14",
            group_start_time: "08:00",
            ca: "SW개발",
            types: "직무, PT, 토론",
            applicants: "김지원 외 49",
            view_wait: "김대기 외 5",
            interviewers: "김면접 외 14",
            infomail: "",
          },
        ],

        [
          {
            no: 9,
            group_date: "07/08",
            group_start_time: "09:00",
            ca: "리서치",
            types: "직무, PT, 토론",
            applicants: "김지원 외 49",
            view_wait: "김대기 외 5",
            interviewers: "김면접 외 14",
            infomail: "",
          },
          {
            no: 10,
            group_date: "07/08",
            group_start_time: "09:00",
            ca: "UI/UX",
            types: "직무, PT, 토론",
            applicants: "김지원 외 49",
            view_wait: "김대기 외 5",
            interviewers: "김면접 외 14",
            infomail: "",
          },
          {
            no: 11,
            group_date: "07/08",
            group_start_time: "13:00",
            ca: "리서치",
            types: "직무, PT, 토론",
            applicants: "김지원 외 49",
            view_wait: "김대기 외 5",
            interviewers: "김면접 외 14",
            infomail: "",
          },
          {
            no: 12,
            group_date: "07/08",
            group_start_time: "13:00",
            ca: "제품디자인",
            types: "직무, PT, 토론",
            applicants: "김지원 외 49",
            view_wait: "김대기 외 5",
            interviewers: "김면접 외 14",
            infomail: "",
          },
        ],
      ],
    };
  },
  created: function () {
    this.reno = this.$store.state.selectedRecruitNo;
    console.log("프로그레스 created의 reno:", this.reno);
  },
};
</script>

<style>
</style>