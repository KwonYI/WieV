package com.web.project.controller.group;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.project.controller.hr.HrController;
import com.web.project.dao.group.GroupAllDao;
import com.web.project.dao.group.GroupDetailDao;
import com.web.project.dao.group.GroupTypeDao;
import com.web.project.dao.hr.CareerDao;
import com.web.project.dao.hr.PartDao;
import com.web.project.dao.interview.InterviewTypeDao;
import com.web.project.dao.interview.InterviewerDao;
import com.web.project.dao.interview.TypeInterviewerDao;
import com.web.project.dao.recruit.ApplicantDao;
import com.web.project.dao.recruit.ApplicantGroupDao;
import com.web.project.dao.recruit.RecruitDao;
import com.web.project.model.BasicResponse;
import com.web.project.model.group.GroupAll;
import com.web.project.model.group.GroupAllRequest;
import com.web.project.model.group.GroupDetail;
import com.web.project.model.group.GroupType;
import com.web.project.model.interview.InterviewType;
import com.web.project.model.interview.Interviewer;
import com.web.project.model.interview.TypeInterviewer;
import com.web.project.model.recruit.Applicant;
import com.web.project.model.recruit.ApplicantGroup;
import com.web.project.model.recruit.Recruit;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
		@ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
		@ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
		@ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })
@CrossOrigin(origins = { "http://localhost:3000" })
@RestController
@RequestMapping("/groupAll")
public class GroupAllController {

	@Autowired
	GroupAllDao groupAllDao;

	@Autowired
	RecruitDao recruitDao;

	@Autowired
	CareerDao careerDao;

	@Autowired
	PartDao partDao;

	@Autowired
	ApplicantDao applicantDao;

	@Autowired
	InterviewTypeDao interviewTypeDao;

	@Autowired
	GroupTypeDao groupTypeDao;

	@Autowired
	InterviewerDao interviewerDao;

	@Autowired
	TypeInterviewerDao typeInterviewerDao;
	
	@Autowired
	GroupDetailDao groupDetailDao;

	@Autowired
	ApplicantGroupDao applicantGroupDao;
	
	public static final Logger logger = LoggerFactory.getLogger(HrController.class);
	
	// 그룹을 만들때 
	@GetMapping("/career")
	@ApiOperation(value = "직무 모두 가져오기")
	public ResponseEntity<List<InterviewType>> getCareer() {
		return new ResponseEntity<List<InterviewType>>(interviewTypeDao.findAll(), HttpStatus.OK);
	}
	
