<template>
  <div id="progress">
    현재 채용공고 번호 : {{ recruitItem.reSeq }}
    <v-simple-table fixed-header class="mt-5" >
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
        <tr v-for="item in this.recruitProgressList" :key="item.progress_no" class="text-center">
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
  import { mapState, mapGetters } from "vuex"


  export default {
    name: "Progress",
    props: {
      recruitItem: [Object, String, Number],
    },
    data: function () {
      return {
      }
    },
    created: function () {
     
      
    },
    computed: {
      ...mapState(["recruitList", "recruitProgressList", "selectedRecruitNo"]),
      ...mapGetters([]),
      // filterdProgressList: function () {
      //   return this.recruitProgressList.filter(re => re.reSeq === this.recruitNo)
      // }
    },
  }
</script>

<style>
</style>