<template>
  <div class="Menu">
    <v-container>
      <v-row>
        <div>
          <v-card class="mx-auto mt-10 mb-10 rounded-b-lg" max-width="1000">
            <v-card-title></v-card-title>
            <v-card-subtitle></v-card-subtitle>
          </v-card>
        </div>
        <v-col cols="3">
          <v-list shaped dark id="sidebarCol">
            <v-subheader>
              채용공고 리스트
            </v-subheader>
            <v-list-item-group color="primary">
              <v-list-item
                v-for="(item, i) in getRecruitListLately"
                :key="i"
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

        <v-col cols="9">
          <Recruit v-if="recruitno" :recruitItem="recruitItem" />
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
        recruitno: -1,
        recruitItem: Object,
      }
    },

    methods: {
      // 선택한 공고 정보
      selectRecruit: function (selectedItem) {
        console.log("selectRecruit클릭!", selectedItem)
        // this.$store.state.selectedRecruitNo = reNo
        this.recruitno = selectedItem.reSeq
        this.recruitItem = selectedItem
        console.log(this.recruitItem)
        // this.$router.push({
        //   name: 'Progress',
        //   params: {
        //     recruitNo: this.recruitno,
        //     recruitInfo: reNo
        //   }
        // })
      },
      goToProfile: function () {
        this.recruitno = ''
      },
    },

    created: function () {
      this.recruitno = this.$route.params.recruitNo

      console.log("Menu의 createD의 reno", this.recruitno)
      console.log("Menu의 회사 seq", this.getUserComSeq)
      this.$store.dispatch("GET_VIEWER_LIST", this.getUserComSeq)
      this.$store.dispatch("GET_VIEWEE_LIST", this.getUserComSeq)
    },

    computed: {
      ...mapState(["user", "selectedRecruitNo"]),
      ...mapGetters(["getUserComSeq", "getRecruitListLately"])
    },

  }
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