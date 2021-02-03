package com.web.project.controller.group;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
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
import com.web.project.dao.group.DetailOrderDao;
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
import com.web.project.model.group.DetailOrder;
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
	
	@Autowired
	DetailOrderDao detailOrderDao;
	
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
				
				tmpApplicantGroupList=applicantGroupDao.findListApplicantGroupByGroupDetailDetailSeq(nowGroupDetail.getDetailSeq());
				
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
	
	@PostMapping("/divide/{recruitSeq}")
	@ApiOperation(value = "그룹 나누기")
	public Object divide(@Valid @RequestBody GroupAllRequest groupAllRequest, @PathVariable("recruitSeq") int recruitSeq) {
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
					
					//추가
					//table순서
					//groupall->groupType->groupDetail->detailOrder->typeInterviewer->applicantGroup
					//1. 세부그룹 나누기
					divideDetail(groupAllRequest.getDivideDetailNum(),resultGroupAll.getGroupSeq());
					//2. 세부 그룹 순서 부여
					allocateDetailOrder(resultGroupAll.getGroupSeq());
					//3.면접관 배치
					interviewerAssign(resultGroupAll.getGroupSeq());
					//4.지원자 배치
					applicantAssign(resultGroupAll.getGroupSeq());
					
					
					
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
		StringTokenizer st = new StringTokenizer(start, "-");
		int startYear = Integer.parseInt(st.nextToken());
		int startMonth = Integer.parseInt(st.nextToken());
		int startDay = Integer.parseInt(st.nextToken());
		st = new StringTokenizer(end, "-");
		int endYear = Integer.parseInt(st.nextToken());
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
		StringTokenizer st = new StringTokenizer(now, "-");
		int year = Integer.parseInt(st.nextToken());
		int month = Integer.parseInt(st.nextToken());
		int day = Integer.parseInt(st.nextToken());
		if (totalDay(month) == day) { // 마지막 날이면
			sb.append(year);
			sb.append("-");
			if(month < 10) {
				sb.append(0);
			}
			sb.append(month + 1);
			sb.append("-");
			sb.append(1);
		} else {
			sb.append(year);
			sb.append("-");
			if(month < 10) {
				sb.append(0);
			}
			sb.append(month);
			sb.append("-");
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
	
	
	//추가
	public void divideDetail(int divideNum,int groupSeq) {
		//HttpStatus status = null;
		GroupDetail groupDetail = null;
		
		//try {
			// 1. 각 그룹의 인원 수를 가져온다.
			int groupAllNum = groupAllDao.findGroupAllByGroupSeq(groupSeq).getGroupDivide();
			
			// 2. 각 세부 그룹으로 나눠준다. => 나머지 인원에 대해서 생각 X
			int detailGroupCount = groupAllNum / divideNum;
			
			// 3. 세부 그룹에 인원을 부여한다.
			for (int i = 0; i < detailGroupCount; i++) {
				groupDetail = new GroupDetail();
				
				groupDetail.setDetailDivide(divideNum);
				groupDetail.setGroupGroupSeq(groupSeq);
				
				groupDetailDao.save(groupDetail);
			}
			
			// 3. 나머지 인원은 따로 처리해야 된다.
			if(groupAllNum % divideNum != 0) {
				int remainderNum = groupAllNum - (detailGroupCount * divideNum);
				
				groupDetail = new GroupDetail();
				
				groupDetail.setDetailDivide(remainderNum);
				groupDetail.setGroupGroupSeq(groupSeq);
				
				groupDetailDao.save(groupDetail);
			}
			
			//status = HttpStatus.OK;
//		} catch (RuntimeException e) {
//			logger.error("세부 그룹 등록 실패", e);
//            status = HttpStatus.INTERNAL_SERVER_ERROR;
//		}
//		
		//return new ResponseEntity<> ("세부 그룹 등록 완료", status);
	}
	
//	@PostMapping("/allocate/detailOrder/{groupSeq}")
//	@ApiOperation(value = "세부 그룹에 순서 부여하기")
	public void allocateDetailOrder(int groupSeq) {
	//	HttpStatus status = null;
		
	//	try {
			// 1. 각 그룹의 면접 리스트, 세부 그룹 리스트를 구해준다.
			List<GroupDetail> groupDetailList = groupDetailDao.findListGroupDetailByGroupGroupSeq(groupSeq);
			List<GroupType> groupTypeList = groupTypeDao.findListGroupTypeByGroupGroupSeq(groupSeq);
			
			// 2. 면접 개수, 세부 그룹 개수를 구해준다.
			int groupDetailCount = groupDetailList.size();
			int interviewCount = groupTypeList.size();
			
			// 3. 순서를 부여하기 위한 Queue 생성
			Queue<Integer> sequenceQ = new LinkedList<Integer>();
			for (int i = 1; i <= interviewCount; i++) {
				sequenceQ.offer(i);
			}
			
			// 4. 세부 그룹의 순서를 부여한다.
			for (int i = 0; i < groupDetailCount; i++) {
				GroupDetail groupDetail = groupDetailList.get(i);
				
				// 5. 세부 그룹 순서 저장
				for (int j = 0; j < interviewCount; j++) {
					GroupType groupType = groupTypeList.get(j);
					
					DetailOrder detailOrder = new DetailOrder();
					
					// 외래키 설정
					detailOrder.setGroupDetailDetailSeq(groupDetail.getDetailSeq());
					detailOrder.setGroupTypeGroupTypeSeq(groupType.getGroupTypeSeq());
					detailOrder.setGroupTypeInterviewTypeTypeSeq(groupType.getInterviewTypeTypeSeq());
					
					// 순서 설정
					int order = sequenceQ.poll();
					detailOrder.setTrueOrder(order);
					sequenceQ.offer(order);
					
					detailOrderDao.save(detailOrder);
				}
				
				// 6. 순서 조정
				sequenceQ.offer(sequenceQ.poll());
			}
			
//			status = HttpStatus.OK;
//		} catch (RuntimeException e) {
//			logger.error("세부 그룹 순서 부여 실패", e);
//            status = HttpStatus.INTERNAL_SERVER_ERROR;
//		}
//		
//		return new ResponseEntity<> ("순서 부여 완료", status);
	}
	
	
	public void interviewerAssign(int groupSeq) {
//		HttpStatus status = null;
		List<Interviewer> interviewerList = new ArrayList<Interviewer>();
		List<GroupType> groupTypeList = new ArrayList<GroupType>();

//		try {

			GroupAll nowGroupAll = groupAllDao.findGroupAllByGroupSeq(groupSeq);

			// 1.group에 해당하는 회사번호,직군,부서 seq 찾기
			int comSeq = recruitDao.findRecruitByReSeq(nowGroupAll.getRecruitReSeq()).getCompanyComSeq();
			int caSeq = nowGroupAll.getCareerCaSeq();
			int partSeq = careerDao.findCareerByCaSeq(caSeq).getPartPartSeq();

			// 2. group안에 있는 그룹별 면접 종류 list만들기
			groupTypeList = groupTypeDao.findListGroupTypeByGroupGroupSeq(nowGroupAll.getGroupSeq());

			// 3. group과 같은 회사,공고,부서,직군&&면접일정 배정하지않은 interviewerList 만들기
			interviewerList = interviewerDao
					.findAllInterviewerByCompanyComSeqAndCareerCaSeqAndCareerPartPartSeqAndViewAssigned(comSeq, caSeq,
							partSeq, 0);

			// 그룹별 면접 종류 만큼 반복
			for (int i = 0; i < groupTypeList.size(); i++) {

				GroupType nowGroupType = groupTypeList.get(i);

				// 4.관리자 1명 배정하기
				for (int j = 0; j < interviewerList.size(); j++) {
					Interviewer nowInterviewer = interviewerList.get(j);
					// 관리자면
					if (nowInterviewer.getViewWait() == 0) {
						// typeInterviewer 설정
						TypeInterviewer typeInterviewer = new TypeInterviewer();
						typeInterviewer.setInterviewerViewSeq(nowInterviewer.getViewSeq());
						typeInterviewer.setGroupTypeGroupTypeSeq(nowGroupType.getGroupTypeSeq());
						typeInterviewer.setGroupTypeInterviewTypeTypeSeq(nowGroupType.getInterviewTypeTypeSeq());
						typeInterviewer.setInterviewerCareerCaSeq(caSeq);
						typeInterviewer.setInterviewerCareerPartPartSeq(partSeq);

						// interviewer viewAsssigned에 면접 배치했다고 update
						Optional<Interviewer> interviewer = interviewerDao
								.findOptionalInterviewerByViewSeq(nowInterviewer.getViewSeq());

						// UPDATE(U) -> SELECT(R) + INSERT(C)
						interviewer.ifPresent(selectInterviewer -> {
							selectInterviewer.setViewAssigned(1);
							// INSERT!
							interviewerDao.save(selectInterviewer);
						});

						// 관리자 insert
						typeInterviewerDao.save(typeInterviewer);

						// 면접 배정한 관리자 list에서 지우기
						interviewerList.remove(j);
						break;
					}
				}

				// 5.면접관 InterviewerDivide 수 만큼 배정하기
				int interviewerCnt = 0;
				for (int j = 0; j < interviewerList.size(); j++) {
					Interviewer nowInterviewer = interviewerList.get(j);

					// 면접 당 면접관 수 만큼 반복
					if (interviewerCnt == nowGroupAll.getInterviewerDivide()) {
						break;
					}

					// 면접관이면
					if (nowInterviewer.getViewWait() == 1) {
						// typeInterviewer 설정
						TypeInterviewer typeInterviewer = new TypeInterviewer();
						typeInterviewer.setInterviewerViewSeq(nowInterviewer.getViewSeq());
						typeInterviewer.setGroupTypeGroupTypeSeq(nowGroupType.getGroupTypeSeq());
						typeInterviewer.setGroupTypeInterviewTypeTypeSeq(nowGroupType.getInterviewTypeTypeSeq());
						typeInterviewer.setInterviewerCareerCaSeq(caSeq);
						typeInterviewer.setInterviewerCareerPartPartSeq(partSeq);

						// interviewer viewAsssigned에 면접 배치했다고 update
						Optional<Interviewer> interviewer = interviewerDao
								.findOptionalInterviewerByViewSeq(nowInterviewer.getViewSeq());

						// UPDATE(U) -> SELECT(R) + INSERT(C)
						interviewer.ifPresent(selectInterviewer -> {
							selectInterviewer.setViewAssigned(1);
							// INSERT!
							interviewerDao.save(selectInterviewer);
						});

						// 관리자 insert
						typeInterviewerDao.save(typeInterviewer);
						interviewerCnt++;

						// 면접 배정한 관리자 list에서 지우기
						interviewerList.remove(j);
						j--;
					}

				}

			}
//			status = HttpStatus.OK;
//		} catch (RuntimeException e) {
//			logger.error("면접관 자동 배정 실패", e);
//			status = HttpStatus.INTERNAL_SERVER_ERROR;
//		}
//		return new ResponseEntity<>("면접관 자동 배정 완료", status);
	}
	
	
	public void applicantAssign(int groupSeq) {
//		HttpStatus status = null;
		List<Applicant> applicantList = new ArrayList<Applicant>();
		List<GroupDetail> groupDetailList = new ArrayList<GroupDetail>();

//		try {

			GroupAll nowGroupAll = groupAllDao.findGroupAllByGroupSeq(groupSeq);

			// 1.group에 해당하는 공고,직군 seq 찾기
			int reSeq = nowGroupAll.getRecruitReSeq();
			int caSeq = nowGroupAll.getCareerCaSeq();

			// 2. group안에 있는 그룹별 세부그룹 list만들기
			groupDetailList = groupDetailDao.findAllGroupDetailByGroupGroupSeq(nowGroupAll.getGroupSeq());

			// 3. group과 같은 공고,직군&&면접일정 배정하지않은 applicantList 만들기
			applicantList = applicantDao.findApplicantByRecruitReSeqAndCareerCaSeqAndApplyAssigned(reSeq, caSeq, 0);

			// 그룹별 면접 종류 만큼 반복
			for (int i = 0; i < groupDetailList.size(); i++) {

				GroupDetail nowGroupDetail = groupDetailList.get(i);

				// 5.면접관 InterviewerDivide 수 만큼 배정하기
				int applicantCnt = 0;
				for (int j = 0; j < applicantList.size(); j++) {
					Applicant nowApplicant = applicantList.get(j);

					// 면접 당 면접관 수 만큼 반복
					if (applicantCnt == nowGroupDetail.getDetailDivide()) {
						break;
					}
					
					if(nowApplicant.getApplyAssigned()==1) {
						//이미 배정받은 지원자이면 continue
						continue;
					}
					
					// applicantGroup 설정
					ApplicantGroup applicantGroup = new ApplicantGroup();
					applicantGroup.setApplicantApplySeq(nowApplicant.getApplySeq());
					applicantGroup.setApplicantCareerCaSeq(caSeq);
					applicantGroup.setApplicantRecruitReSeq(reSeq);
					applicantGroup.setGroupDetailDetailSeq(nowGroupDetail.getDetailSeq());
					applicantGroup.setGroupDetailGroupGroupSeq(nowGroupDetail.getGroupGroupSeq());

					// applicant applyAsssigned에 면접 배치했다고 update
					Optional<Applicant> applicant = applicantDao.findOptionalApplicantByApplySeq(nowApplicant.getApplySeq());

					// UPDATE(U) -> SELECT(R) + INSERT(C)
					applicant.ifPresent(selectApplicant -> {
						selectApplicant.setApplyAssigned(1);
						// INSERT!
						applicantDao.save(selectApplicant);
					});

					//지원자 insert
					applicantGroupDao.save(applicantGroup);
					applicantCnt++;

				}

			}
//			status = HttpStatus.OK;
//		} catch (RuntimeException e) {
//			logger.error("지원자 자동 배정 실패", e);
//			status = HttpStatus.INTERNAL_SERVER_ERROR;
//		}
//		return new ResponseEntity<>("지원자 자동 배정 완료", status);
	}
}
