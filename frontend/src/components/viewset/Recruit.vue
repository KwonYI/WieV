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

          <template v-slot:extension>
            <v-tabs v-model="recruitTab" align-with-title>
              <v-tabs-slider></v-tabs-slider>
              <v-tab v-for="(tab, i) in tabs" :key="i">
                {{ tab.name }}
              </v-tab>
            </v-tabs>
          </template>

          <v-chip style="" outlined>
            <router-link :to="{ name: 'CreateSet', params: { recruitNo: recruitNo }}">
              면접스케줄 생성
            </router-link>
          </v-chip>
        </v-app-bar>
        <v-tabs-items v-model="recruitTab" class="bg-transparent">
          <v-tab-item v-for="(tab, i) in tabs" :key="i">
            <component :is="tab.content" :recruitNo="recruitNo"></component>
          </v-tab-item>
        </v-tabs-items>
        <!-- </v-col> -->
      </v-row>
    </v-container>
    <router-view />
  </div>
</template>

<script>
  import Progress from '@/views/viewset/Progress'
  import Viewees from '@/views/viewset/Viewees'
  import Viewers from '@/views/viewset/Viewers'

  export default {
    name: "Recruit",
    components: {
      Progress,
      Viewees,
      Viewers
    },
    data: function () {
      return {
        selectedItem: 1,
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
      recruitNo: [Object, String, Number],
      selectRecruitTrigger: [Object, String, Number],
    },
    created: function () {
      console.log("reno:", this.recruitNo)
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
  }
</script>

<style>
</style>