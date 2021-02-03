<template>
  <div id="viewees">
    <v-toolbar dark color="teal">
      <v-toolbar-title>지원자 찾기</v-toolbar-title>
      <v-autocomplete v-model="select" :loading="loading" :items="items" :search-input.sync="search" cache-items
        class="mx-4" flat hide-no-data hide-details label="지원자 이름 검색" solo-inverted></v-autocomplete>
      <v-btn class="m-3"> 검색 </v-btn>
      <v-file-input show-size counter multiple label="File input"></v-file-input>
      <v-btn class="m-3" @click="createVieweeDB"> DB업데이트 </v-btn>
    </v-toolbar>
    <v-simple-table fixed-header class="mt-5">
      <thead>
        <tr>
          <th class="text-center">No</th>
          <th class="text-center">직군부류</th>
          <th class="text-center">이름</th>
          <th class="text-center">연락처</th>
          <th class="text-center">생년월일</th>
          <th class="text-center">이메일</th>
          <th class="text-center">자기소개서</th>
          <th class="text-center">이력서</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(viewee, index) in filterdVieweeList()" :key="viewee.index" class="text-center">
          <td>{{ index+1 }}</td>
          <td>{{ viewee.careerCaSeq }}</td>
          <td>{{ viewee.applyName }}</td>
          <td>{{ viewee.applyPhone }}</td>
          <td>{{ viewee.applyBirth }}</td>
          <td>{{ viewee.applyEmail }}</td>
          <td>
            <v-btn>자기소개서</v-btn>
          </td>
          <td>
            <v-btn>이력서</v-btn>
          </td>
        </tr>


      </tbody>
    </v-simple-table>
  </div>
</template>

<script>
  //  import axios from 'axios';
  // const SERVER_URL = "https://localhost:8080/";

  import {
    mapState
  } from "vuex";

  export default {
    name: "Viewees",
    data: function () {
      return {
        loading: false,
        search: null,
        select: null,
        items: [],
        reno: "",

      };
    },
    created: function () {
      // this.reno = this.$route.params.recruitNo;
      this.reno = this.$store.state.selectedRecruitNo;
      console.log("reno:", this.reno);
      console.log("지원자들어있나 created때?", this.recruitVieweeList)
      this.filterdVieweeList();
      // console.log(this.filterdVieweeList)
      // this.createVieweeDB();





    },

    methods: {
      createVieweeDB: function () {

        this.$store.dispatch("GETVIEWEELIST", this.reno)
        this.filterdVieweeList();

      },
      filterdVieweeList: function () { //re.recruitReSeq === this.reno 로 수정해야함
        return this.recruitVieweeList.filter(re => re.recruitReSeq === this.reno)

      },



    },
    computed: {
      ...mapState(["recruitVieweeList"]),
      //지원자 리스트에서 공고에 해당하는 것만 골라서 필터링
      // filterdVieweeList: function () { //re.recruitReSeq === this.reno 로 수정해야함
      //   return this.recruitVieweeList.filter(re => re.recruitReSeq === this.reno)

      // }



    },



  };
</script>

<style>
</style>