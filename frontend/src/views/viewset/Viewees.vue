<template>
  <div id="viewees">
    <v-toolbar dark color="teal">
      <v-toolbar-title>지원자 찾기</v-toolbar-title>
      <v-autocomplete v-model="select" :loading="loading" :items="items" :search-input.sync="search" cache-items
        class="mx-4" flat hide-no-data hide-details label="지원자 이름 검색" solo-inverted></v-autocomplete>
      <v-btn class="m-3"> 검색 </v-btn>
      <div class="large-12 medium-12 small-12 cell">
                <label>File
                    <input v-model="title">
                    <input type="file" id="files" ref="files" v-on:change="handleFileUpload()" multiple />
                </label>
                <button v-on:click="submitFile()">Submit</button>
          </div>      
      <!-- <v-file-input v-model="files" show-size label="File input"></v-file-input>
      <v-btn @click="upload" color="primary">Upload</v-btn>
      <p>File Name : {{ files.name }}</p> -->
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
  import axios from 'axios';
   const SERVER_URL = "https://localhost:8080/";

  import {
    mapState
  } from "vuex";


  export default {
    name: "Viewees",
    data:
     function () {
      return {
        title:"",
        files:[],
        loading: false,
        search: null,
        select: null,
        items: [],
        reno: "",
        applicants: [
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
        ]

      };
    },
    // props: {
    //   recruitNo: [Object, String, Number],
    // },
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
                     submitFile() {
                 
                        let formData = new FormData();
                        formData.append('title', this.title);
                        formData.append('files', this.files[0]);
                        console.log(this.reno)
                        axios.post(`${SERVER_URL}applicant/register/`+this.reno,
                                formData, {
                                    headers: {
                                        'Content-Type': 'multipart/form-data'
                                    }
                                }
                            ).then(function() {
                                console.log('SUCCESS!!');
                            })
                            .catch(function() {
                                console.log('FAILURE!!');
                            });

                },

                handleFileUpload() {
                    this.files = this.$refs.files.files;
                    console.log(this.files);
                },

        //여기서 요청을 보내면, excel파일대로 DB를 저장한 뒤 여기에 뿌려줘야 합니다.
        //공고 데이터를 보내면, 받는 데이터는 해당 공고 지원자 전체입니다.

        //axios.post(보낼url, reno)
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