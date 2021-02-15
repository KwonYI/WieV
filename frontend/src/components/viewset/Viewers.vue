<template>
  <div id="viewers">
    <v-simple-table fixed-header height="58vh" class="mt-1 mb-1">
      <thead>
        <tr>
          <th class="text-center">No</th>
          <th class="text-center">이름</th>
          <th class="text-center">이메일</th>
          <th class="text-center">연락처</th>
          <th class="text-center">비밀번호</th>
          <th class="text-center">상태</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="viewer in comViewerList" :key="viewer.no" class="text-center">
          <td>{{ viewer.viewSeq }}</td>
          <td>{{ viewer.viewName }}</td>
          <td>{{ viewer.viewEmail }}</td>
          <td>{{ viewer.viewPhone }}</td>
          <td>{{ viewer.viewPassword }}</td>
          <td v-if="viewer.viewAssigned === 1" class="blue--text">배정완료</td>
            <td v-else class="red--text">미배정</td>
        </tr>
      </tbody>
    </v-simple-table>
    <v-toolbar dark color="#b0c4de" class="font-weight-bold black--text d-flex justify-content-end">
    <!-- <v-toolbar dark color="brown darken-1 black--text"> -->
      <v-toolbar-title></v-toolbar-title>
     
        <span class="m-2 text-subtitle-1">File:</span>
        <input
        type="file"
         id="files"
        ref="files" 
        v-on:change="handleFileUpload()" 
        multiple />
      <v-btn class="m-2" v-on:click="submitFile()"><v-icon left>mdi-table-arrow-up</v-icon>엑셀 업로드</v-btn>
      <v-btn class="m-2" @click="exportExcel"><v-icon left>mdi-download-box</v-icon>엑셀 양식 다운로드</v-btn>
      <v-btn class="m-2" @click="updateViewerDB" color="green"><v-icon left>mdi-refresh</v-icon>목록 업데이트 </v-btn>
      <v-btn class="m-2" v-on:click="deleteAllInterviewer" color="red"><v-icon left>mdi-trash-can-outline</v-icon>면접관 전체삭제</v-btn>
    </v-toolbar>
  </div>
</template>

<script>
import { mapGetters, mapState } from "vuex"
import XLSX from 'xlsx'
import axios from 'axios'
const SERVER_URL = process.env.VUE_APP_SERVER_URL
// const SERVER_URL = "https://localhost:8080"
  export default {
    name: "Viewers",
    props: {
      recruitItem: [Object, String, Number],
    },
    data: function () {
      return {
        tableData: [
          {
            순서:'1',
            이름: '홍길동(예시)',
            이메일:'email@gmail.com',
            부서:'CE/IM',
            직군:'SW개발',
            핸드폰번호:'010-1111-2222',
            관리자OR면접관:'면접관',
          }
        ],
        title: "",
        files: [],
      }
    },

    methods: {
      exportExcel() {
        const wb = XLSX.utils.book_new()    // 엑셀 파일 생성 (workbook)
        const ws = XLSX.utils.json_to_sheet(this.tableData)    // 시트 생성 (worksheet) 및 데이터 삽입
        
        XLSX.utils.book_append_sheet(wb, ws, 'sheet1')  // 엑셀 파일에 시트 추가

        XLSX.writeFile(wb, '면접관등록_양식.xlsx') // 엑셀 다운로드
      },

      filterdVieweeList: function () { 
        return this.comVieweeList.filter(re => re.recruitReSeq === this.recruitItem.reSeq)
      },

      submitFile() {
        let formData = new FormData()
        formData.append('title', this.title)
        formData.append('files', this.files[0])
        
        axios.post(`${SERVER_URL}/interviewer/register/` + this.recruitItem.reSeq, formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        })
          .then(() => {
            console.log('면접관 엑셀 업로드 성공')
            alert("면접관 등록 성공을 성공했습니다. 목록을 업데이트 해주세요")
          })
          .catch(err => {
            console.log(err)
            alert("면접관 등록을 실패했습니다. 양식을 맞춰서 업로드 해주세요.")
            console.log('면접관 엑셀 업로드 실패')
          })
      },

      handleFileUpload() {
        this.files = this.$refs.files.files
        console.log(this.files)
      },

      updateViewerDB() {

        this.$store.dispatch("GET_VIEWER_LIST",this.user.userComSeq );


      },
       deleteAllInterviewer: function() {
      if(confirm('면접관을 전체 삭제하시겠습니까?(면접에 배정된 면접관은 삭제할 수 없습니다.)')==true){
        console.log("삭제됌")
            axios.delete(`${SERVER_URL}/interviewer/delete/` + this.user.userComSeq)
            .then(() => {
              alert("면접관 전체 삭제 완료")
              this.$store.dispatch("GET_VIEWER_LIST", this.getUserComSeq)
            })
            .catch((err) => {
              console.log(err)
              alert("면접관 전체 삭제 실패")
            })   
      }
 },
    },
    computed: {
      ...mapState(["selectedRecruitNo", "recruitList", "comViewerList", "user"]),
      ...mapGetters(["getComViewerList", "getUserComSeq"])

    },
  }
</script>

<style>
</style>