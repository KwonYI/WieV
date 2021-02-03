<template>
  <div class="Menu">
    <v-container>
      <v-row>
        <v-col cols="3">
          <v-list shaped dark id="sidebarCol">
            <!-- <v-subheader><v-btn @click="goToProfile">기업정보</v-btn></v-subheader> -->
            <v-subheader>
              <!-- <v-btn @click="selectRecruit(-1)">기업정보</v-btn> -->
              공고 리스트
            </v-subheader>


            <v-list-item-group v-model="selectedItem" color="primary">

              <v-list-item v-for="(item, i) in recruitList" :key="i">
                <v-list-item-content>
                  <v-list-item-title v-text="`${item.reYear}${item.reFlag} ${item.reStatus}`"
                    @click="selectRecruit(item.reSeq)"></v-list-item-title>
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

  import {
    mapState, mapGetters
  } from "vuex";

  export default {
    name: "Menu",
    components: {
      Recruit,
      Profile,
    },
    data: function () {
      return {
        recruitno: this.$route.params.recruitNo,
        selectedItem: -1,
        tempReList: [],
        curRecruit: {
          reYear: '',
          reFlag: '',
          reStatus: ''
        }
      }
    },
    methods: {
      selectRecruit: function (selectedreno) {
        console.log("selectRecruit클릭!", selectedreno)
        this.$store.state.selectedRecruitNo = selectedreno
        this.recruitno = selectedreno;
      },
      goToProfile: function () {
        this.recruitno = -1
      },

      // 지금문제점이 뭐냐면, 이렇게 되면 this.reno 말고 전체 지원자 리스트 가져와야해요
      // filterdVieweeList: function () { //re.recruitReSeq === this.reno 로 수정해야함
      //   return this.recruitVieweeList.filter(re => re.recruitReSeq === this.reno)
    },
    created: function () {
      this.tempReList = this.recruitList
      console.log("Menu의 createD의 reno", this.recruitno)
      console.log("Menu의 회사 seq", this.getUserComSeq)
      // console.log("Menu의 createD의 recruitList",this.tempReList )
      this.$store.dispatch("GET_VIEWER_LIST", this.getUserComSeq)



      // this.$store.dispatch("GET_VIEWEE_LIST", this.reno)
      // this.filterdVieweeList();
    },

    computed: {
      ...mapState(["selectedRecruitNo", "recruitList"]),
      ...mapGetters(["getUserComSeq"])
    },

  };
</script>

<style>
  #sidebarCol {
    background: #304b61;

    left: 0;
    /* position: fixed; */
    /* height: 100%; */
    height: 70vh;
    width: 200px;
  }
</style>