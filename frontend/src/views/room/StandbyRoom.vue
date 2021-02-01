<template>
  <div id="standbyRoom">
    <!-- 대기실 페이지. 신분에 따라 보여지는 컴포넌트가 다르다.  -->


    <!--상단 바-->
    <div>
      <v-app-bar color="deep-purple accent-4" dense dark>
        <div class="d-flex">
          <v-toolbar-title class="mx-3">현재 면접자 수 : __ 명</v-toolbar-title>
          <v-toolbar-title class="mx-3">현재 시간 : </v-toolbar-title>
        </div>

        <div class="float-right">
          <v-toolbar-title class="mx-3"> {{com_name}} {{re_year}}{{re_flag}} {{re_status}} </v-toolbar-title>
        </div>
      </v-app-bar>
    </div>


    <!-- 바 밑에 내용물들.  -->
    <v-container>




      <!--row1. : 공지사항 배너, 면접실 만들기 버튼-->
      <v-row>






      </v-row>

      <!-- row2 : 관리자, 지원자, FAQ 잡동사니 row-->
      <v-row>
        
       
       
       
       <!-- row2[왼쪽] : 관리자 리스트-->
        <v-col cols="3">
          <ManagerList/>
   
        </v-col>

        <!-- row2[가운데] : 지원자 리스트-->
        <v-col cols="6">
          <VieweeList/>

        </v-col>



        <!-- row2[오른쪽] : 우측에 FAQ 채팅창 잡동사니 -->
        <v-col cols="3">


        
        </v-col>



      </v-row>


    </v-container>





  </div>
</template>

<script>
  import {
    mapState
  } from "vuex";

  import ManagerList from '@/components/room/ManagerList.vue'
  import VieweeList from '@/components/room/VieweeList.vue'

  export default {
    name: "StandbyRoom",
    components: {
      ManagerList,
      VieweeList,

    },
    data: function () {
      return {
        com_name: "버즈글로벌",
        re_year: 2021,
        re_flag: "상반기",
        re_status: "신입",


      };
    },

    methods: {},
    created: function () {
      // if (this.viewerLogin) {
      //   this.$router.push({ name: "ViewerRecruitItem" });
      // }
    },

    computed: {
      ...mapState(["whoLogin"]),
    },
  };
</script>

<style scoped>
  #standbyRoom {
    background-color: white;
    /* width: 100vw; */
    height: 100vh;
  }
</style>