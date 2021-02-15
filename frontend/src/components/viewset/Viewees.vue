<template>
  <div id="viewees">
    <v-toolbar dark color="#b0c4de" class="font-weight-bold black--text d-flex justify-content-end">
      <v-toolbar-title></v-toolbar-title>

      <span class="m-2 text-subtitle-1">File:</span>
      <!-- <input v-model="title" /> -->
      <input type="file" id="files" ref="files" v-on:change="handleFileUpload()" multiple />

      <v-btn class="m-2" v-on:click="submitFile()">
        <v-icon left>mdi-table-arrow-up</v-icon>엑셀 업로드
      </v-btn>
      <v-btn class="m-2" @click="exportExcel">
        <v-icon left>mdi-download-box</v-icon>엑셀 양식 다운로드
      </v-btn>
      <v-btn class="m-2" @click="updateVieweeDB" color="green">
        <v-icon left>mdi-refresh</v-icon>목록 업데이트
      </v-btn>
    </v-toolbar>
    <v-simple-table fixed-header height="500px" class="mt-5">
      <thead>
        <tr>
          <th class="text-center">No</th>
          <th class="text-center">부서</th>
          <th class="text-center">직군</th>
          <th class="text-center">이름</th>
          <th class="text-center">연락처</th>
          <th class="text-center">생년월일</th>
          <th class="text-center">이메일</th>
          <th class="text-center">자기소개서</th>
          <th class="text-center">상태</th>
        </tr>
      </thead>

      <v-dialog v-model="dialog" width="600px">
        <template v-slot:activator="{ on, attrs }">
          <tbody>
            <tr v-for="(viewee, index) in getVieweeListCurrentRecruit" :key="viewee.index" class="text-center">
              <td>{{ index + 1 }}</td>
              <td>{{ viewee.applyPartName }}</td>
              <td>{{ viewee.applyCareerName }}</td>
              <td>{{ viewee.applyName }}</td>
              <td>{{ viewee.applyPhone }}</td>
              <td>{{ viewee.applyBirth }}</td>
              <td>{{ viewee.applyEmail }}</td>
              <td>
                <v-btn @click="showResume(viewee)" color="blue-grey lighten-4" v-bind="attrs" v-on="on">
                  자기소개서
                </v-btn>
              </td>
              <td v-if="viewee.applyAssigned === 1" class="blue--text">배정완료</td>
              <td v-else class="red--text">미배정</td>
              <!-- <td>
            <v-btn>이력서</v-btn>
          </td> -->
            </tr>
          </tbody>
        </template>
        <v-card>
          <v-card-title>
            <span class="headline">[ {{selectedViewee.applyName}} ] 지원자의 자기소개서</span>
          </v-card-title>
          <v-card-title>
            <span class="headline">문항1</span>
          </v-card-title>
          <v-card-text>
            {{selectedViewee.applyResume1}}
          </v-card-text>
          <v-card-title>
            <span class="headline">문항2</span>
          </v-card-title>
          <v-card-text>
            {{selectedViewee.applyResume2}}
          </v-card-text>
          <v-card-title>
            <span class="headline">문항3</span>
          </v-card-title>
          <v-card-text>
            {{selectedViewee.applyResume3}}
          </v-card-text>
          <v-card-title>
            <span class="headline">문항4</span>
          </v-card-title>
          <v-card-text>
            {{selectedViewee.applyResume4}}
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="green darken-1" text @click="dialog = false">
              닫기
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-simple-table>
    <div class="d-flex justify-end">
      <v-btn class="m-2" @click="deleteAllApplicant">지원자 전체삭제</v-btn>
    </div>
  </div>

</template>

