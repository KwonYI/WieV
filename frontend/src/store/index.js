import Vue from "vue";
import Vuex from "vuex";
import axios from "axios";
import createPersistedState from "vuex-persistedstate";
import _ from "lodash"

Vue.use(Vuex);
//  const SERVER_URL = "https://localhost:8080"
// const SERVER_URL = "https://i4a405.p.ssafy.io:8080"
const SERVER_URL = process.env.VUE_APP_SERVER_URL;

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
      userSeq: 0,
      userEmail: "",
      userName: "",
      userPhone: "",
      userViewWait: 0,
      userComSeq: 0,
      userComName: "",
      userComLogo: "",
      userComAddress: "",
      userComHomepage: "",
    },

    //관리자가 선택한 현재 공고 :
    selectRecruitTrigger: false,
    selectedRecruitNo: -1,

    //그냥 인담자, 면접관, 지원자 로그인 상황 한 변수로 통일하는 게 낫다.
    whoLogin: "viewer", //Manager, viewer, viewee

    companyNameList: [],

    companyList: [
      {
        comSeq: 0,
        comName: "",
        comLogo: "",
        comAddress: "",
        comHomepage: "",
      },
    ],

    recruitList: [
      {
        reSeq: 0,
        reYear: 0,
        reFlag: "",
        reStatus: "",
        reStartDate: "",
        reEndDate: "",
      },
    ],

    recruitProgressList: [
      {
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

    //얘는 공고별 저장인데 comVieweeList 쓸거같다.
    recruitVieweeList: [],

    //회사 전체 지원자 리스트
    comVieweeList: [],

    comViewerList: [
      //careerCaSeq , careerPartPartSeq , companyComSeq, viewAssigned,
      //viewEamil, viewName, viewPassword, viewPhone,
      // viewSeq, viewWait
    ],

    // participantsInInterviews: [],
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
    getUserComSeq(state) {
      return state.user.userComSeq;
    },
    // getCompanyNameList(state){
    //   return state.companyNameList;
    // },
    // getParticipantsInInterview(state) {
    //   return (sessionName) =>
    //     state.participantsInInterviews.filter((interview) => {
    //       return interview.sessionName === sessionName
    //     })
    // },

    //공고 최근순으로 정렬해서 가져오기
    getRecruitListLately: function(state) {
      // return _.sortBy(state.recruitList, 'reSeq').reverse()
      return _.orderBy(state.recruitList, ["reSeq"], ["desc"]);
    }, 
    getRecruitListCount: function(state) {
      return state.recruitList.length;
    },

    //현재 공고의 지원자만 가져오는 로직
    getVieweeListCurrentRecruit: function(state) {
      var list = state.comVieweeList.filter(
        (re) => re.recruitReSeq === state.selectedRecruitNo
      );
      console.log("게터 getVieweeListCurrentRecruit");
      console.log(list);
      console.log(list.length);

      return list;
    },

    getComViewerList(state) {
      console.log("게터 getComViewerList");
      console.log(state.user.comViewerList);
      return state.user.comViewerList;
    },
  },
  mutations: {
    LOGIN(state, res) {
      state.accessToken = res["auth-token"];
      state.user.userSeq = res["user-Seq"];
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
      state.user.userSeq = 0;
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
    USER_UPDATE(state, res) {
      state.user.userName = res["user-Name"];
      state.user.userPhone = res["user-Phone"];
    },
    GET_COMPANY_NAME_LIST(state, res) {
      console.log(typeof(res));
      console.log("mutaions의 GET_COMPANY_NAME_LIST", res);
      state.companyNameList = res;
    },
    GET_COMPANY_LIST(state, res) {
      console.log("mutaions의 GET_COMPANY_LIST", res);
      state.companyList = res;
    },
    GET_RECRUIT_LIST(state, res) {
      console.log("mutaions의 GET_RECRUIT_LIST", res);
      state.recruitList = res;
    },
    INSERT_RECRUIT(state, res) {
      console.log("mutaions의 INSERT_RECRUIT", res);
      state.recruitList.push(res);
    },
    GET_VIEWEE_LIST(state, res) {
      console.log("mutaions의 GET_VIEWEE_LIST", res);
      state.comVieweeList = res;
      // console.log(state.comVieweeList);
    },
    UPDATE_VIEWEE_LIST(state, res) {
      console.log("mutaions의 UPDATE_VIEWEE_LIST", res);
      state.recruitVieweeList = res;
      // console.log(state.recruitVieweeList);
      state.comVieweeList.push(res);
    },
    GET_VIEWER_LIST(state, res) {
      console.log("mutaions의 GET면접관LIST", res);
      state.comViewerList = res;
      // console.log(state.comViewerList);
    },
  },

  actions: {
    // 로그인, 로그아웃
    LOGIN(context, user) {
      axios.post(`${SERVER_URL}/hr/login`, user).then((response) => {
        context.commit("LOGIN", response.data)
        axios.defaults.headers.common[
          "auth-token"
        ] = `${response.data["auth-token"]}`
      }).then((res) => {
        console.log(res)
      }).catch(() => 
      alert("이메일과 비밀번호를 확인해주십시오.")
    )},
    LOGOUT(context) {
      context.commit("LOGOUT");
      axios.defaults.headers.common["auth-token"] = undefined;
    },
    USER_UPDATE(context, userUpdateRequest){
      axios
        .put(`${SERVER_URL}/hr/update/` + this.state.user.userSeq, userUpdateRequest)
        .then((res) => {
          context.commit("USER_UPDATE", res.data)
          alert("회원정보 수정이 완료되었습니다.")
        });
    },
    USER_DELETE(context) {
      axios
        .delete(`${SERVER_URL}/hr/delete/` + this.state.user.userSeq)
        .then(() => {
          alert("회원 탈퇴가 완료되었습니다.")
        });
      context.commit("LOGOUT");
    },

    GET_COMPANY_NAME_LIST(context) {
      axios
        .get(`${SERVER_URL}/recruit/companyNameList`)
        .then((res) => {
          context.commit("GET_COMPANY_NAME_LIST", res.data);
          console.log("회사 이름 리스트")
          console.log(res.data);
        });
    },
    GET_COMPANY_LIST(context) {
      axios
        .get(`${SERVER_URL}/recruit/companyList`)
        .then((response) => {
          context.commit("GET_COMPANY_LIST", response.data);
          console.log("회사 리스트")
          console.log(response.data);
        });
    },
 
    GET_RECRUIT_LIST(context) {
      axios
        .get(`${SERVER_URL}/recruit/getList/` + this.state.user.userComSeq)
        .then((response) => {
          context.commit("GET_RECRUIT_LIST", response.data);
          console.log("겟 공고 실행");
          console.log(response.data);
        });
      // context.commit("GET_RECRUIT_LIST");
    },

    INSERT_RECRUIT(context, newRecruit) {
      axios
        .post(
          `${SERVER_URL}/recruit/register/` + this.state.user.userComSeq,
          newRecruit
        )
        .then((response) => {
          context.commit("INSERT_RECRUIT", response.data);
          console.log("인서트 공고 실행");
          console.log(response.data);
        });
    },

    DELETE_RECRUIT(context, reSeq){
      axios.delete(`${SERVER_URL}/recruit/delete/` + reSeq)
      .then(
        alert("삭제되었습니다!")
      );
    },

    //지원자를 엑셀로 저장
    UPDATE_VIEWEE_LIST(context, recruitNo) {
      axios
        .get(`${SERVER_URL}/applicant/getList/${recruitNo}`)
        .then((res) => {
          context.commit("UPDATE_VIEWEE_LIST", res.data);
          console.log("업데이트 지원자 리스트 실행");
          console.log(res.data);
        })
        .catch((err) => {
          console.log(err);
        });
    },

    //지원자 리스트를 불러온다. //이거.. 공고별이 아니라, 회사 전체의 리스트를 불러올 수 있어야 함.
    GET_VIEWEE_LIST(context, comSeq) {
      axios
        .get(`${SERVER_URL}/applicant/getListByCompany/${comSeq}`)
        .then((res) => {
          context.commit("GET_VIEWEE_LIST", res.data);
          console.log("겟 지원자 리스트 실행");
          console.log(res.data);
        })
        .catch((err) => {
          console.log(err);
        });
    },

    GET_VIEWER_LIST(context, comSeq) {
      axios
        .get(`${SERVER_URL}/interviewer/getList/${comSeq}`)
        .then((res) => {
          context.commit("GET_VIEWER_LIST", res.data);
          console.log("겟 인터뷰 리스트 실행");
          console.log(res.data);
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
  plugins: [createPersistedState()],
});
