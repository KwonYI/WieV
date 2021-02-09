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
        </tr>
      </thead>
      <tbody>
        <tr v-for="(group, index) in getProgressListCurrentRecruit" :key="index" class="text-center">
          <td><p class="d-inline p-1">{{ group.groupDate }}</p></td>
          <td><p class="d-inline p-1">{{ group.groupStartTime }}:00</p></td>
          <td><p class="d-inline p-1">{{ group.groupCareerName }}</p></td>
          <td><p v-for="(item, index) in group.interviewTypeList" :key="index" class="d-inline p-1" >{{ item }}</p></td>
          <td><p v-for="(item, index) in group.groupApplicantList" :key="index" class="d-inline p-1">{{ item }}</p></td>
          <td><p v-for="(item, index) in group.waitInterviewerList" :key="index" class="d-inline p-1">{{ item.interviewerName }}</p></td>
          <td><p v-for="(item, index) in group.interviewerList" :key="index" class="d-inline p-1"> {{ item.interviewerName }}</p></td>
          <td>
            <v-checkbox color="indigo"></v-checkbox>
          </td>
        </tr>

      </tbody>
    </v-simple-table>
    <div class="d-flex justify-end">
      <v-btn class="m-2" v-on:click="applicantSendMail()">지원자 안내메일 전송</v-btn>
      <v-btn class="m-2" v-on:click="interviewerSendMail()">면접관 안내메일 전송</v-btn>
      <v-btn class="m-2" v-on:click="deleteGroupAll()">면접일정 전체삭제</v-btn>
    </div>
  </div>
</template>

<script>
  import { mapState, mapGetters } from "vuex"
  const SERVER_URL ="https://localhost:8080"
  import axios from "axios"

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
    methods:{
    applicantSendMail: function() {
      console.log(this.recruitItem.reSeq)
        axios.post(`${SERVER_URL}/applicant/send/`+this.recruitItem.reSeq)
          .then(res => {
            console.log(res)
            alert("지원자 메일 전송 성공")
            this.$router.push({ name: "Home" })
          })
          .catch((err) => {
            console.log(err)
            alert("지원자 메일 전송 실패")
          })
    },
    interviewerSendMail: function() {
      console.log(this.recruitItem.reSeq)
        axios.post(`${SERVER_URL}/interviewer/send/`+this.recruitItem.reSeq)
          .then(res => {
            console.log(res)
            alert("면접관 메일 전송 성공")
            this.$router.push({ name: "Home" })
          })
          .catch((err) => {
            console.log(err)
            alert("면접관 메일 전송 실패")
          })
    },
      deleteGroupAll: function() {
      if(confirm('모든 면접 일정을 삭제하시겠습니까?')==true){
        console.log("삭제됌")
            axios.delete(`${SERVER_URL}/groupAll/delete/` + this.recruitItem.reSeq)
            .then(() => {
               alert("면접 일정 전체 삭제 완료") 
            })
            .catch(() => {
              alert("삭제할 면접 일정이 없습니다.")
            })   
      }
      // .then(result => {
      // console.log(result)
      // })
      // .catch((err) => {
      //      console.log(err)
      //        alert("면접관 메일 전송 실패")
      //      })
        // axios.post(`${SERVER_URL}/interviewer/send/`+this.recruitItem.reSeq)
        //   .then(res => {
        //     console.log(res)
        //     alert("면접관 메일 전송 성공")
        //     this.$router.push({ name: "Home" })
        //   })
        //   .catch((err) => {
        //     console.log(err)
        //     alert("면접관 메일 전송 실패")
        //   })
    },


    },
    computed: {
      ...mapState(["recruitList", "recruitProgressList", "selectedRecruitNo"]),
      ...mapGetters(["getProgressListCurrentRecruit"]),
      // filterdProgressList: function () {
      //   return this.recruitProgressList.filter(re => re.reSeq === this.recruitNo)
      // }
    },
  }
</script>

<style>
</style>