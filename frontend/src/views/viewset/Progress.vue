<template>
  <div id="progress"  v-if="givenreno">
    <v-simple-table fixed-header height="1000px" class="mt-5" >
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
        <tr v-for="item in filterdProgressList" :key="item.progress_no" class="text-center">
          <td>{{ item.group_date }}</td>
          <td>{{ item.group_start_time }}</td>
          <td>{{ item.ca }}</td>
          <td>{{ item.types }}</td>
          <td>{{ item.applicants }}</td>
          <td>{{ item.view_wait }}</td>
          <td>{{ item.interviewers }}</td>
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
  import {
    mapState
  } from "vuex";


  export default {
    name: "Progress",
    data: function () {
      return {
        reno: "",
        givnereno: "",
      };
    },
    created: function () {
      this.givnereno = this.$route.params.recruitNo;
      this.reno = this.$store.state.selectedRecruitNo;
      console.log("프로그레스 created의 reno:", this.reno);
    },
   

    computed: {

      ...mapState(["recruitList", "recruitProgressList"]),

      filterdProgressList: function () {
        return this.recruitProgressList.filter(re => re.reSeq === this.reno)

      }

    },

  };
</script>

<style>
</style>