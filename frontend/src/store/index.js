import Vue from 'vue'
import Vuex from 'vuex'
import axios from "axios"

Vue.use(Vuex)
const SERVER_URL = "https://localhost:8080"
// import axios from 'axios'

export default new Vuex.Store({
  state: {
    // 로그인 정보
    // isLogin: false,
    // isManager: false,
    // userEmail: '',

    accessToken: null,
    // userViewWait => 인사담당자(-1), 대기관(0), 면접관(1) 구분자
    user: {
      userEmail: "",
      userName: "",
      userPhone: "",
      userViewWait: 0,
      userComSeq: 0,
      userComName: "",
      userComLogo: "",
      userComAddress: "",
      userComHomepage: ""
    },

    //관리자가 선택한 현재 공고 : 
    selectRecruitTrigger : false,
    selectedRecruitNo: -1,


    //그냥 인담자, 면접관, 지원자 로그인 상황 한 변수로 통일하는 게 낫다.
    whoLogin: 'viewer', //Manager, viewer, viewee

    comData: {
      comSeq:'1',
      comName:'버즈글로벌',
      comLogo:'asdf',
      comAddress:'서울특별시 강남구 테헤란로',
      comHomepage:'https://www.naver.com',
    },
    recruitList: [{
        reSeq: '1003',
        reYear: 2021,
        reFlag: "상반기",
        reStatus: "신입",
        reStartDate: "2021-08-07",
        reEndDate: "2021-08-08",
      },
      {
        reSeq: '1002',
        reYear: 2020,
        reFlag: "하반기",
        reStatus: "신입",
        reStartDate: "2020-11-07",
        reEndDate: "2020-11-10",
      },
      {
        reSeq: '1001',
        reYear: 2020,
        reFlag: "상반기",
        reStatus: "신입",
        reStartDate: "2020-08-22",
        reEndDate: "2020-08-24",
      }
    ]
  },


  getters: {
    getUser(state) {
      return state.user;
    },
    getAccessToken(state) {
      return state.accessToken;
    },
    getUserViewWait(state) {  
      return state.user.userViewWait;
    }
  },
  mutations: {

    // 공고 리스트 가져오는 요청
    // 필요한 데이터 : re_seq, re_year, re_flag, re_status, re_startdate, re_enddate
    GET_RECRUITS: function (state, res) {
      console.log(state, res)

      // this.$set(state, comData, res.'회사 정보')
      // this.$set(state, recruitList, res.'공고 리스트')

      // axios.post('공고요청하는URL', state.com_seq)
      //   .then(res => {
      //     const recruits = res.data
      //     for (const recruit of recruits) {
      //       state.recruit_list.push(recruit)
      //     }
      //   })
      //   .catch(err => {
      //     console.log(err)
      //   })
    },



    //############# Schedule.vue 에서 새로 추가한 공고 store에 넣는 작업 ###################################
    ADD_RECRUIT: function (state, recruitData) {
      console.log("state의 ADD_RECRUIT실행:", state, recruitData)
      state.recruit_list.push(recruitData)
      //필요하다면, recruitData 형식을 가공해서 넣어줘야 함 

    }





    LOGIN(state, res) {
      state.accessToken = res["auth-token"];
      state.user.userEmail = res["user-Email"];
      state.user.userName = res["user-Name"];
      state.user.userPhone = res["user-Phone"];
      state.user.userViewWait = res["user-View-Wait"];
      state.user.userComSeq = res["user-Company-seq"];
      state.user.userComName = res["user-Company-Name"];
      state.user.userComLogo = res["user-Company-Logo"];
      state.user.userComAddress = res["user-Company-Address"];
      state.user.userComHomepage = res["user-Company-Homepage"];
    },
    LOGOUT(state) {
      state.accessToken = null;
      state.user.userEmail = "";
      state.user.userName = "";
      state.user.userPhone = "";
      state.user.userViewWait = 0;
      state.user.userComSeq = 0;
      state.user.userComName = "";
      state.user.userComLogo = "";
      state.user.userComAddress = "";
      state.user.userComHomepage = "";
    }
  },
  actions: {
    getRecruits: function({commit}, res){
      commit('GET_RECRUITS', res)
    },

    addRecruit: function({commit}, recruitData){
      commit('ADD_RECRUIT', recruitData )
    }
    LOGIN(context, user) {
      axios.post(`${SERVER_URL}/hr/login`, user)
        .then(response => {
          context.commit("LOGIN", response.data);
          console.log(response.data);
          axios.defaults.headers.common[
            "auth-token"
          ] = `${response.data["auth-token"]}`;
        });
    },
    LOGOUT(context){
      context.commit("LOGOUT");
      axios.defaults.headers.common["auth-token"] = undefined;
    },
  }
})
