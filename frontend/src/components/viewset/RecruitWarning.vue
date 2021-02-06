<template>
  <div id="recruitWarning">
    <!--######## 공고 선택 경고 페이지##############-->
    <v-container>
      <v-row>
        <v-col>
          현재 선택된 공고가 없습니다. 
          좌측에서 공고를 선택해주세요.
        </v-col>
        
      </v-row>
    </v-container>
    <router-view />
  </div>
</template>

<script>
  export default {
    name: "RecruitWarning",
    data: function () {
      return {
        selectedItem: 1,
        
      };
    },
    
    created: function () {
      console.log("reno:", this.recruitNo);
    },
    watch: {
     

    },
  };
</script>

<style>
</style>