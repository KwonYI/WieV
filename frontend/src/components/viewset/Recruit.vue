<template>
  <div class="recruit">
    <!--######## 공고 별 컴포넌트##############-->
    <v-container>
      <v-row>
        <!-- <v-col cols="10"> -->

        <!--######## 상단 메뉴들 ###############-->
        <!--######## if문 걸어서 recruitNo 관련해서 값 없으면 그냥 프로필 보여주던지 해야함 -->
        <v-app-bar>
          <v-tabs style="width: initial" align-with-title>
            <v-tab :to="{ name: 'Progress', params: { recruitNo: recruitNo } }">
              reseq:{{ recruitNo }}의 면접 일정</v-tab>
            <!-- <v-tab>
              <router-link :to="{ name: 'Viewers' }">면접관 관리</router-link>
            </v-tab> -->
            <v-tab :to="{ name: 'Viewers', params: { recruitNo: recruitNo } }">면접관 관리</v-tab>
            <v-tab :to="{ name: 'Viewees', params: { recruitNo: recruitNo } }">지원자 관리</v-tab>
          </v-tabs>
          <v-chip style="" outlined>
            <router-link :to="{ name: 'CreateSet'}">
              면접스케줄 생성
            </router-link>
          </v-chip>
        </v-app-bar>

        <!-- </v-col> -->
      </v-row>
    </v-container>
    <router-view />
  </div>
</template>

<script>
  export default {
    name: "Recruit",
    data: function () {
      return {
        selectedItem: 1,
        tabs: [{
            text: "면접 일정",
          },
          {
            text: "면접관 관리",
          },
          {
            text: "지원자 관리",
          },
        ],
      };
    },
    props: {
      recruitNo: [Object, String, Number],
      selectRecruitTrigger: [Object, String, Number],
    },
    created: function () {
      console.log("reno:", this.recruitNo);
    },
    watch: {
      recruitNo: function () {
        if (this.$route.path != "/recruitmenu/progress") {
          this.$router.push({
            name: "Progress"
          });
          // if (this.$route.path != "/recruitmenu") {
          //   this.$router.go(1);
        }
      },
    },
  };
</script>

<style>
</style>