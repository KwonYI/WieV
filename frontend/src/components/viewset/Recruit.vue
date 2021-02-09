<template>
  <div id="recruit">
    <!--######## 공고 별 컴포넌트##############-->
    <v-container>
      <v-row>
        <!-- <v-col cols="10"> -->

        <!--######## 상단 메뉴들 ###############-->
        <!--######## if문 걸어서 recruitNo 관련해서 값 없으면 그냥 프로필 보여주던지 해야함 -->
        <v-app-bar>
          <!-- <v-tabs style="width: initial" align-with-title>
            {{ recruitNo }}
            <v-tab :to="{ name: 'Progress', params: { recruitNo: recruitNo } }">면접 일정</v-tab>
            <v-tab :to="{ name: 'Viewers', params: { recruitNo: recruitNo } }">면접관 관리</v-tab>
            <v-tab :to="{ name: 'Viewees', params: { recruitNo: recruitNo } }">지원자 관리</v-tab>
          </v-tabs> -->
          
          <v-tabs v-model="recruitTab" align-with-title>
            <v-tabs-slider></v-tabs-slider>
            <v-tab v-for="(tab, i) in tabs" :key="i" class=" text-subtitle-1">
              {{ tab.name }}
            </v-tab>
          </v-tabs>
          
          <div>
            <v-chip style="" outlined color="blue-grey darken-3">
              <router-link :to="{ name: 'CreateSet', params: { recruitItem: recruitItem }}">
                면접스케줄 생성 +
              </router-link>
            </v-chip>
          </div>
        </v-app-bar>
        <v-tabs-items v-model="recruitTab" class="bg-transparent">
          <v-tab-item v-for="(tab, i) in tabs" :key="i">
            <component :is="tab.content" :recruitItem="recruitItem"></component>
          </v-tab-item>
        </v-tabs-items>
        <!-- </v-col> -->
      </v-row>
    </v-container>

    <!-- <router-view /> -->
  </div>
</template>

<script>
  import Progress from '@/components/viewset/Progress'
  import Viewees from '@/components/viewset/Viewees'
  import Viewers from '@/components/viewset/Viewers'
import {  mapState } from "vuex"
  export default {
    name: "Recruit",
    components: {
      Progress,
      Viewees,
      Viewers
    },
    data: function () {
      return {
        // tabs: [{
        //     text: "면접 일정",
        //   },
        //   {
        //     text: "면접관 관리",
        //   },
        //   {
        //     text: "지원자 관리",
        //   },
        // ],
        recruitTab: null,
        tabs: [
          { name: '면접 현황', content: 'Progress' },
          { name: '지원자 관리', content: 'Viewees' },
          { name: '면접관 관리', content: 'Viewers' }
        ]
      }
    },
    props: {
      recruitItem: [Object, String, Number],
      
    },
    created: function () {
      console.log("reno:", this.recruitItem.reSeq)
     
    },
    watch: {
      // recruitNo: function () {
      //   if (this.$route.path != "/recruitmenu/progress") {
      //     this.$router.push({
      //       name: "Progress"
      //     });
      //     // if (this.$route.path != "/recruitmenu") {
      //     //   this.$router.go(1);
      //   }
      // },


      recruitNo: function () {
          
          // this.$router.push({
          //   name: "Progress"
          // });
         
            this.$router.go(1);

            // this.$router.go(this.$router.currentRoute)
        
       
      },
    },
    computed: {
      ...mapState(["selectedRecruitNo", "recruitList", "comViewerList", "user"]),
      
    },
  }
</script>

<style>
</style>