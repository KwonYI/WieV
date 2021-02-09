import Vue from "vue"
import Vuex from "vuex"
import axios from "axios"
import createPersistedState from "vuex-persistedstate"
import _ from "lodash"

Vue.use(Vuex)
// const SERVER_URL = "https://localhost:8080"
// const SERVER_URL = "https://i4a405.p.ssafy.io:8080"
const SERVER_URL = process.env.VUE_APP_SERVER_URL

let userInfo = {
  userEmail: 'user-Email',
  userSeq: "user-Seq",
  userName: 'user-Name',
  userPhone: 'user-Phone',
  userPassword: 'user-Password',
  userViewWait: 'user-View-Wait',
  userComSeq: 'user-Company-Seq',
  userComName: 'user-Company-Name',
  userComLogo: 'user-Company-Logo',
  userComAddress: 'user-Company-Address',
  userComHomepage: 'user-Company-Homepage',
}

let vieweeInfo = {
  applyPhone: 'apply-Phone',
  applyMajor: 'apply-Major',
  applyName: 'apply-Name',
  applyBirth: 'apply-Birth',
  applyEmail: 'apply-Email',
  applyGrade: 'apply-Grade',
  applyRecruitSeq: 'apply-Recruit-Seq',
  applyId: 'apply-Id',
  applyResume4: 'apply-Resume4',
  applyAssigned: 'apply-Assigned',
  applySeq: 'apply-Seq',
  applyUniversity: 'apply-University',
  applyCareerName: 'apply-Career-Name',
  applyResume1: 'apply-Resume1',
  applyResume2: 'apply-Resume2',
  applyResume3: 'apply-Resume3',

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
      userSeq: 0,
      userEmail: "",
      userName: "",
      userPhone: "",
      userPassword: "",
      userViewWait: 0,
      userComSeq: 0,
      userComName: "",
      userComLogo: "",
      userComAddress: "",
      userComHomepage: "",
    },

    viewee: {
      applyPhone: '',
      applyMajor: '',
      applyName: '',
      applyBirth: '',
      applyEmail: '',
      applyGrade: 0.0,
      applyRecruitSeq: 0,
      applyId: '',
      applyResume4: '',
      applyAssigned: 0,
      applySeq: 0,
      applyUniversity: '',
      applyCareerName: '',
      applyResume1: '',
      applyResume2: '',
      applyResume3: '',
    },

    //채용담당자가 선택한 현재 공고 :
    selectedRecruitNo: -1,
    storeRecruitItem: {},

    //그냥 인담자, 면접관, 지원자 로그인 상황 한 변수로 통일하는 게 낫다.
    whoLogin: "viewer", //Manager, viewer, viewee

    companyNameList: [],
    

    companyList: [{
      comSeq: 0,
      comName: "",
      comLogo: "",
      comAddress: "",
      comHomepage: "",
    }, ],

    // 전체 채용공고 리스트
    recruitList: [{
      reSeq: 0,
      reYear: 0,
      reFlag: "",
      reStatus: "",
      reStartDate: "",
      reEndDate: "",
    }, ],

    // 공고별 면접현황 리스트
    recruitProgressList: [],

    // 얘는 공고별 저장인데 comVieweeList 쓸거같다.
    // recruitVieweeList: [],

    // 얘는 공고별 지원자 저장인데 comVieweeList 쓸거같다.
    recruitVieweeList: [],


    // 회사별 면접현황 리스트
    comProgressList: [],

    // 회사 전체 지원자 리스트
    comVieweeList: [],

    // 회사 전체 면접관 리스트
    comViewerList: [
      //careerCaSeq , careerPartPartSeq , companyComSeq, viewAssigned,
      //viewEamil, viewName, viewPassword, viewPhone,
      // viewSeq, viewWait
    ],

    participants: [],

    checkIn: [],
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
    // getCompanyNameList(state){
    //   return state.companyNameList
    // },
    // getParticipantsInInterview(state) {
    //   return (sessionName) =>
    //     state.participantsInInterviews.filter((interview) => {
    //       return interview.sessionName === sessionName
    //     })
    // },

    // 공고 최근순으로 정렬해서 가져오기
    getRecruitListLately: function (state) {
      // return _.sortBy(state.recruitList, 'reSeq').reverse()
      return _.orderBy(state.recruitList, ["reSeq"], ["desc"])
    },
    getRecruitListCount: function (state) {
      return state.recruitList.length
    },

    // 현재 공고의 지원자만 가져오기
    getVieweeListCurrentRecruit: function (state) {
      let list = state.comVieweeList.filter(
        (re) => re.applyRecruitSeq === state.selectedRecruitNo
      )
      // console.log("현재 공고의 지원자 가져오기")
      // console.log(list)

      return list
    },
    // // 현재 공고의 면접현황만 가져오기 => 안써요
    // getProgressListCurrentRecruit: function(state) {
    //   let list = state.comVieweeList.filter(
    //     (re) => re.recruitReSeq === state.selectedRecruitNo
    //   )
    //   console.log("현재 공고의 면접현황 가져오기")
    //   console.log(list)

    //   return list
    // },

    // 현재공고의 면접현황만 가져오기
    getProgressListCurrentRecruit: function (state) {
      let list = state.comProgressList.filter(
        (re) => re.recruitSeq === state.selectedRecruitNo
      )
      // console.log("현재", state.selectedRecruitNo, "공고의 면접현황 가져오기", list[0].groupInfoList)
      return list[0].groupInfoList

    },

    // 회사 전체 면접관 리스트 가져오기
    getComViewerList(state) {
      return state.user.comViewerList
    },
    

    getParticipants(state) {
      // console.log("게터 실행, 모든 연결 참가자 정보 수")
      // console.log(state.participants.length)
      return state.participants
    },

    getCheckIn(state) {
      // console.log("게터 실행, 모든 연결 수")
      // console.log(state.checkIn.length)
      return state.checkIn
    },

  },

  mutations: {

    // 로그인 프로세스
    LOGIN(state, res) {
      // state.accessToken = res["auth-token"]
      // state.user.userSeq = res["user-Seq"]
      // state.user.userEmail = res["user-Email"]
      // state.user.userName = res["user-Name"]
      // state.user.userPhone = res["user-Phone"]
      // state.user.userViewWait = res["user-View-Wait"]
      // state.user.userComSeq = res["user-Company-Seq"]
      // state.user.userComName = res["user-Company-Name"]
      // state.user.userComLogo = res["user-Company-Logo"]
      // state.user.userComAddress = res["user-Company-Address"]
      // state.user.userComHomepage = res["user-Company-Homepage"]
      state.accessToken = res["auth-token"]
      for (const key in userInfo) {
        state.user[key] = res[userInfo[key]]
      }
      console.log("로긴", state.user)
    },
    // 로그아웃 프로세스
    LOGOUT(state) {
      // state.accessToken = null
      // state.user.userSeq = 0
      // state.user.userEmail = ""
      // state.user.userName = ""
      // state.user.userPhone = ""
      // state.user.userViewWait = 0
      // state.user.userComSeq = 0
      // state.user.userComName = ""
      // state.user.userComLogo = ""
      // state.user.userComAddress = ""
      // state.user.userComHomepage = ""

      state.accessToken = null
      for (const key in userInfo) {
        state.user[key] = ''
      }
      state.user.userViewWait = 0
      state.user.userComSeq = 0
      state.user.userSeq = 0
      state.comVieweeList = []
    },

    USER_UPDATE(state, res) {
      state.user.userName = res["user-Name"]
      state.user.userPhone = res["user-Phone"]
    },

    // CLEAR_COMVIEWEELIST(){
    //   state.comVieweeList = ''

    // },

    GET_COMPANY_NAME_LIST(state, res) {
      // console.log(typeof res)
      // console.log("mutaions의 GET_COMPANY_NAME_LIST", res)
      state.companyNameList = res
    },
    GET_COMPANY_LIST(state, res) {
      // console.log("mutaions의 GET_COMPANY_LIST", res)
      state.companyList = res
    },

    // 공고 리스트 state에 저장
    GET_RECRUIT_LIST(state, res) {
      // console.log("mutaions의 GET_RECRUIT_LIST", res)
      state.recruitList = res
    },
    // 생성한 공고 state에 저장
    INSERT_RECRUIT(state, res) {
      // console.log("mutaions의 INSERT_RECRUIT", res)
      state.recruitList.push(res)
    },

    // // 업데이트된 지원자 리스트 state에 저장
    // UPDATE_VIEWEE_LIST(state, res) {
    //   console.log("mutaions의 UPDATE_VIEWEE_LIST", res)
    //   // 얘는 뭐지?
    //   state.recruitVieweeList = res
    //   state.comVieweeList.push(res)
    // },
    // 면접관 리스트 state에 저장
    GET_VIEWER_LIST(state, res) {
      state.comViewerList = res
    },
    addParticipants(state, data) {
      // console.log("뮤테이션 실행 밑에 있는 데이터 넣을 예정")
      // console.log(data)
      state.participants.push(data)
    },
    deleteParticipants(state, data) {
      const index = state.participants.indexOf(data, 0)
      // console.log("정보 삭제 시도")
      // console.log("변경전 : ", state.participants.length)
      if (index >= 0) {
        state.participants.splice(index, 1)
      }
      // console.log("변경후 : ", state.participants.length)
    },
    clearParticipants(state, data) {
      // console.log("클리어 실행, 실행 후 남은 정보")
      state.participants = data
      // console.log(state.participants.length)
    },
    addCheckIn(state, data) {
      // console.log("뮤테이션 실행 밑에 있는 데이터 넣을 예정")
      // console.log(data)
      state.checkIn.push(data)
    },
    deleteCheckIn(state, data) {
      const index = state.checkIn.indexOf(data, 0)
      // console.log("정보 삭제 시도")
      // console.log("변경전 : ", state.checkIn.length)
      if (index >= 0) {
        state.checkIn.splice(index, 1)
      }
      // console.log("변경후 : ", state.checkIn.length)
    },
    clearcheckIn(state, data) {
      // console.log("클리어 실행, 실행 후 남은 정보")
      state.checkIn = data
      // console.log(state.checkIn.length)
    },

    // 공고 면접현황 리스트 state에 저장
    GET_PROGRESS_LIST(state, res) {
      // console.log("GET_PROGRESS_LIST", res)
      state.comProgressList = res
    },

    // 지원자 리스트 state에 저장
    GET_VIEWEE_LIST(state, res) {
      state.comVieweeList = []
      for (var i = 0; i < res.length; i++) {
        // console.log(res[i])
        state.viewee = {}
        for (const key in vieweeInfo) {
          state.viewee[key] = res[i][vieweeInfo[key]]
          // state.comVieweeList[key] = res[vieweeInfo[key]]
          // var tempi[key] = res[i][vieweeInfo[key]]
        }
        state.comVieweeList.push(state.viewee)
      }
      
      
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
    USER_UPDATE(context, userUpdateRequest) {
      axios.put(`${SERVER_URL}/hr/update/` + this.state.user.userSeq, userUpdateRequest)
        .then(res => {
          context.commit("USER_UPDATE", res.data)
          alert("회원정보 수정이 완료되었습니다.")
        })
    },
    USER_DELETE(context) {
      axios.delete(`${SERVER_URL}/hr/delete/` + this.state.user.userSeq)
        .then(() => {
          alert("회원 탈퇴가 완료되었습니다.")
        })
      context.commit("LOGOUT")
    },

    GET_COMPANY_NAME_LIST(context) {
      axios.get(`${SERVER_URL}/recruit/companyNameList`)
        .then(res => {
          context.commit("GET_COMPANY_NAME_LIST", res.data)
        })
    },
    GET_COMPANY_LIST(context) {
      axios.get(`${SERVER_URL}/recruit/companyList`)
        .then(res => {
          context.commit("GET_COMPANY_LIST", res.data)
        })
    },

    GET_RECRUIT_LIST(context) {
      axios.get(`${SERVER_URL}/recruit/getList/` + this.state.user.userComSeq)
        .then(res => {
          context.commit("GET_RECRUIT_LIST", res.data)
        })
      // context.commit("GET_RECRUIT_LIST")
    },
    // Action : 신규 공고 생성하기
    INSERT_RECRUIT(context, newRecruit) {
      axios.post(`${SERVER_URL}/recruit/register/` + this.state.user.userComSeq, newRecruit)
        .then(res => {
          context.commit("INSERT_RECRUIT", res.data)
        })
    },
    // 공고 삭제하기
    DELETE_RECRUIT(context, reSeq) {
      axios.delete(`${SERVER_URL}/recruit/delete/` + reSeq)
        .then(() => alert("공고가 삭제되었습니다!"))
    },
    // 지원자를 엑셀로 업데이트하기
    UPDATE_VIEWEE_LIST(context, recruitNo) {
      axios.get(`${SERVER_URL}/applicant/getList/${recruitNo}`)
        .then(res => {
          context.commit("UPDATE_VIEWEE_LIST", res.data)
        })
        .catch(err => console.log(err))
    },
    // 전체 지원자 리스트 가져오기
    //이거.. 공고별이 아니라, 회사 전체의 리스트를 불러올 수 있어야 함.
    GET_VIEWEE_LIST(context, comSeq) {
      axios.get(`${SERVER_URL}/applicant/getListByCompany/${comSeq}`)
        .then(res => {
          context.commit("GET_VIEWEE_LIST", res.data)
        })
        .catch(err => console.log(err))
    },
    // 전체 면접관 리스트 가져오기
    GET_VIEWER_LIST(context, comSeq) {
      axios.get(`${SERVER_URL}/interviewer/getList/${comSeq}`)
        .then(res => {
          context.commit("GET_VIEWER_LIST", res.data)
        })
        .catch(err => console.log(err))
    },

    //회사의 면접현황 리스트 가져오기
    GET_PROGRESS_LIST(context, comSeq) {
      axios.get(`${SERVER_URL}/groupInfo/comTotalGroupList/${comSeq}`)
        .then(res => {
          context.commit("GET_PROGRESS_LIST", res.data)
        })
        .catch(err => console.log(err))
    }


  },

  plugins: [createPersistedState()],
})
