<template>
  <div id="viewers">
    <v-simple-table fixed-header class="mt-5">
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
        <tr v-for="viewer in viewers" :key="viewer.no" class="text-center">
          <td>{{ viewer.no }}</td>
          <td>{{ viewer.name }}</td>
          <td>{{ viewer.email }}</td>
          <td>{{ viewer.phone }}</td>
          <td>{{ viewer.password }}</td>
          <td>
            <v-btn>관리</v-btn>
          </td>
        </tr>
      </tbody>
    </v-simple-table>
  </div>
</template>

<script>
  export default {
    name: "Viewers",
    data: function () {
      return {
        reno: "",

        viewers: [

          {
            no: 1,
            name: "김김김",
            email: "123",
            phone: "010-0000-1234",
            password: "1234",
          },
          {
            no: 2,
            name: "김김김",
            email: "123",
            phone: "010-0000-1234",
            password: "1234",
          },
          {
            no: 3,
            name: "김김김",
            email: "123",
            phone: "010-0000-1234",
            password: "1234",
          },
          {
            no: 4,
            name: "김김김",
            email: "123",
            phone: "010-0000-1234",
            password: "1234",
          },

          {
            no: 5,
            name: "이이이",
            email: "123",
            phone: "010-0000-1234",
            password: "1234",
          },
          {
            no: 6,
            name: "이이이",
            email: "123",
            phone: "010-0000-1234",
            password: "1234",
          },
          {
            no: 7,
            name: "이이이",
            email: "123",
            phone: "010-0000-1234",
            password: "1234",
          },
          {
            no: 8,
            name: "이이이",
            email: "123",
            phone: "010-0000-1234",
            password: "1234",
          },

          {
            no: 9,
            name: "박박박",
            email: "123",
            phone: "010-0000-1234",
            password: "1234",
          },
          {
            no: 10,
            name: "박박박",
            email: "123",
            phone: "010-0000-1234",
            password: "1234",
          },
          {
            no: 11,
            name: "박박박",
            email: "123",
            phone: "010-0000-1234",
            password: "1234",
          },
          {
            no: 12,
            name: "박박박",
            email: "123",
            phone: "010-0000-1234",
            password: "1234",
          },

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