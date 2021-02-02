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
    // comSeq: 0,
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
    selectRecruitTrigger: false,
    selectedRecruitNo: -1,


    //그냥 인담자, 면접관, 지원자 로그인 상황 한 변수로 통일하는 게 낫다.
    whoLogin: 'viewer', //Manager, viewer, viewee

    comData: {
      comSeq:'1',
      comName:'WieV Inc.',
      comLogo:'asdf',
      comAddress:'서울특별시 강남구 테헤란로',
      comHomepage:'https://www.naver.com',
    },

    recruitList: [{
        reSeq: 0,
        reYear: 0,
        reFlag: "",
        reStatus: "",
        reStartDate: "",
        reEndDate: "",
      }],


    recruitProgressList: [{
        reSeq: 1003,
        progress_no: 1,
        group_date: "07/20",
        group_start_time: "10:00",
        ca: "SW개발",
        types: "직무, PT, 토론",
        applicants: "김지원 외 49",
        view_wait: "김대기 외 5",
        interviewers: "김면접 외 14",
        infomail: "",
      },
      {
        reSeq: 1003,
        progress_no: 2,
        group_date: "07/20",
        group_start_time: "10:00",
        ca: "마케팅",
        types: "직무, PT, 토론",
        applicants: "김지원 외 49",
        view_wait: "김대기 외 5",
        interviewers: "김면접 외 14",
        infomail: "",
      },
      {
        reSeq: 1003,
        progress_no: 3,
        group_date: "07/20",
        group_start_time: "10:00",
        ca: "회로개발",
        types: "직무, PT, 토론",
        applicants: "김지원 외 49",
        view_wait: "김대기 외 5",
        interviewers: "김면접 외 14",
        infomail: "",
      },
      {
        reSeq: 1003,
        progress_no: 4,
        group_date: "07/20",
        group_start_time: "16:00",
        ca: "SW개발",
        types: "직무, PT, 토론",
        applicants: "김지원 외 49",
        view_wait: "김대기 외 5",
        interviewers: "김면접 외 14",
        infomail: "",
      },
      {
        reSeq: 1002,
        progress_no: 5,
        group_date: "11/13",
        group_start_time: "08:00",
        ca: "SW개발",
        types: "직무, PT, 토론",
        applicants: "김지원 외 49",
        view_wait: "김대기 외 5",
        interviewers: "김면접 외 14",
        infomail: "",
      },
      {
        reSeq: 1002,
        progress_no: 6,
        group_date: "11/13",
        group_start_time: "08:00",
        ca: "총무",
        types: "직무, PT, 토론",
        applicants: "김지원 외 49",
        view_wait: "김대기 외 5",
        interviewers: "김면접 외 14",
        infomail: "",
      },
      {
        reSeq: 1002,
        progress_no: 7,
        group_date: "11/13",
        group_start_time: "08:00",
        ca: "회로개발",
        types: "직무, PT, 토론",
        applicants: "김지원 외 49",
        view_wait: "김대기 외 5",
        interviewers: "김면접 외 14",
        infomail: "",
      },
      {
        reSeq: 1002,
        progress_no: 8,
        group_date: "11/14",
        group_start_time: "08:00",
        ca: "SW개발",
        types: "직무, PT, 토론",
        applicants: "김지원 외 49",
        view_wait: "김대기 외 5",
        interviewers: "김면접 외 14",
        infomail: "",
      },


      {
        reSeq: 1001,
        progress_no: 9,
        group_date: "07/08",
        group_start_time: "09:00",
        ca: "리서치",
        types: "직무, PT, 토론",
        applicants: "김지원 외 49",
        view_wait: "김대기 외 5",
        interviewers: "김면접 외 14",
        infomail: "",
      },
      {
        reSeq: 1001,
        progress_no: 10,
        group_date: "07/08",
        group_start_time: "09:00",
        ca: "UI/UX",
        types: "직무, PT, 토론",
        applicants: "김지원 외 49",
        view_wait: "김대기 외 5",
        interviewers: "김면접 외 14",
        infomail: "",
      },
      {
        reSeq: 1001,
        progress_no: 11,
        group_date: "07/08",
        group_start_time: "13:00",
        ca: "리서치",
        types: "직무, PT, 토론",
        applicants: "김지원 외 49",
        view_wait: "김대기 외 5",
        interviewers: "김면접 외 14",
        infomail: "",
      },
      {
        reSeq: 1001,
        progress_no: 12,
        group_date: "07/08",
        group_start_time: "13:00",
        ca: "제품디자인",
        types: "직무, PT, 토론",
        applicants: "김지원 외 49",
        view_wait: "김대기 외 5",
        interviewers: "김면접 외 14",
        infomail: "",
      },
    ],




    recruitVieweeList: [
      
    ],

    recruitViewerList: [

    ],



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
    },
    getUserComSeq(state){
      return state.user.userComSeq;
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

    },





    LOGIN(state, res) {
      state.accessToken = res["auth-token"];
      state.user.userEmail = res["user-Email"];
      state.user.userName = res["user-Name"];
      state.user.userPhone = res["user-Phone"];
      state.user.userViewWait = res["user-View-Wait"];
      state.user.userComSeq = res["user-Company-Seq"];
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
    },
    GET_RECRUIT_LIST(state, res){
      state.recruitList = res;
    },
    GETVIEWEELIST(state, res){
      console.log("mutaions의 GETVIEWEELIST", res)
      state.recruitVieweeList = res
      console.log(state.recruitVieweeList)
    }
  },


  actions: {
    getRecruits: function ({
      commit
    }, res) {
      commit('GET_RECRUITS', res)
    },


    addRecruit: function ({
      commit
    }, recruitData) {
      commit('ADD_RECRUIT', recruitData)
    },

    // 로그인, 로그아웃
    LOGIN(context, user) {
      // console.log("login_in");
      axios.post(`${SERVER_URL}/hr/login`, user)
        .then(response => {
          context.commit("LOGIN", response.data);
          // console.log(response.data);
          axios.defaults.headers.common[
            "auth-token"
          ] = `${response.data["auth-token"]}`;
        });
    },
    LOGOUT(context) {
      context.commit("LOGOUT");
      axios.defaults.headers.common["auth-token"] = undefined;
    },

    // 공고 리스트 가져오기
    GET_RECRUIT_LIST(context) {
      console.log(this.state.user.userComSeq);
      axios.get(`${SERVER_URL}/recruit/getList/` + this.state.user.userComSeq )
      .then(response => {
        console.log(response.data);
        context.commit("GET_RECRUIT_LIST", response.data);
      });
      context.commit("GET_RECRUIT_LIST");
    },
    
    //지원자를 저장 
    GETVIEWEELIST(context, recruitNo) {
      axios.get(`${SERVER_URL}/applicant/getList/${recruitNo}`)
        .then((res)=>{
          context.commit("GETVIEWEELIST", res.data);
        })
        .catch((err)=>{
          console.log(err)
        })
    },


  }
})