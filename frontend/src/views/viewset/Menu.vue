<template>
  <div class="Menu">
    <v-container>
      <v-row>
        <v-col cols="3">
          <v-list shaped dark id="sidebarCol">
            <!-- <v-subheader><v-btn @click="goToProfile">기업정보</v-btn></v-subheader> -->
            <v-subheader>
              <v-btn @click="selectRecruit(-1)">기업정보</v-btn>
            </v-subheader>

            
            <v-list-item-group v-model="selectedItem" color="primary">

              <v-list-item v-for="(item, i) in recruitList" :key="i">
                <v-list-item-content>
                  <v-list-item-title
                    v-text="`${item.reYear}${item.reFlag} ${item.reStatus}`"
                    @click="selectRecruit(item.reSeq)"
                  ></v-list-item-title>
                </v-list-item-content>
              </v-list-item>


            </v-list-item-group>
          </v-list>
        </v-col>

        <v-col cols="9">
          <!--######## 공고 별 컴포넌트##############-->
          <!--초기 화면에서는 기업 정보를 보여준다. 공고를 선택하지 않앗을 경우 -1이다. -->
          <Recruit v-if="recruitno !== -1" :recruitNo="recruitno" />
          <Profile v-else />
        </v-col>

        <!-- <router-view /> -->
      </v-row>
    </v-container>
  </div>
</template>

<script>
import Recruit from "../../components/viewset/Recruit.vue";
import Profile from "../../views/accounts/Profile.vue";

import { mapState } from "vuex";

export default {
  name: "Menu",
  components: {
    Recruit,
    Profile,
  },
  data: function () {
    return {
      recruitno: -1,
      selectedItem: -1,
      tempReList:[],
    };
  },
  methods: {
    selectRecruit: function (selectedreno) {
      console.log("selectRecruit클릭!", selectedreno);
      this.$store.state.selectedRecruitNo = selectedreno;
      // this.selectedRecruitNo = selectedreno;
      this.recruitno = selectedreno;
      // this.selectRecruitTrigger = true;
    },
    goToProfile: function () {
      // this.$store.state.selectedRecruitNo = -1;
      // this.selectedRecruitNo = -1;
      this.recruitno = -1;
    },
  },
  created: function () {
    // this.recruitno = this.$store.state.selectedRecruitNo;
    this.tempReList = this.recruitList
    console.log("createD????",this.tempReList )
  },
  computed: {
    ...mapState(["selectedRecruitNo", "recruitList"]),
  },
  
};
</script>

<style>
#sidebarCol {
  background: #304b61;

  left: 0;
  /* position: fixed; */
  /* height: 100%; */
  height: 100vh;
  width: 200px;
}
</style>