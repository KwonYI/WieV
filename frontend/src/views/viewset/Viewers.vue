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
        <tr
          v-for="recruit in recruits[reno - 1]"
          :key="recruit.no"
          class="text-center"
        >
          <td>{{ recruit.no }}</td>
          <td>{{ recruit.name }}</td>
          <td>{{ recruit.email }}</td>
          <td>{{ recruit.phone }}</td>
          <td>{{ recruit.password }}</td>
          <td><v-btn>관리</v-btn></td>
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
      comno: "",
      reno:"",
      recruits: [
        [
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
        ],

        [
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
        ],

        [
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
      ],
    };
  },
  created: function () {
    // this.comno = this.$route.params.recruitNo;
    this.comno =this.$store.state.userComSeq;
    this.reno=this.$store.state.selectedRecruitNo;
    console.log("comno:", this.comno);
  },
    methods: {
      createVieweeDB: function (reno) {
        
        //reno 는 현재 공고 reSeq
        console.log("createVieweeDB!", reno);

        //여기서 요청을 보내면, excel파일대로 DB를 저장한 뒤 여기에 뿌려줘야 합니다.
        //공고 데이터를 보내면, 받는 데이터는 해당 공고 지원자 전체입니다.

        //axios.post(보낼url, reno)

      },

    },  
};
</script>

<style>
</style>