<template>
  <div id="viewers" v-if="givnereno">
    <v-simple-table fixed-header  height="500px" class="mt-5">
      <thead>
        <tr>
          <th class="text-center">No</th>
          <th class="text-center">이름</th>
          <th class="text-center">이메일</th>
          <th class="text-center">연락처</th>
          <th class="text-center">비밀번호</th>
          <th class="text-center">관리</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="viewer in comViewerList" :key="viewer.no" class="text-center">
          <td>{{ viewer.viewSeq }}</td>
          <td>{{ viewer.viewName }}</td>
          <td>{{ viewer.viewEmail }}</td>
          <td>{{ viewer.viewPhone }}</td>
          <td>{{ viewer.viewPassword }}</td>
          <td>
            <v-btn>삭제</v-btn>
          </td>
        </tr>
      </tbody>
    </v-simple-table>
  </div>
</template>

<script>
  import { mapGetters, mapState } from "vuex";


  export default {
    name: "Viewers",
    data: function () {
      return {
        reno: "",
        givnereno:"",

      };
    },
    created: function () {
      this.givnereno = this.$route.params.recruitNo;
      this.reno = this.$store.state.selectedRecruitNo;
      console.log("reno:", this.reno);
    },

    computed: {
      ...mapState(["selectedRecruitNo", "recruitList", "comViewerList"]),
      ...mapGetters(["getComViewerList"])
      
    },
  };
</script>

<style>
</style>