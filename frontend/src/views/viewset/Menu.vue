<template>
  <div class="Menu">
    <v-container>
      <v-row>
        <div>
          <v-card class="mx-auto mt-10 mb-10 rounded-b-lg" max-width="1000">
            <v-card-title> </v-card-title>
                  <v-card-subtitle></v-card-subtitle>
          </v-card>
        </div>
        <v-col cols="3">
          <v-list shaped dark id="sidebarCol">
            <v-subheader>
              공고 리스트
            </v-subheader>
            <v-list-item-group v-model="selectedItem" color="primary">
              <v-list-item v-for="(item, i) in getRecruitListLately" :key="i">
                <v-list-item-content>
                  <v-list-item-title v-text="`${item.reYear}${item.reFlag} ${item.reStatus}`"
                    @click="selectRecruit(item.reSeq)"></v-list-item-title>
                </v-list-item-content>
              </v-list-item>


            </v-list-item-group>
          </v-list>
        </v-col>

        <v-col cols="9">

          <Recruit v-if="recruitno" :recruitNo="recruitno" />
          <RecruitWarning v-else/>
        </v-col>

        <!-- <router-view /> -->
      </v-row>
    </v-container>
  </div>
</template>

<script>
  import Recruit from "../../components/viewset/Recruit.vue";
  import RecruitWarning from "../../components/viewset/RecruitWarning.vue";

  import {
    mapState,
    mapGetters
  } from "vuex";

  export default {
    name: "Menu",
    components: {
      Recruit,
      RecruitWarning,
    },
    data: function () {
      return {
        recruitno: this.$route.params.recruitNo,
        selectedItem: -1,

      }
    },

 
    methods: {
      selectRecruit: function (selectedreno) {
        console.log("selectRecruit클릭!", selectedreno)
        this.$store.state.selectedRecruitNo = selectedreno
        this.recruitno = selectedreno
      },
      goToProfile: function () {
        this.recruitno = -1
      },

    },
    created: function () {
      console.log("Menu의 createD의 reno", this.recruitno)
      console.log("Menu의 회사 seq", this.getUserComSeq)
      this.$store.dispatch("GET_VIEWER_LIST", this.getUserComSeq)
      this.$store.dispatch("GET_VIEWEE_LIST", this.getUserComSeq)
    },

    computed: {
      ...mapState(["selectedRecruitNo", "recruitList", "user"]),
      ...mapGetters(["getUserComSeq", "getRecruitListLately", "getUserComSeq"])
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