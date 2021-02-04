<template>
  <div id="ViewerRecruitItem">
    <!--면접관 메인페이지의 공고리스트-->
    <v-card class="mx-auto mt-10 mb-10 rounded-b-lg" max-width="1000">
      <v-container>
        <v-row>
          <v-col cols="3" class="d-flex align-center">
            <div class="h2 mx-5">{{ interview.comName }}</div>
          </v-col>

          <v-col cols="9">
            <v-card-title>
              {{ interview.recruitYear }}
              {{ interview.recruitFlag }}
              {{ interview.recruitStatus }}
            </v-card-title>
            <v-card-subtitle>
              {{ interview.recruitStartDate }} ~
              {{ interview.recruitEndDate }}
            </v-card-subtitle>
          </v-col>
        </v-row>
        <v-row>
          <!-- <v-divider></v-divider>  -->
          <v-expand-transition>
            <!-- <div v-show="show"> -->
            <div class="blue-grey darken-1 pb-5 rounded-b-lg">
              <v-card class="mx-auto m-3" max-width="1000">
                <v-card-text class="d-flex justify-space-around align-center">
                  <!-- <div class="text--primary">{{ this.index }}</div> -->

                  <div class="text--primary">
                    {{ interview.groupDate }}
                  </div>
                  <!-- 시간이 조금 이상하게 생겼다 -->
                  <div class="text--primary">
                    {{ interview.groupStartTime }} : 00
                  </div>
                  <div class="text--primary">
                    {{ interview.partName }} - {{ interview.careerName }} 직군
                  </div>
                  <div class="text--primary">
                    {{ interview.interviewType }} 면접
                  </div>
                  <v-btn
                    color="blue lighten-3 yellow--text"
                    dark
                    @click="goSession"
                  >
                    대기실 입장
                  </v-btn>
                  <!--
                  <div>
                    <v-dialog v-model="dialog" persistent max-width="600px">
                      <template v-slot:activator="{ on, attrs }">
                        <v-btn
                          color="blue lighten-3 yellow--text"
                          dark
                          v-bind="attrs"
                          v-on="on"
                        >
                          대기실 입장
                        </v-btn>
                      </template>
                      <v-card>
                        <v-card-title>
                          <span class="headline">User Profile</span>
                        </v-card-title>
                        <v-card-text>
                          <v-container>
                            <v-row>
                              <v-col cols="12" sm="6" md="4">
                                <v-text-field
                                  label="Legal first name*"
                                  required
                                ></v-text-field>
                              </v-col>
                              <v-col cols="12" sm="6" md="4">
                                <v-text-field
                                  label="Legal middle name"
                                  hint="example of helper text only on focus"
                                >
                                </v-text-field>
                              </v-col>
                              <v-col cols="12" sm="6" md="4">
                                <v-text-field
                                  label="Legal last name*"
                                  hint="example of persistent helper text"
                                  persistent-hint
                                  required
                                ></v-text-field>
                              </v-col>
                              <v-col cols="12">
                                <v-text-field
                                  label="Email*"
                                  required
                                ></v-text-field>
                              </v-col>
                              <v-col cols="12">
                                <v-text-field
                                  label="Password*"
                                  type="password"
                                  required
                                ></v-text-field>
                              </v-col>
                              <v-col cols="12" sm="6">
                                <v-select
                                  :items="['0-17', '18-29', '30-54', '54+']"
                                  label="Age*"
                                  required
                                ></v-select>
                              </v-col>
                              <v-col cols="12" sm="6">
                                <v-autocomplete
                                  :items="[
                                    'Skiing',
                                    'Ice hockey',
                                    'Soccer',
                                    'Basketball',
                                    'Hockey',
                                    'Reading',
                                    'Writing',
                                    'Coding',
                                    'Basejump',
                                  ]"
                                  label="Interests"
                                  multiple
                                ></v-autocomplete>
                              </v-col>
                            </v-row>
                          </v-container>
                          <small>*indicates required field</small>
                        </v-card-text>
                        <v-card-actions>
                          <v-spacer></v-spacer>
                          <v-btn
                            color="blue darken-1"
                            text
                            @click="dialog = false"
                          >
                            Close
                          </v-btn>
                          <v-btn
                            color="blue darken-1"
                            text
                            @click="dialog = false"
                          >
                            Save
                          </v-btn>
                        </v-card-actions>
                      </v-card>
                    </v-dialog>
                  </div>
                    -->
                </v-card-text>
              </v-card>
            </div>
          </v-expand-transition>
        </v-row>
      </v-container>
    </v-card>
  </div>
</template>

<script>
import axios from "axios";

const SERVER_URL = process.env.VUE_APP_TEST_SERVER_URL;

export default {
  name: "ViewerRecruitItem",
  props: {
    interview: {
      type: Object,
    },
    user: {
      type: Object,
    },
  },
  data: function() {
    return {
      // re_data: [1, 2, 3],
      // show: false,
      // com_name: "버즈글로벌",
      // re_year: 2021,
      // re_flag: "상반기",
      // re_status: "신입",
      // re_startdate: "2021-08-07",
      // re_enddate: "2021-08-08",
      // group_date: "20-08-07",
      // group_start_time: "14:00",
      // ca_name: "마케팅",
      // type_name: "PT면접",
      // dialog: false,
    };
  },
  methods: {
    goSession() {
      axios
        .get(`${SERVER_URL}/session/create`, {
          params: {
            interviewerWait: this.user.userViewWait,
            interviewerName: this.user.userName,
            sessionName: this.interview.sessionName,
          },
        })
        .then((res) => {
          console.log(res);
          this.$router.push({
            name: "WaitRoom",
            params: { interview: this.interview, interviewer: res.data },
          });
        })
        .catch((err) => {
          if (this.user.userViewWait == 0) {
            console.log(err);
          } else {
            alert("방이 아직 개설되지 않았습니다.");
          }
        });
    },
  },
};
</script>

<style></style>
