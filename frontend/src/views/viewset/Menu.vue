<template>
  <div class="Menu">
    <v-container>
      <v-row>
        <div>
          <v-card class="mx-auto mt-10 rounded-b-lg d-flex justify-space-between align-center">
              <div class="d-flex">
                <v-icon large color="blue-grey darken-3" class="ml-5">mdi-calendar-multiple</v-icon>
            <v-card-title>
              [ {{recruitItem.reYear}}년도 {{recruitItem.reFlag}} {{recruitItem.reStatus}} ] 공고 현황
            </v-card-title>

              </div>
            <v-btn class="mr-5" @click="deleteRecruit(recruitItem.reSeq)">공고 삭제</v-btn>
            <!-- <v-card-subtitle></v-card-subtitle> -->
          </v-card>
        </div>
        <v-col cols="2">
          <v-list shaped dark id="sidebarCol" >
            <v-subheader>
              채용공고 리스트
            </v-subheader>
            <v-list-item-group v-model="recruitNo" color="#FFF1C3">
              <v-list-item
                v-for="(item, i) in getRecruitListLately"
                :key="i"
              :disabled="item.reSeq == $store.state.selectedRecruitNo ? true:false"
                
                @click="selectRecruit(item)"
              >
                <v-list-item-content>
                  <v-list-item-title>
                    {{item.reYear}} {{item.reFlag}} {{item.reStatus}} {{item.reSeq}}
                  </v-list-item-title>
                </v-list-item-content>
              </v-list-item>
            </v-list-item-group>
          </v-list>
        </v-col>

        <v-col cols="10">
          <Recruit v-if="$store.state.selectedRecruitNo > -1" :recruitItem="recruitItem" />
          <RecruitWarning v-else/>
        </v-col>

        <!-- <router-view /> -->
      </v-row>
    </v-container>
  </div>
</template>

<script>
  import Recruit from "../../components/viewset/Recruit.vue"
  import RecruitWarning from "../../components/viewset/RecruitWarning.vue"

  import { mapState, mapGetters } from "vuex"

  export default {
    name: "Menu",
    components: {
      Recruit,
      RecruitWarning,
    },
    
    data: function () {
      return {
        // recruitIndex: this.$route.params.recruitIndex,
        // recruitItem: this.$route.params.recruitItem,
        recruitNo: this.$route.params.recruitIndex,
        // recruitNo: this.$stroe.state.selectedRecruitNo,
        recruitItem: this.$store.state.storeRecruitItem,

      }
    },

    methods: {
      // 선택한 공고 정보
      selectRecruit: function (selectedItem) {
        console.log("selectRecruit클릭!", selectedItem)
        this.$store.state.selectedRecruitNo = selectedItem.reSeq
        this.$store.state.storeRecruitItem = selectedItem
        this.recruitNo = this.$store.state.selectedRecruitNo
        this.recruitItem = this.$store.state.storeRecruitItem
        console.log("menu 왼쪽 리스트에서 선택한 아이템:", this.$store.state.storeRecruitItem)
        console.log("menu 왼쪽 리스트에서 선택한 번호:", this.recruitNo)
        
        // this.$router.push({
        //   name: 'Progress',
        //   params: {
        //     recruitNo: this.recruitNo,
        //     recruitInfo: reNo
        //   }
        // })
        
      },
      goToProfile: function () {
        this.recruitNo = ''
      },

      deleteRecruit: function (reSeq) {
        console.log("시작")
        console.log(this.$store.state.recruitList)
        if(confirm('해당 공고를 삭제하시겠습니까?') == true){
            this.$store
              .dispatch("DELETE_RECRUIT", reSeq)
              .then(() => {
                console.log("deleteRecruit")
                this.$store.state.selectedRecruitNo = -1;
                // this.$store.state.selectedRecruitNo = this.$store.state.recruitList[this.$store.state.recruitList.length - 1].reSeq;
                this.$router.push({ name: "Menu" })
                })
        }
        console.log("끝")
        console.log(this.$store.state.recruitList)
      },
    },

    created: function () {
      this.$store.dispatch("GET_VIEWER_LIST", this.getUserComSeq)
      this.$store.dispatch("GET_VIEWEE_LIST", this.getUserComSeq)
      this.$store.dispatch("GET_PROGRESS_LIST", this.getUserComSeq)
    },

    computed: {
      ...mapState(["user", "selectedRecruitNo", "recruitVieweeList"]),
      ...mapGetters(["getUserComSeq", "getRecruitListLately", "getVieweeListCurrentRecruit"])
    },

  }
</script>

<style>
  #sidebarCol {
    background: #304b61;

    left: 0;
    /* position: fixed; */
    /* height: 100%; */
    height: 100vh;
    width: 12vw;
  }
</style>