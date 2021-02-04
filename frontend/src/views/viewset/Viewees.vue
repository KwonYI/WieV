<template>
  <div id="viewees" v-if="givenreno">
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

      <label
        >File
        <input v-model="title" />
        <input
          type="file"
          id="files"
          ref="files"
          v-on:change="handleFileUpload()"
          multiple
        />
      </label>
      <v-btn class="m-3" v-on:click="submitFile()">엑셀 업로드</v-btn>

      <v-btn class="m-3" @click="exportExcel">엑셀 양식 다운로드</v-btn>
      <!-- <v-file-input v-model="files" show-size label="File input"></v-file-input>
      <v-btn @click="upload" color="primary">Upload</v-btn>
      <p>File Name : {{ files.name }}</p> -->
      <v-btn class="m-3" @click="createVieweeDB"> DB업데이트 </v-btn>
    </v-toolbar>
    <v-simple-table fixed-header height="500px" class="mt-5">
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
        <tr
          v-for="(viewee, index) in this.arr"
          :key="viewee.index"
          class="text-center"
        >
          <td>{{ index + 1 }}</td>
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
import axios from "axios";
import XLSX from "xlsx";
const SERVER_URL = "https://localhost:8080/";
// const SERVER_URL = "https://i4a405.p.ssafy.io:8080"
// const SERVER_URL = process.env.VUE_APP_SERVER_URL;

import { mapState, mapGetters } from "vuex";

export default {
  name: "Viewees",
  data: function() {
    return {
      arr: [],
      tableData: [
        {
          순서: 1,
          이름: "홍길동(예시)",
          이메일: "email@gmail.com",
          부서: "CE/IM",
          직군: "SW개발",
          생년월일: "1999-01-01",
          핸드폰번호: "010-1111-1111",
          대학교: "서울대학교",
          전공: "컴퓨터공학과",
          학점: 4.5,
          자소서항목1: "자소서 내용을 입력해주세요.",
          자소서항목2: "자소서 내용을 입력해주세요.",
          자소서항목3: "자소서 내용을 입력해주세요.",
          자소서항목4: "자소서 내용을 입력해주세요.",
        },
      ],
      title: "",
      files: [],
      loading: false,
      search: null,
      select: null,
      items: [],
      reno: "",
      givenreno: "",
    };
  },

  created: function() {
    this.arr = this.getVieweeListCurrentRecruit;
    this.reno = this.$store.state.selectedRecruitNo;
    this.givenreno = this.$route.params.recruitNo;
    console.log("reno:", this.reno);
    console.log("회사모든지원자들어있나 created때?", this.comVieweeList);
  },

  methods: {
    exportExcel() {
      const wb = XLSX.utils.book_new(); // 엑셀 파일 생성 (workbook)
      const ws = XLSX.utils.json_to_sheet(this.tableData); // 시트 생성 (worksheet) 및 데이터 삽입

      XLSX.utils.book_append_sheet(wb, ws, "sheet1"); // 엑셀 파일에 시트 추가

      XLSX.writeFile(wb, "지원자등록_양식.xlsx"); // 엑셀 다운로드
    },
    filterdVieweeList: function() {
      return this.comVieweeList.filter((re) => re.recruitReSeq === this.reno);
    },

    submitFile() {
      let formData = new FormData();
      formData.append("title", this.title);
      formData.append("files", this.files[0]);
      console.log(this.reno);
      axios
        .post(`${SERVER_URL}applicant/register/` + this.reno, formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        })
        .then(function() {
          console.log("SUCCESS!!");
        })
        .catch(function() {
          console.log("FAILURE!!");
        });
    },

    handleFileUpload() {
      this.files = this.$refs.files.files;
      console.log(this.files);
    },

    //axios.post(보낼url, reno)

    createVieweeDB: function() {
      this.$store.dispatch("UPDATE_VIEWEE_LIST", this.reno);
      this.filterdVieweeList();
      this.arr = this.recruitVieweeList;
    },
  },
  computed: {
    ...mapState(["recruitVieweeList", "comVieweeList"]),
    ...mapGetters(["getUserComSeq", "getVieweeListCurrentRecruit"]),
  },
};
</script>

<style></style>