	@GetMapping("/interviewTypeList/{groupSeq}")
	@ApiOperation(value = "그룹에 해당하는 직무 리스트 가져오기")
	public ResponseEntity<List<InterviewType>> getGroupInterviewTypeList(@PathVariable("groupSeq") int groupSeq) {
		HttpStatus status = null;
		List<InterviewType> groupInterviewTypeList = new ArrayList<InterviewType>();

		try {
			// 1. 해당 그룹별 면접 종류 리스트 가져오기
			List<GroupType> groupTypeList = groupTypeDao.findListGroupTypeByGroupGroupSeq(groupSeq);

			// 2. 해당 그룹의 면접 종류 리스트로 만들어서 저장하기
			int groupTypeListSize = groupTypeList.size();
			for (int i = 0; i < groupTypeListSize; i++) {
				groupInterviewTypeList.add(
						interviewTypeDao.findInterviewTypeByTypeSeq(groupTypeList.get(i).getInterviewTypeTypeSeq()));
			}

			status = HttpStatus.OK;
		} catch (RuntimeException e) {
			groupInterviewTypeList = null;
			logger.error("그룹에 해당하는 직무 가져오기 실패", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return new ResponseEntity<List<InterviewType>>(groupInterviewTypeList, status);
	}
	
	@GetMapping("/applicantList/{groupSeq}")
	@ApiOperation(value = "그룹에 해당하는 지원자 리스트 가져오기")
	public ResponseEntity<List<Applicant>> getGroupApplicantList(@PathVariable("groupSeq") int groupSeq) {
		HttpStatus status = null;
		List<Applicant> applicantList = new ArrayList<Applicant>();
		List<ApplicantGroup> applicantGroupList=new ArrayList<ApplicantGroup>();
		
		try {
			// 1. 해당 그룹별 면접 세부일정  가져오기
			List<GroupDetail> groupDetailList = groupDetailDao.findAllGroupDetailByGroupGroupSeq(groupSeq);
			
			for (int i = 0; i < groupDetailList.size(); i++) {
				GroupDetail nowGroupDetail=groupDetailList.get(i);
				List<ApplicantGroup> tmpApplicantGroupList=new ArrayList<ApplicantGroup>();
				
				tmpApplicantGroupList=applicantGroupDao.findAllApplicantGroupByGroupDetailDetailSeq(nowGroupDetail.getDetailSeq());
				
				for (int j = 0; j < tmpApplicantGroupList.size(); j++) {
					applicantGroupList.add(tmpApplicantGroupList.get(j));
				}
			}
			
			//2. 세부일정 안에 있는 지원자 갖고오기
			for (int i = 0; i < applicantGroupList.size(); i++) {
				ApplicantGroup nowApplicantGroup=applicantGroupList.get(i);
				applicantList.add(applicantDao.findApplicantByApplySeq(nowApplicantGroup.getApplicantApplySeq()));
			}
			
			status = HttpStatus.OK;
		} catch (RuntimeException e) {
			logger.error("그룹에 해당하는 지원자 리스트 가져오기 실패", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return new ResponseEntity<List<Applicant>>(applicantList, status);
	}
	
	@PutMapping("/applicantList")
	@ApiOperation(value = "그룹에 해당하는 지원자 리스트 수정하기")
	public Object updateApplicantList () {
		HttpStatus status = null;
		
		return new ResponseEntity<> (null, status);
	}
	
	@GetMapping("/WaitInterviewerList/{groupSeq}")
	@ApiOperation(value = "그룹에 해당하는 대기관 리스트 가져오기")
	public ResponseEntity<List<Interviewer>> getWaitInterviewerList (@PathVariable("groupSeq") int groupSeq) {
		HttpStatus status = null;
		List<Interviewer> interviewerList = new ArrayList<Interviewer>();
		List<TypeInterviewer> typeInterviewerList = new ArrayList<TypeInterviewer>();
		try {
			// 1. 해당 그룹별 면접 종류 리스트 가져오기
			List<GroupType> groupTypeList = groupTypeDao.findListGroupTypeByGroupGroupSeq(groupSeq);

			// 2. 해당 면접 종류별 관리자 리스트 갖고오기
			for (int i = 0; i < groupTypeList.size(); i++) {
				GroupType nowGroupType = groupTypeList.get(i);
				List<TypeInterviewer> tmpTypeInterviewerList=new ArrayList<TypeInterviewer>();
				tmpTypeInterviewerList=typeInterviewerDao.findAllTypeInterviewerByGroupTypeGroupTypeSeqAndGroupTypeInterviewTypeTypeSeq(nowGroupType.getGroupTypeSeq(),nowGroupType.getInterviewTypeTypeSeq());
				
				for (int j = 0; j < tmpTypeInterviewerList.size(); j++) {
					typeInterviewerList.add(tmpTypeInterviewerList.get(j));
				}
			}

			// 3. 해당 그룹의 관리자들 리스트로 만들어서 저장하기
			for (int i = 0; i < typeInterviewerList.size(); i++) {
				TypeInterviewer nowTypeInterviewer = typeInterviewerList.get(i);
				Interviewer nowInterviewer = interviewerDao
						.findInterviewerByViewSeq(nowTypeInterviewer.getInterviewerViewSeq());

				if (nowInterviewer.getViewWait() == 0) {
					// 관리자이면
					interviewerList.add(nowInterviewer);
				}
			}

			status = HttpStatus.OK;
		} catch (RuntimeException e) {
			logger.error("그룹에 해당하는 관리자 가져오기 실패", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return new ResponseEntity<List<Interviewer>>(interviewerList, status);
	}
	
	@PutMapping("/waitInterviewerList")
	@ApiOperation(value = "그룹에 해당하는 대기관 리스트 수정하기")
	public Object updateWaitInterviewerList () {
		HttpStatus status = null;
		
		return new ResponseEntity<> (null, status);
	}
	
	@GetMapping("/InterviewerList/{groupSeq}")
	@ApiOperation(value = "그룹에 해당하는 면접관 리스트 가져오기")
	public ResponseEntity<List<Interviewer>> getGroupInterviewerList(@PathVariable("groupSeq") int groupSeq) {
		HttpStatus status = null;
		List<Interviewer> interviewerList = new ArrayList<Interviewer>();
		List<TypeInterviewer> typeInterviewerList = new ArrayList<TypeInterviewer>();
		try {
			// 1. 해당 그룹별 면접 종류 리스트 가져오기
			List<GroupType> groupTypeList = groupTypeDao.findListGroupTypeByGroupGroupSeq(groupSeq);

			// 2. 해당 면접 종류별 면접관 리스트 갖고오기
			for (int i = 0; i < groupTypeList.size(); i++) {
				GroupType nowGroupType = groupTypeList.get(i);
				List<TypeInterviewer> tmpTypeInterviewerList=new ArrayList<TypeInterviewer>();
				tmpTypeInterviewerList=typeInterviewerDao.findAllTypeInterviewerByGroupTypeGroupTypeSeqAndGroupTypeInterviewTypeTypeSeq(nowGroupType.getGroupTypeSeq(),nowGroupType.getInterviewTypeTypeSeq());
				
				for (int j = 0; j < tmpTypeInterviewerList.size(); j++) {
					typeInterviewerList.add(tmpTypeInterviewerList.get(j));
				}
			}

			// 3. 해당 그룹의 면접관들 리스트로 만들어서 저장하기
			for (int i = 0; i < typeInterviewerList.size(); i++) {
				TypeInterviewer nowTypeInterviewer = typeInterviewerList.get(i);
				Interviewer nowInterviewer = interviewerDao
						.findInterviewerByViewSeq(nowTypeInterviewer.getInterviewerViewSeq());

				if (nowInterviewer.getViewWait() == 1) {
					// 면접관이면
					interviewerList.add(nowInterviewer);
				}
			}

			status = HttpStatus.OK;
		} catch (RuntimeException e) {
			logger.error("그룹에 해당하는 면접관 리스트 가져오기 실패", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return new ResponseEntity<List<Interviewer>>(interviewerList, status);
	}
	
	@PutMapping("/interviewerList")
	@ApiOperation(value = "그룹에 해당하는 면접관 리스트 수정하기")
	public Object updateInterviewerList () {
		HttpStatus status = null;
		
		return new ResponseEntity<> (null, status);
	}
	
	@PostMapping("/divide")
	@ApiOperation(value = "그룹 나누기")
	public Object divide(@Valid @RequestBody GroupAllRequest groupAllRequest, int recruitSeq) {
		HttpStatus status = null;
		GroupAll resultGroupAll = null;

		Recruit recruit = recruitDao.findRecruitByReSeq(recruitSeq);

		try {
			if (recruit != null) {
				int partSeq = partDao.findPartByPartName(groupAllRequest.getPart()).getPartSeq();
				int careerCaSeq = careerDao.findCareerByCaNameAndPartPartSeq(groupAllRequest.getCareer(), partSeq)
						.getCaSeq();

				// 몇 개의 그룹이 필요한지 결정하기 - 공고 날짜, 끝시간 - 시작시간, 소요시간
				// 1. 공고의 시작, 끝 날짜를 통해 몇일동안 면접을 진행하는지 파악
				int fullRoutine = calFullRoutine(recruit.getReStartDate(), recruit.getReEndDate());

				// 2. 하루 면접이 언제 시작하고 끝나는지 파악
				int dailyRoutine = Integer.parseInt(groupAllRequest.getEndTime())
						- Integer.parseInt(groupAllRequest.getStartTime());

				// 3. 그룹 개수 결정하기
				int groupCount = (fullRoutine * dailyRoutine) / groupAllRequest.getDivideTime();

				// 4. 총 명수 구하기 -> applicant dao에서 careerSeq랑 recruitSeq로 인원수 구해오기
				int totalApplyCount = (int) applicantDao.countByRecruitReSeqAndCareerCaSeq(recruitSeq, careerCaSeq);

				// 5. 그룹 결정하기
				int time = 1;
				int nowTime = Integer.parseInt(groupAllRequest.getStartTime());
				String nowDate = recruit.getReStartDate();
				for (int i = 1; i <= groupCount; i++) {
					// 이미 지원자를 다 넣은 경우, 종료
					if (totalApplyCount <= 0)
						break;

					GroupAll groupAll = new GroupAll();

					// 기본 세팅 - 외래키, 그룹 당 인원, 이력서, 자기소개 오픈 여부, 방 당 면접관수, 그룹 면접당 유형개수
					groupAll.setRecruitReSeq(recruit.getReSeq());
					groupAll.setCareerCaSeq(careerCaSeq);
					groupAll.setInterviewerDivide(groupAllRequest.getDivideInterviewer());
					groupAll.setGroupTypeCnt(groupAllRequest.getInterviewTypeList().size());

					// 지원자 명수대로 배치하기
					// 만약 배치 명수보다 적으면 그냥 그대로 배치
					int divideNum = groupAllRequest.getDivideNumber();
					if (totalApplyCount < groupAllRequest.getDivideNumber()) {
						divideNum = totalApplyCount;
					}
					groupAll.setGroupDivide(divideNum);
					totalApplyCount -= groupAllRequest.getDivideNumber();

					groupAll.setGroupVisible(groupAllRequest.getDivideVisible());

					// 각 그룹 날짜 세팅
					// 오늘 할당된 시간보다 늦어진 경우
					if (nowTime > Integer.parseInt(groupAllRequest.getEndTime())) {
						// 다음날로 넘어간다.
						// 시간 초기화
						time = 1;
						nowTime = Integer.parseInt(groupAllRequest.getStartTime());
						// 날짜 계산하기
						nowDate = findNextDate(nowDate);
					}
					groupAll.setGroupStartTime(Integer.toString(nowTime));
					groupAll.setGroupDate(nowDate);

					// Insert -> 하나씩 들어감!
					resultGroupAll = groupAllDao.save(groupAll);

					// 날짜 & 시간 -> 시작 시간을 기준으로 끝 시간까지 소요 시간을 더하기
					nowTime += (time * groupAllRequest.getDivideTime());

					time++;

//					System.out.println(groupAllDao.findGroupAllByGroupDate(groupAll.getGroupDate()).getGroupSeq());
//					System.out.println(i);

					// 그룹별 면접 종류 만들기!
					int interviewTypeCount = groupAllRequest.getInterviewTypeList().size();
//					System.out.println("면접 종류는 " + interviewTypeCount + "개 입니다.");

					for (int j = 0; j < interviewTypeCount; j++) {
						GroupType groupType = new GroupType();
						groupType.setGroupGroupSeq(
								groupAllDao.findGroupAllByGroupSeq(resultGroupAll.getGroupSeq()).getGroupSeq());
//						System.out.println(groupAllRequest.getInterviewTypeList().get(j));

						groupType.setInterviewTypeTypeSeq(groupAllRequest.getInterviewTypeList().get(j).getTypeSeq());
						groupType.setSessionName(getUUID());

						groupTypeDao.save(groupType);
					}
				}
				status = HttpStatus.OK;
			} else {
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		} catch (RuntimeException e) {
			logger.error("그룹 만들기 실패", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return new ResponseEntity<>("각 그룹 등록 완료", status);
	}

	private static int calFullRoutine(String start, String end) {
		int count = 0;

		StringTokenizer st = new StringTokenizer(start, "/");
		int startMonth = Integer.parseInt(st.nextToken());
		int startDay = Integer.parseInt(st.nextToken());

		st = new StringTokenizer(end, "/");
		int endMonth = Integer.parseInt(st.nextToken());
		int endDay = Integer.parseInt(st.nextToken());

		// 면접이 모두 같은 달에 시행되는 경우
		if (startMonth == endMonth) {
			count = endDay - startDay + 1;
		} else { // 다른 달에 시행되는 경우
			count += (endDay + (totalDay(startMonth) - startDay) + 1);
		}

		return count;
	}

	// 다음날 찾아주는 Method
	private static String findNextDate(String now) {
		StringBuilder sb = new StringBuilder();

		StringTokenizer st = new StringTokenizer(now, "/");
		int month = Integer.parseInt(st.nextToken());
		int day = Integer.parseInt(st.nextToken());

		if (totalDay(month) == day) { // 마지막 날이면
			sb.append(month + 1);
			sb.append("/");
			sb.append(1);
		} else {
			sb.append(month);
			sb.append("/");
			sb.append(day + 1);
		}

		return sb.toString();
	}

	// 해당 달이 몇일인지 알려주는 Method
	private static int totalDay(int month) {
		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
			return 31;
		} else if (month == 2) { // 윤년은 없다고 가정
			return 28;
		} else {
			return 30;
		}
	}

	public String getUUID() {
		String uuid = UUID.randomUUID().toString().replace("-", "");
		return uuid;
	}
}
