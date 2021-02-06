<template>
  <div id="viewers">
    {{ recruitNo }}
    <v-toolbar dark color="teal">
      <v-toolbar-title></v-toolbar-title>
      <!-- <v-toolbar-title>면접관 찾기</v-toolbar-title>
      <v-autocomplete v-model="select" :loading="loading" :items="items" :search-input.sync="search" cache-items
        class="mx-4" flat hide-no-data hide-details label="지원자 이름 검색" solo-inverted></v-autocomplete>
      <v-btn class="m-3"> 검색 </v-btn> -->

        <!-- <label>File
          <input v-model="title"> -->
          <v-text class="m-2">File:</v-text>
          <input type="file" id="files" ref="files" v-on:change="handleFileUpload()" multiple />
        <!-- </label> -->
        <v-btn class="m-3" v-on:click="submitFile()">엑셀 업로드</v-btn>
    
      <v-btn class="m-3" @click="exportExcel">엑셀 양식 다운로드</v-btn>
    
    </v-toolbar>
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
import { mapGetters, mapState } from "vuex"
import XLSX from 'xlsx'
import axios from 'axios'
const SERVER_URL = process.env.VUE_APP_SERVER_URL

  export default {
    name: "Viewers",
    props: {
      recruitNo: [Object, String, Number],
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
        return this.comVieweeList.filter(re => re.recruitReSeq === this.recruitNo)
      },

      submitFile() {
        let formData = new FormData()
        formData.append('title', this.title)
        formData.append('files', this.files[0])
        
        axios.post(`${SERVER_URL}/interviewer/register/` + this.recruitNo, formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        })
          .then(() => {
            console.log('면접관 엑셀 업로드 성공')
            alert("면접관 등록 성공")
          })
          .catch(err => {
            console.log(err)
            alert("면접관 등록 실패")
            console.log('면접관 엑셀 업로드 실패')
          })
      },

      handleFileUpload() {
        this.files = this.$refs.files.files
        console.log(this.files)
      }
    },
    computed: {
      ...mapState(["selectedRecruitNo", "recruitList", "comViewerList"]),
      ...mapGetters(["getComViewerList"])
    },
  }
</script>

<style>
</style>