<script>
  import axios from "axios";
  import XLSX from "xlsx";
  // const SERVER_URL = "https://localhost:8080";
  // const SERVER_URL = "https://i4a405.p.ssafy.io:8080"
  const SERVER_URL = process.env.VUE_APP_SERVER_URL

  import {
    mapState,
    mapGetters
  } from "vuex";

  export default {
    name: "Viewees",
    props: {
      recruitItem: [Object, String, Number]
    },
    data: function () {
      return {
        arr: [],
        selectedViewee: '',
        tableData: [{
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
        }, ],
        title: "",
        files: [],
        loading: false,
        search: null,
        select: null,
        items: [],
        reno: "",
        dialog: false,

        // recruitNo: "",
      };
    },

    created: function () {
      // this.arr = this.getVieweeListCurrentRecruit;
      this.reno = this.$store.state.selectedRecruitNo;

    },

    methods: {
      exportExcel() {
        const wb = XLSX.utils.book_new(); // 엑셀 파일 생성 (workbook)
        const ws = XLSX.utils.json_to_sheet(this.tableData); // 시트 생성 (worksheet) 및 데이터 삽입

        XLSX.utils.book_append_sheet(wb, ws, "sheet1"); // 엑셀 파일에 시트 추가

        XLSX.writeFile(wb, "지원자등록_양식.xlsx"); // 엑셀 다운로드
      },
      filterdVieweeList: function () {
        return this.comVieweeList.filter((re) => re.recruitReSeq === this.reno);
      },

      submitFile() {
        let formData = new FormData();
        formData.append("title", this.title);
        formData.append("files", this.files[0]);
        console.log(this.reno);
        axios
          .post(`${SERVER_URL}/applicant/register/` + this.reno, formData, {
            headers: {
              "Content-Type": "multipart/form-data",
            },
          })
          .then(function () {
            console.log("지원자 엑셀 업로드 성공");
            alert("지원자 등록을 성공했습니다. 목록을 업데이트 해주세요");
          })
          .catch(function (err) {
            console.log(err);
            alert("지원자 등록을 실패했습니다. 양식을 맞춰서 업로드 해주세요.");
            console.log("지원자 엑셀 업로드 실패");
          });
      },

      handleFileUpload() {
        this.files = this.$refs.files.files;
        console.log(this.files);
      },

      //axios.post(보낼url, reno)

      updateVieweeDB: function () {
        this.$store.dispatch("GET_VIEWEE_LIST", this.user.userComSeq);

      },

      showResume: function (viewee) {
        this.selectedViewee = viewee
      },

      // watch: {
      //   getVieweeListCurrentRecruit: function () {
      //     this.getVieweeListCurrentRecruit = this.$store.getters.getVieweeListCurrentRecruit

      //   }

      // },

      // updateVieweeDB: function() {
      //   this.$store.dispatch("UPDATE_VIEWEE_LIST", this.reno);
      //   this.filterdVieweeList();
      //   this.arr = this.recruitVieweeList;
      // },

      deleteAllApplicant: function () {
        if (confirm('지원자 전체 삭제하시겠습니까?(면접에 배정된 지원자는 삭제할 수 없습니다.)') == true) {
          console.log("삭제됌")
          axios.delete(`${SERVER_URL}/applicant/delete/` + this.recruitItem.reSeq)
            .then(() => {
              alert("지원자 전체 삭제 완료")
              this.$store.dispatch("GET_VIEWEE_LIST", this.getUserComSeq)
            })
            .catch((err) => {
              console.log(err)
              alert("지원자 전체 삭제 실패")
            })
        }
      },

      // watch: {
      //   getVieweeListCurrentRecruit: function () {
      //     this.getVieweeListCurrentRecruit = this.$store.getters.getVieweeListCurrentRecruit

      //   }

      // },

      // updateVieweeDB: function() {
      //   this.$store.dispatch("UPDATE_VIEWEE_LIST", this.reno);
      //   this.filterdVieweeList();
      //   this.arr = this.recruitVieweeList;
      // },
    },
    computed: {
      ...mapState(["recruitVieweeList", "comVieweeList", "user"]),
      ...mapGetters(["getUserComSeq", "getVieweeListCurrentRecruit"]),
    },
  };
</script>

<style></style>