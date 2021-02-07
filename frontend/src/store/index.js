import Vue from "vue"
import Vuex from "vuex"
import axios from "axios"
import createPersistedState from "vuex-persistedstate"
import _ from "lodash"

Vue.use(Vuex)
 const SERVER_URL = "https://localhost:8080"
// const SERVER_URL = "https://i4a405.p.ssafy.io:8080"
// const SERVER_URL = process.env.VUE_APP_SERVER_URL

let userInfo = {
  userEmail: 'user-Email',
  userName: 'user-Name',
  userPhone: 'user-Phone',
  userViewWait: 'user-View-Wait',
  userComSeq: 'user-Company-Seq',
  userComName: 'user-Company-Name',
  userComLogo: 'user-Company-Logo',
  userComAddress: 'user-Company-Address',
  userComHomepage: 'user-Company-Homepage'
}

export default new Vuex.Store({
  state: {
    // 로그인 정보
    // isLogin: false,
    // isManager: false,
    // userEmail: '',
    // comSeq: 0,
    accessToken: null,
    // userViewWait => 채용담당자(-1), 대기관(0), 면접관(1) 구분자
    user: {
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

    //채용담당자가 선택한 현재 공고 :
    selectRecruitTrigger: false,
    selectedRecruitNo: -1,

    //그냥 인담자, 면접관, 지원자 로그인 상황 한 변수로 통일하는 게 낫다.
    whoLogin: "viewer", //Manager, viewer, viewee

    comData: {
      comSeq: "1",
      comName: "WieV Inc.",
      comLogo: "asdf",
      comAddress: "서울특별시 강남구 테헤란로",
      comHomepage: "https://www.naver.com",
    },

    // 전체 채용공고 리스트
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

    // 얘는 공고별 저장인데 comVieweeList 쓸거같다.
    recruitVieweeList: [],

    // 회사 전체 지원자 리스트
    comVieweeList: [],

    // 회사 전체 면접관 리스트
    comViewerList: [
      //careerCaSeq , careerPartPartSeq , companyComSeq, viewAssigned,
      //viewEamil, viewName, viewPassword, viewPhone,
      // viewSeq, viewWait
    ],

    // participantsInInterviews: [],
  },




  getters: {
    getUser(state) {
      return state.user
    },
    getAccessToken(state) {
      return state.accessToken
    },
    getUserViewWait(state) {
      return state.user.userViewWait
    },
    getUserComSeq(state) {
      return state.user.userComSeq
    },
    // getParticipantsInInterview(state) {
    //   return (sessionName) =>
    //     state.participantsInInterviews.filter((interview) => {
    //       return interview.sessionName === sessionName
    //     })
    // },

    // 공고 최근순으로 정렬해서 가져오기
    getRecruitListLately: function(state) {
      // return _.sortBy(state.recruitList, 'reSeq').reverse()
      return _.orderBy(state.recruitList, ["reSeq"], ["desc"])
    },

    // 현재 공고의 지원자만 가져오기
    getVieweeListCurrentRecruit: function(state) {
      let list = state.comVieweeList.filter(
        (re) => re.recruitReSeq === state.selectedRecruitNo
      )
      console.log("현재 공고의 지원자 가져오기")
      console.log(list)

      return list
    },

    // 회사 전체 면접관 리스트 가져오기
    getComViewerList(state) {
      console.log("회사 전체 면접관 가져오기")
      console.log(state.user.comViewerList)
      return state.user.comViewerList
    },
  },




  mutations: {
    // 로그인 프로세스
    LOGIN(state, res) {
      state.accessToken = res["auth-token"]
      for (const key in userInfo) {
        state.user[key] = res[userInfo[key]]
      }
    },
    // 로그아웃 프로세스
    LOGOUT(state) {
      state.accessToken = null
      for (const key in userInfo) {
        state.user[key] = ''
      }
      state.user.userViewWait = 0
      state.user.userComSeq = 0
    },
    // 공고 리스트 state에 저장
    GET_RECRUIT_LIST(state, res) {
      console.log("mutaions의 GET_RECRUIT_LIST", res)
      state.recruitList = res
    },
    // 생성한 공고 state에 저장
    INSERT_RECRUIT(state, res) {
      console.log("mutaions의 INSERT_RECRUIT", res)
      state.recruitList.push(res)
    },
    // 지원자 리스트 state에 저장
    GET_VIEWEE_LIST(state, res) {
      console.log("mutaions의 GET_VIEWEE_LIST", res)
      state.comVieweeList = res
    },
    // 업데이트된 지원자 리스트 state에 저장
    UPDATE_VIEWEE_LIST(state, res) {
      console.log("mutaions의 UPDATE_VIEWEE_LIST", res)
      // 얘는 뭐지?
      state.recruitVieweeList = res
      state.comVieweeList.push(res)
    },
    // 면접관 리스트 state에 저장
    GET_VIEWER_LIST(state, res) {
      console.log("mutaions의 GET면접관LIST", res)
      state.comViewerList = res
    },
  },




  actions: {
    // 로그인
    LOGIN(context, user) {
      axios.post(`${SERVER_URL}/hr/login`, user)
        .then(res => {
          context.commit("LOGIN", res.data)
          axios.defaults.headers.common["auth-token"] = `${res.data["auth-token"]}`
        })
        .then(res => console.log(res))
        .catch(() => alert("이메일과 비밀번호를 확인해주십시오."))
    },
    // 로그아웃
    LOGOUT(context) {
      context.commit("LOGOUT")
      axios.defaults.headers.common["auth-token"] = undefined
    },
    // Action : 공고 리스트 가져오기
    GET_RECRUIT_LIST(context) {
      axios.get(`${SERVER_URL}/recruit/getList/` + this.state.user.userComSeq)
        .then(res => {
          console.log(res.data)
          context.commit("GET_RECRUIT_LIST", res.data)
          console.log("전체 공고 가져오기")
        })
      // context.commit("GET_RECRUIT_LIST")
    },
    // Action : 신규 공고 생성하기
    INSERT_RECRUIT(context, newRecruit) {
      axios.post(`${SERVER_URL}/recruit/register/` + this.state.user.userComSeq, newRecruit)
        .then(res => {
          console.log(res.data)
          context.commit("INSERT_RECRUIT", res.data)
          console.log("신규 공고 생성하기")
        })
    },
    // 공고 삭제하기
    DELETE_RECRUIT(context, reSeq){
      axios.delete(`${SERVER_URL}/recruit/delete/` + reSeq)
        .then(() => alert("공고가 삭제되었습니다!"))
    },
    // 지원자를 엑셀로 업데이트하기
    UPDATE_VIEWEE_LIST(context, recruitNo) {
      axios.get(`${SERVER_URL}/applicant/getList/${recruitNo}`)
        .then(res => {
          console.log(res.data)
          context.commit("UPDATE_VIEWEE_LIST", res.data)
          console.log("지원자 리스트 업데이트하기")
        })
        .catch(err => console.log(err))
    },
    // 전체 지원자 리스트 가져오기
    //이거.. 공고별이 아니라, 회사 전체의 리스트를 불러올 수 있어야 함.
    GET_VIEWEE_LIST(context, comSeq) {
      axios.get(`${SERVER_URL}/applicant/getListByCompany/${comSeq}`)
        .then(res => {
          console.log(res.data)
          context.commit("GET_VIEWEE_LIST", res.data)
          console.log("전체 지원자 리스트 가져오기")
        })
        .catch(err => console.log(err))
    },
    // 전체 면접관 리스트 가져오기
    GET_VIEWER_LIST(context, comSeq) {
      axios.get(`${SERVER_URL}/interviewer/getList/${comSeq}`)
        .then(res => {
          console.log(res.data)
          context.commit("GET_VIEWER_LIST", res.data)
          console.log("전체 면접관 리스트 가져오기")
        })
        .catch(err => console.log(err))
    }
  },

  plugins: [createPersistedState()],
})
