package com.web.project.controller.recruit;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.servlet.MultipartConfigElement;
import javax.validation.Valid;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import com.web.project.controller.hr.HrController;
import com.web.project.dao.group.DetailOrderDao;
import com.web.project.dao.group.GroupAllDao;
import com.web.project.dao.group.GroupDetailDao;
import com.web.project.dao.group.GroupTypeDao;
import com.web.project.dao.hr.CareerDao;
import com.web.project.dao.hr.CompanyDao;
import com.web.project.dao.hr.PartDao;
import com.web.project.dao.interview.InterviewTypeDao;
import com.web.project.dao.recruit.ApplicantDao;
import com.web.project.dao.recruit.ApplicantGroupDao;
import com.web.project.dao.recruit.CertificateDao;
import com.web.project.dao.recruit.RecruitDao;
import com.web.project.model.BasicResponse;
import com.web.project.model.group.DetailOrder;
import com.web.project.model.group.GroupAll;
import com.web.project.model.group.GroupAllRequest;
import com.web.project.model.group.GroupDetail;
import com.web.project.model.group.GroupType;
import com.web.project.model.hr.Career;
import com.web.project.model.hr.Company;
import com.web.project.model.hr.Part;
import com.web.project.model.interview.Interview;
import com.web.project.model.interview.InterviewType;
import com.web.project.model.interview.Interviewer;
import com.web.project.model.interview.TypeInterviewer;
import com.web.project.model.recruit.Applicant;
import com.web.project.model.recruit.ApplicantGroup;
import com.web.project.model.recruit.Certificate;
import com.web.project.model.recruit.Recruit;
import com.web.project.service.hr.EmailService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
		@ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
		@ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
		@ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })
@CrossOrigin(origins = { "http://localhost:3000" })
@RestController
@RequestMapping("/applicant")
public class ApplicantController {
	@Autowired
	ApplicantDao applicantDao;

	@Autowired
	EmailService emailService;

	@Autowired
	RecruitDao recruitDao;

	@Autowired
	PartDao partDao;

	@Autowired
	CareerDao careerDao;

	@Autowired
	CompanyDao companyDao;

	@Autowired
	GroupAllDao groupAllDao;

	@Autowired
	GroupDetailDao groupDetailDao;

	@Autowired
	GroupTypeDao groupTypeDao;
	@Autowired
	ApplicantGroupDao applicantGroupDao;
	
	@Autowired
	DetailOrderDao detailOrderDao;
	
	@Autowired
	CertificateDao certificateDao;
	
	@Autowired
	InterviewTypeDao interviewTypeDao;

	public static final Logger logger = LoggerFactory.getLogger(HrController.class);

	@GetMapping(value = "/getList/{reSeq}")
	@ApiOperation(value = "공고에 따른 지원자 리스트 모두 가져오기")
	public ResponseEntity<List<Map<String, Object>>> getApplicationList(@PathVariable("reSeq") int reSeq) {
		List<Map<String, Object>> applicantList = new ArrayList<Map<String,Object>>();
		HttpStatus status = null;
		
		try {
			List<Applicant> applicantTempList = applicantDao.findAllApplicantByRecruitReSeq(reSeq);
			
			for (int i = 0; i < applicantTempList.size(); i++) {
				Applicant applicant = applicantTempList.get(i);
				
				Map<String, Object> resultMap = new HashMap<String, Object>();
				
				// 지원자 seq
				resultMap.put("apply-Seq", applicant.getApplySeq());
				// 지원자 session Id
				resultMap.put("apply-Id", applicant.getApplyId());
				// 지원자 이름
				resultMap.put("apply-Name", applicant.getApplyName());
				// 지원자 email
				resultMap.put("apply-Email", applicant.getApplyEmail());
				// 지원자 생년월일
				resultMap.put("apply-Birth", applicant.getApplyBirth());
				// 지원자 핸드폰 번호
				resultMap.put("apply-Phone", applicant.getApplyPhone());
				// 지원자 대학교
				resultMap.put("apply-University", applicant.getApplyUniversity());
				// 지원자 전공
				resultMap.put("apply-Major", applicant.getApplyMajor());
				// 지원자 학점
				resultMap.put("apply-Grade", applicant.getApplyGrade());
				// 지원자 자기소개서 항목 1
				resultMap.put("apply-Resume1", applicant.getApplyResume1());
				// 지원자 자기소개서 항목 2
				resultMap.put("apply-Resume2", applicant.getApplyResume2());
				// 지원자 자기소개서 항목 3
				resultMap.put("apply-Resume3", applicant.getApplyResume3());
				// 지원자 자기소개서 항목 4
				resultMap.put("apply-Resume4", applicant.getApplyResume4());
				// 지원자가 할당되었는지
				resultMap.put("apply-Assigned", applicant.getApplyAssigned());
				// 지원자 직무 이름
				resultMap.put("apply-Career-Name", careerDao.findCareerByCaSeq(applicant.getCareerCaSeq()).getCaName());
				// 지원자 공고 seq
				resultMap.put("apply-Recruit-Seq", applicant.getRecruitReSeq());
				
				applicantList.add(resultMap);
			}
			status = HttpStatus.OK;
		} catch (RuntimeException e) {
			logger.error("공고의 지원자 리스트 가져오기 실패", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		return new ResponseEntity<List<Map<String, Object>>>(applicantList, status);
	}

	@GetMapping(value = "/getListByCompany/{comSeq}")
	@ApiOperation(value = "회사에 따른 지원자 리스트 모두 가져오기")
	public ResponseEntity<List<Map<String, Object>>> getApplicationListByCompany(@PathVariable("comSeq") int comSeq) {
		HttpStatus status = null;
		List<Map<String, Object>> applicantList = new ArrayList<Map<String,Object>>();
		
		try {
			// 모든 지원자 목록
			List<Applicant> allApplicantList = applicantDao.findAll();
			
			for (int i = 0; i < allApplicantList.size(); i++) {
				Applicant applicant = allApplicantList.get(i);
				
				if (comSeq == recruitDao.findRecruitByReSeq(applicant.getRecruitReSeq()).getCompanyComSeq()) {
					Map<String, Object> resultMap = new HashMap<String, Object>();
					
					// 지원자 seq
					resultMap.put("apply-Seq", applicant.getApplySeq());
					// 지원자 session Id
					resultMap.put("apply-Id", applicant.getApplyId());
					// 지원자 이름
					resultMap.put("apply-Name", applicant.getApplyName());
					// 지원자 email
					resultMap.put("apply-Email", applicant.getApplyEmail());
					// 지원자 생년월일
					resultMap.put("apply-Birth", applicant.getApplyBirth());
					// 지원자 핸드폰 번호
					resultMap.put("apply-Phone", applicant.getApplyPhone());
					// 지원자 대학교
					resultMap.put("apply-University", applicant.getApplyUniversity());
					// 지원자 전공
					resultMap.put("apply-Major", applicant.getApplyMajor());
					// 지원자 학점
					resultMap.put("apply-Grade", applicant.getApplyGrade());
					// 지원자 자기소개서 항목 1
					resultMap.put("apply-Resume1", applicant.getApplyResume1());
					// 지원자 자기소개서 항목 2
					resultMap.put("apply-Resume2", applicant.getApplyResume2());
					// 지원자 자기소개서 항목 3
					resultMap.put("apply-Resume3", applicant.getApplyResume3());
					// 지원자 자기소개서 항목 4
					resultMap.put("apply-Resume4", applicant.getApplyResume4());
					// 지원자가 할당되었는지
					resultMap.put("apply-Assigned", applicant.getApplyAssigned());
					// 지원자 직무 이름
					resultMap.put("apply-Career-Name", careerDao.findCareerByCaSeq(applicant.getCareerCaSeq()).getCaName());
					// 지원자 공고 seq
					resultMap.put("apply-Recruit-Seq", applicant.getRecruitReSeq());
					
					applicantList.add(resultMap);
				}
			}
			
			status = HttpStatus.OK;
		} catch (RuntimeException e) {
			logger.error("공고리스트 가져오기 실패", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		return new ResponseEntity<List<Map<String, Object>>>(applicantList, status);
	}
	
	@GetMapping(value = "/getListBySessionName/{interviewSessionName}")
	@ApiOperation(value = "세션  이름에 따른 지원자 리스트 모두 가져오기")
	public ResponseEntity<List<Map<String, Object>>> getApplicationListBySessionName(@PathVariable("interviewSessionName") String interviewSessionName) {
		HttpStatus status = null;
		List<Map<String, Object>> applicantList = new ArrayList<Map<String,Object>>();
		
		
		try {
		
			GroupType groupType=groupTypeDao.findGroupTypeByInterviewSessionName(interviewSessionName);
			List<GroupDetail> groupDetailList=groupDetailDao.findAllGroupDetailByGroupGroupSeq(groupType.getGroupGroupSeq());
			List<ApplicantGroup> applicantGroupList=new ArrayList<ApplicantGroup>();
			List<Applicant> allApplicantList=new ArrayList<Applicant>();
			
			for (int i = 0; i < groupDetailList.size(); i++) {
				applicantGroupList=applicantGroupDao.findListApplicantGroupByGroupDetailDetailSeq(groupDetailList.get(i).getDetailSeq());
				for (int j = 0; j < applicantGroupList.size(); j++) {
					allApplicantList.add(applicantDao.findApplicantByApplySeq(applicantGroupList.get(j).getApplicantApplySeq()));
				}
			}
			
			for (int i = 0; i < allApplicantList.size(); i++) {
				Applicant applicant = allApplicantList.get(i);
				

					Map<String, Object> resultMap = new HashMap<String, Object>();
					
					// 지원자 seq
					resultMap.put("apply-Seq", applicant.getApplySeq());
					// 지원자 session Id
					resultMap.put("apply-Id", applicant.getApplyId());
					// 지원자 이름
					resultMap.put("apply-Name", applicant.getApplyName());
					// 지원자 email
					resultMap.put("apply-Email", applicant.getApplyEmail());
					// 지원자 생년월일
					resultMap.put("apply-Birth", applicant.getApplyBirth());
					// 지원자 핸드폰 번호
					resultMap.put("apply-Phone", applicant.getApplyPhone());
					// 지원자 대학교
					resultMap.put("apply-University", applicant.getApplyUniversity());
					// 지원자 전공
					resultMap.put("apply-Major", applicant.getApplyMajor());
					// 지원자 학점
					resultMap.put("apply-Grade", applicant.getApplyGrade());
					// 지원자 자기소개서 항목 1
					resultMap.put("apply-Resume1", applicant.getApplyResume1());
					// 지원자 자기소개서 항목 2
					resultMap.put("apply-Resume2", applicant.getApplyResume2());
					// 지원자 자기소개서 항목 3
					resultMap.put("apply-Resume3", applicant.getApplyResume3());
					// 지원자 자기소개서 항목 4
					resultMap.put("apply-Resume4", applicant.getApplyResume4());
					// 지원자가 할당되었는지
					resultMap.put("apply-Assigned", applicant.getApplyAssigned());
					// 지원자 직무 이름
					resultMap.put("apply-Career-Name", careerDao.findCareerByCaSeq(applicant.getCareerCaSeq()).getCaName());
					// 지원자 공고 seq
					resultMap.put("apply-Recruit-Seq", applicant.getRecruitReSeq());
					
					applicantList.add(resultMap);
			
			}
			
			status = HttpStatus.OK;
		} catch (RuntimeException e) {
			logger.error("공고리스트 가져오기 실패", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		return new ResponseEntity<List<Map<String, Object>>>(applicantList, status);
	}

//	@PostMapping("/assign/{groupSeq}")
//	@ApiOperation(value = "지원자 자동 배정")
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

				if (nowApplicant.getApplyAssigned() == 1) {
					// 이미 배정받은 지원자이면 continue
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
				Optional<Applicant> applicant = applicantDao
						.findOptionalApplicantByApplySeq(nowApplicant.getApplySeq());

				// UPDATE(U) -> SELECT(R) + INSERT(C)
				applicant.ifPresent(selectApplicant -> {
					selectApplicant.setApplyAssigned(1);
					// INSERT!
					applicantDao.save(selectApplicant);
				});

				// 지원자 insert
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

	@GetMapping("/mypage/{Id}")
	@ApiOperation(value = "지원자마이페이지")
	public Object applicantMypage(@PathVariable("Id") String applyId) throws MessagingException {

		final BasicResponse result = new BasicResponse();
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;
		
		System.out.println(applyId);

		Applicant applicant = applicantDao.findApplicantByapplyId(applyId); // 지원자 정보
		if(applicant != null) {
			int applySeq = applicant.getApplySeq();
			logger.info("지원번호 " + applySeq + " 번, " + applicant.getApplyName() + " 접속");
			
			List<Certificate> certificates = certificateDao.findListCertificateByApplicantApplySeq(applySeq); 	// 자격증 검색
			
			ApplicantGroup applicantGroup = applicantGroupDao.findApplicantGroupByApplicantApplySeq(applySeq); 	// 지원자가 속한 그룹
			if(applicantGroup != null) {
				
				Recruit recruit = recruitDao.findRecruitByReSeq(applicantGroup.getApplicantRecruitReSeq()); 	// 공고 정보
				Company company = companyDao.findCompanyByComSeq(recruit.getCompanyComSeq()); 					// 회사 정보
				Career career = careerDao.findCareerByCaSeq(applicantGroup.getApplicantCareerCaSeq()); 			// 직군 정보
				Part part = partDao.findPartByPartSeq(career.getPartPartSeq());									// 부서 정보
				
				int groupSeq = applicantGroup.getGroupDetailGroupGroupSeq(); 									// 그룹 대 분류(동일한 시간에 보는 사람들)
				int detailGroupSeq = applicantGroup.getGroupDetailDetailSeq(); 									// 시간 같은데 면접 순서도 같은 애들
				
				List<DetailOrder> detailOrders = detailOrderDao.findListDetailOrderByGroupDetailDetailSeq(detailGroupSeq);
				Collections.sort(detailOrders, new Comparator<DetailOrder>() { 									// 면접 순서(pt, 인성 ..) 로 정렬
					@Override
					public int compare(DetailOrder o1, DetailOrder o2) {
						return o1.getTrueOrder() - o2.getTrueOrder();
					}
				});
				
				List<Interview> interviews = new ArrayList<Interview>();
				for (DetailOrder detailOrder : detailOrders) {
					// 면접에 대한 모든 정보들
					Interview interview = new Interview();
					// 세션 정보 담긴 객체
					GroupType groupType = groupTypeDao.findGroupTypeByGroupTypeSeq(detailOrder.getGroupTypeGroupTypeSeq());
					GroupAll groupAll = groupAllDao.findGroupAllByGroupSeq(groupType.getGroupGroupSeq());
					InterviewType interviewType = interviewTypeDao.findInterviewTypeByTypeSeq(groupType.getInterviewTypeTypeSeq());

					interview.setGroupDate(groupAll.getGroupDate());
					interview.setGroupStartTime(groupAll.getGroupStartTime());
					interview.setCareerName(career.getCaName());
					interview.setPartName(part.getPartName());
					interview.setInterviewType(interviewType.getTypeName());
					interview.setWaitSessionName(groupType.getWaitSessionName());
					interview.setInterviewSessionName(groupType.getInterviewSessionName());
					
					interviews.add(interview);
				}
				
				resultMap.put("company", company);				// 지원자가 지원한 회사 정보
				resultMap.put("recruit", recruit);				// 지원자가 지원한 공고 정보
				resultMap.put("user", applicant); 				// 지원자 정보
				resultMap.put("userCertificate", certificates); // 지원자 자격증, 없으면 null
				resultMap.put("interviews", interviews); 		// 지원자 면접 정보

				result.status = true;
				result.data = "success";
				result.object = resultMap;
				status = HttpStatus.OK;
			}else {
				logger.error("지원자가 배정된 그룹이 없습니다.");
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		}else {
			logger.error("잘못된 지원자 아이디 입니다.");
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<>(resultMap, status);
	};

	@PostMapping("/register/{reSeq}")
	@ApiOperation(value = "지원자등록")
	public ResponseEntity<List<Applicant>> applicantRegister(@PathVariable("reSeq") int reSeq, MultipartFile files)
			throws EncryptedDocumentException, IOException, ParseException {
		HttpStatus status = HttpStatus.ACCEPTED;

		// 웹상에서 업로드 되어 MultipartFile인 경우 바로 InputStream으로 변경하여 사용.
		InputStream inputStream = new ByteArrayInputStream(files.getBytes());

		// String filePath = "C:\\example.xlsx"; // xlsx 형식
		// String filePath = "D:\\applicant.xls"; // xls 형식

		// InputStream inputStream = new FileInputStream(filePath);

		// 엑셀 로드
		Workbook workbook = WorkbookFactory.create(inputStream);

		List<Applicant> applicantList = new ArrayList<Applicant>();

		// 시트 로드 0, 첫번째 시트 로드
		Sheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowItr = sheet.iterator();

		try {
			// 행만큼 반복
			
			Loop1:while (rowItr.hasNext()) {
				Applicant applicant = new Applicant();
				Row row = rowItr.next();
				String partName = "";
				String careerName = "";

				// 첫번재 행이 해더인 경우 스킵, 2번째 행부터 data 로드
				if (row.getRowNum() == 0) {
					continue;
				}

				Iterator<Cell> cellItr = row.cellIterator();

				// 한행이 한열씩 읽기 (셀 읽기)
				while (cellItr.hasNext()) {
					Cell cell = cellItr.next();
					int index = cell.getColumnIndex();

					switch (index) {
					case 0:
						// 인덱스
						// applicant.setApplySeq(((Double) getValueFromCell(cell)).intValue());
						// 셀이 숫자형인 경우 Double형으로 변환 후 int형으로 변환
						break;
					case 1:
						// 성명
						applicant.setApplyName((String) getValueFromCell(cell));
						break;
					case 2:
						// 이메일
						String email = (String) getValueFromCell(cell);
						Optional<Applicant> applicantEmailOpt = applicantDao.findOptionalApplicantByApplyEmail(email);
						if (applicantEmailOpt.isPresent()) {
							System.out.println("이메일 중복 되었음");
							continue Loop1;
						}
						else {
						applicant.setApplyEmail((String) getValueFromCell(cell));
						}
						break;
					case 3:
						// 부서
						partName = (String) getValueFromCell(cell);
						break;
					case 4:
						// 직군
						careerName = (String) getValueFromCell(cell);
						break;
					case 5:
						// 생년월일
						if (cell.getCellType() == CellType.NUMERIC) {
							DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
							Date date = cell.getDateCellValue();
							String tmp = dateFormat.format(date);
							applicant.setApplyBirth(tmp);
						} else {
							applicant.setApplyBirth((String) getValueFromCell(cell));
						}

						break;
					case 6:
						// 핸드폰
						applicant.setApplyPhone((String) getValueFromCell(cell));
						break;
					case 7:
						// 대학교
						applicant.setApplyUniversity((String) getValueFromCell(cell));
						break;
					case 8:
						// 전공
						applicant.setApplyMajor((String) getValueFromCell(cell));
						break;
					case 9:
						// 학점
						applicant.setApplyGrade((Double) getValueFromCell(cell));
						break;
					case 10:
						// 자소서
						applicant.setApplyResume1((String) getValueFromCell(cell));
						break;
					case 11:
						applicant.setApplyResume2((String) getValueFromCell(cell));
						break;
					case 12:
						applicant.setApplyResume3((String) getValueFromCell(cell));
						break;
					case 13:
						applicant.setApplyResume4((String) getValueFromCell(cell));
						break;
					}
				}

				// 랜덤아이디부여
				applicant.setApplyId(getUUID());

				Recruit recruit = recruitDao.findRecruitByReSeq(reSeq);

				int comSeq = recruit.getCompanyComSeq();
				Part part = partDao.findPartByCompanyComSeqAndPartName(comSeq, partName);

				int partSeq = part.getPartSeq();
				Career career = careerDao.findCareerByCaNameAndPartPartSeq(careerName, partSeq);
				applicant.setCareerCaSeq(career.getCaSeq());
				applicant.setRecruitReSeq(recruit.getReSeq());

				// 아직 면접 일정 배정 안함
				applicant.setApplyAssigned(0);

				// insert문
				//applicantDao.save(applicant);

				applicantList.add(applicant);
			}
		
		for (int i = 0; i < applicantList.size(); i++) {
			applicantDao.save(applicantList.get(i));
		}
		
		status = HttpStatus.OK;

		} catch (Exception e) {
			logger.error("지원자 등록 실패 : {}", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			applicantList=null;
		}

		return new ResponseEntity<List<Applicant>>(applicantList,status);
	}

	// 셀서식에 맞게 값 읽기
	public Object getValueFromCell(Cell cell) {
		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue();
		case BOOLEAN:
			return cell.getBooleanCellValue();
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				return cell.getDateCellValue();
			}
			return cell.getNumericCellValue();
		case FORMULA:
			return cell.getCellFormula();
		case BLANK:
			return "";
		default:
			return "";
		}
	}

	public String getUUID() {
		String uuid = UUID.randomUUID().toString().replace("-", "");
		return uuid;
	}
	
	
	@PostMapping("/send/{reSeq}")
	@ApiOperation(value = "인증 메일 보내기")
	public Object sendMail(@PathVariable("reSeq") int reSeq) throws MessagingException {
		
		HttpStatus status = null;
		Map<String, Object> resultMap = new HashMap<>();
		
		try {
			List<Applicant> applicantList = new ArrayList<Applicant>();
			applicantList=applicantDao.findAllApplicantByRecruitReSeq(reSeq);
			
			Recruit recruit = recruitDao.findRecruitByReSeq(reSeq);
			Company company = companyDao.findCompanyByComSeq(recruit.getCompanyComSeq());
			
			for (int i = 0; i < applicantList.size(); i++) {
				Applicant applicant=applicantList.get(i);
				ApplicantGroup applicantGroup=applicantGroupDao.findApplicantGroupByApplicantApplySeq(applicant.getApplySeq());
				int groupSeq=groupDetailDao.findGroupDetailByDetailSeq(applicantGroup.getGroupDetailDetailSeq()).getGroupGroupSeq();
				GroupAll groupAll=groupAllDao.findGroupAllByGroupSeq(groupSeq);
				
				StringBuffer emailContent = new StringBuffer();
				emailContent.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional //EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
				emailContent.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">");
				emailContent.append("<head>");
				emailContent.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n" + 
						"  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n" + 
						"  <meta name=\"x-apple-disable-message-reformatting\">\r\n" + 
						"<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\r\n" + 
						"  <title></title>\r\n" + 
						"  \r\n" + 
						"    <style type=\"text/css\">\r\n" + 
						"      a { color: #5f5f8d; text-decoration: none; }\r\n" + 
						"@media only screen and (min-width: 620px) {\r\n" + 
						"  .u-row {\r\n" + 
						"    width: 600px !important;\r\n" + 
						"  }\r\n" + 
						"  .u-row .u-col {\r\n" + 
						"    vertical-align: top;\r\n" + 
						"  }\r\n" + 
						"\r\n" + 
						"  .u-row .u-col-100 {\r\n" + 
						"    width: 600px !important;\r\n" + 
						"  }\r\n" + 
						"\r\n" + 
						"}\r\n" + 
						"\r\n" + 
						"@media (max-width: 620px) {\r\n" + 
						"  .u-row-container {\r\n" + 
						"    max-width: 100% !important;\r\n" + 
						"    padding-left: 0px !important;\r\n" + 
						"    padding-right: 0px !important;\r\n" + 
						"  }\r\n" + 
						"  .u-row .u-col {\r\n" + 
						"    min-width: 320px !important;\r\n" + 
						"    max-width: 100% !important;\r\n" + 
						"    display: block !important;\r\n" + 
						"  }\r\n" + 
						"  .u-row {\r\n" + 
						"    width: calc(100% - 40px) !important;\r\n" + 
						"  }\r\n" + 
						"  .u-col {\r\n" + 
						"    width: 100% !important;\r\n" + 
						"  }\r\n" + 
						"  .u-col > div {\r\n" + 
						"    margin: 0 auto;\r\n" + 
						"  }\r\n" + 
						"}\r\n" + 
						"body {\r\n" + 
						"  margin: 0;\r\n" + 
						"  padding: 0;\r\n" + 
						"}\r\n" + 
						"\r\n" + 
						"table,\r\n" + 
						"tr,\r\n" + 
						"td {\r\n" + 
						"  vertical-align: top;\r\n" + 
						"  border-collapse: collapse;\r\n" + 
						"}\r\n" + 
						"\r\n" + 
						"p {\r\n" + 
						"  margin: 0;\r\n" + 
						"}\r\n" + 
						"\r\n" + 
						".ie-container table,\r\n" + 
						".mso-container table {\r\n" + 
						"  table-layout: fixed;\r\n" + 
						"}\r\n" + 
						"\r\n" + 
						"* {\r\n" + 
						"  line-height: inherit;\r\n" + 
						"}\r\n" + 
						"\r\n" + 
						"a[x-apple-data-detectors='true'] {\r\n" + 
						"  color: inherit !important;\r\n" + 
						"  text-decoration: none !important;\r\n" + 
						"}\r\n" + 
						"\r\n" + 
						"</style>\r\n" + 
						"  \r\n" + 
						"  \r\n" + 
						"\r\n" + 
						"<link href=\"https://fonts.googleapis.com/css?family=Open+Sans:400,700\" rel=\"stylesheet\" type=\"text/css\">\r\n");
				emailContent.append("</head>");
				emailContent.append("<body class=\"clean-body\" style=\"margin: 0;padding: 0;-webkit-text-size-adjust: 100%;background-color: #fbfbfb\">");
				emailContent.append("  <table style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;min-width: 320px;Margin: 0 auto;background-color: #fbfbfb;width:100%\" cellpadding=\"0\" cellspacing=\"0\">\r\n" + 
						"  <tbody>\r\n" + 
						"  <tr style=\"vertical-align: top\">\r\n" + 
						"    <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top\">\r\n" + 
						"    <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td align=\"center\" style=\"background-color: #fbfbfb;\"><![endif]-->\r\n" + 
						"    \r\n" + 
						"\r\n" + 
						"<div class=\"u-row-container\" style=\"padding: 0px;background-color: #ffffff\">\r\n" + 
						"  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #ffffff;\">\r\n" + 
						"    <div style=\"border-collapse: collapse;display: table;width: 100%;background-image: url('https://ifh.cc/g/O3IRC5.jpg');background-repeat: no-repeat;background-position: center top;background-color: transparent;\">\r\n" + 
						"      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: #ffffff;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-image: url('images/image-2.png');background-repeat: no-repeat;background-position: center top;background-color: #ffffff;\"><![endif]-->\r\n" + 
						"      \r\n" + 
						"<!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\" valign=\"top\"><![endif]-->\r\n" + 
						"<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\r\n" + 
						"  <div style=\"width: 100% !important;\">\r\n" + 
						"  <!--[if (!mso)&(!IE)]><!--><div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\"><!--<![endif]-->\r\n" + 
						"  \r\n" + 
						"<table style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n" + 
						"  <tbody>\r\n" + 
						"    <tr>\r\n" + 
						"      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:11px;font-family:'Open Sans',sans-serif;\" align=\"left\">\r\n" + 
						"        \r\n" + 
						"<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\r\n" + 
						"  <tr>\r\n" + 
						"    <td style=\"padding-right: 0px;padding-left: 0px;\" align=\"center\">\r\n" + 
						"      \r\n" + 
						"      <img align=\"center\" border=\"0\" src=\"https://ifh.cc/g/VvzR6M.png\" alt=\"Image\" title=\"Image\" style=\"outline: none;text-decoration: none;-ms-interpolation-mode: bicubic;clear: both;display: inline-block !important;border: none;height: auto;float: none;width: 100%;max-width: 50px;\" width=\"50\"/>\r\n" + 
						"      \r\n" + 
						"    </td>\r\n" + 
						"  </tr>\r\n" + 
						"</table>\r\n" + 
						"\r\n" + 
						"      </td>\r\n" + 
						"    </tr>\r\n" + 
						"  </tbody>\r\n" + 
						"</table>\r\n" + 
						"\r\n" + 
						"<table style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n" + 
						"  <tbody>\r\n" + 
						"    <tr>\r\n" + 
						"      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:20px 10px 50px;font-family:'Open Sans',sans-serif;\" align=\"left\">\r\n" + 
						"        \r\n" + 
						"  <div style=\"color: #425b8d; line-height: 130%; text-align: center; word-wrap: break-word;\">\r\n" + 
						"    <p style=\"font-size: 14px; line-height: 130%; text-align: center;\">&nbsp;</p>\r\n" + 
						"<p style=\"font-size: 14px; line-height: 130%; text-align: center;\"><span style=\"font-size: 28px; line-height: 36.4px; color: #466b8c;\"><strong><span style=\"font-size: 18px; line-height: 23.4px; font-family: 'Open Sans', sans-serif;\">V<span style=\"font-size: 18px; line-height: 23.4px;\">iew Everywhere </span></span></strong></span></p>\r\n" + 
						"<p style=\"font-size: 14px; line-height: 130%; text-align: center;\"><span style=\"font-size: 44px; line-height: 57.2px; font-family: 'Open Sans', sans-serif; color: #466b8c;\"><strong>WieV</strong></span></p>\r\n" + 
						"<p style=\"font-size: 14px; line-height: 130%; text-align: center;\">&nbsp;</p>\r\n" + 
						"  </div>\r\n" + 
						"\r\n" + 
						"      </td>\r\n" + 
						"    </tr>\r\n" + 
						"  </tbody>\r\n" + 
						"</table>\r\n" + 
						"\r\n" + 
						"  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\r\n" + 
						"  </div>\r\n" + 
						"</div>\r\n" + 
						"<!--[if (mso)|(IE)]></td><![endif]-->\r\n" + 
						"      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\r\n" + 
						"    </div>\r\n" + 
						"  </div>\r\n" + 
						"</div>\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"<div class=\"u-row-container\" style=\"padding: 0px;background-color: #ffffff\">\r\n" + 
						"  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #ffffff;\">\r\n" + 
						"    <div style=\"border-collapse: collapse;display: table;width: 100%;background-color: transparent;\">\r\n" + 
						"      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: #ffffff;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-color: #ffffff;\"><![endif]-->\r\n" + 
						"      \r\n" + 
						"<!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"background-color: #ffffff;width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\" valign=\"top\"><![endif]-->\r\n" + 
						"<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\r\n" + 
						"  <div style=\"background-color: #ffffff;width: 100% !important;\">\r\n" + 
						"  <!--[if (!mso)&(!IE)]><!--><div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\"><!--<![endif]-->\r\n" + 
						"  \r\n" + 
						"<table style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n" + 
						"  <tbody>\r\n" + 
						"    <tr>\r\n" + 
						"      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:'Open Sans',sans-serif;\" align=\"left\">\r\n" + 
						"        \r\n" + 
						"  <table height=\"0px\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;border-top: 1px solid #BBBBBB;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\r\n" + 
						"    <tbody>\r\n" + 
						"      <tr style=\"vertical-align: top\">\r\n" + 
						"        <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;font-size: 0px;line-height: 0px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\r\n" + 
						"          <span>&#160;</span>\r\n" + 
						"        </td>\r\n" + 
						"      </tr>\r\n" + 
						"    </tbody>\r\n" + 
						"  </table>\r\n" + 
						"\r\n" + 
						"      </td>\r\n" + 
						"    </tr>\r\n" + 
						"  </tbody>\r\n" + 
						"</table>\r\n" + 
						"\r\n" + 
						"<table style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n" + 
						"  <tbody>\r\n" + 
						"    <tr>\r\n" + 
						"      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:35px 44px 10px;font-family:'Open Sans',sans-serif;\" align=\"left\">\r\n" + 
						"        \r\n" + 
						"  <div style=\"color: #34495e; line-height: 140%; text-align: left; word-wrap: break-word;\">\r\n" + 
						"    <p style=\"font-size: 14px; line-height: 140%;\"><span style=\"color: #4d6381; font-size: 14px; line-height: 19.6px;\"><strong><span style=\"font-size: 22px; line-height: 30.8px;\"><span style=\"color: #466b8c; font-size: 22px; line-height: 30.8px;\">"
						+company.getComName()+"</span><br /><span style=\"color: #466b8c; font-size: 22px; line-height: 30.8px;\">"
						+recruit.getReYear() +" "+recruit.getReFlag() +" "+ recruit.getReStatus() +" 채용"
						+"</span><br /></span><span style=\"font-size: 22px; line-height: 30.8px; color: #3b7df0;\">면접일정</span>"
						+ "<span style=\"font-size: 22px; line-height: 30.8px; color: #466b8c;\"> 안내입니다.</span><br /><br />"
						+ "<span style=\"color: #363d5a; font-size: 14px; line-height: 26.6px;\">면접 일시 : "+groupAll.getGroupDate()+"</span><br />"
						+ "<span style=\"color: #363d5a; font-size: 14px; line-height: 26.6px;\">면접 시작 시간 : "+groupAll.getGroupStartTime()+"시"+"</span><br />"	
						+ "<span style=\"color: #363d5a; font-size: 14px; line-height: 26.6px;\">면접 장소 : 자택 (독립된 장소)</span><br />"	
						+ "</strong></span></p>\r\n" + 
						"  </div>\r\n" + 
						"\r\n" + 
						"      </td>\r\n" + 
						"    </tr>\r\n" + 
						"  </tbody>\r\n" + 
						"</table>\r\n" + 
						"\r\n" + 
						"<table style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n" + 
						"  <tbody>\r\n" + 
						"    <tr>\r\n" + 
						"      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px 44px 22px;font-family:'Open Sans',sans-serif;\" align=\"left\">\r\n" + 
						"        \r\n" + 
						"  <div style=\"color: #000000; line-height: 190%; text-align: left; word-wrap: break-word;\">\r\n" + 
						"    <p style=\"font-size: 14px; line-height: 190%;\">"+applicant.getApplyName()+"님 안녕하세요.<br />면접 일정 안내입니다.<br />면접 일시에 <strong><span style=\"color: #363d5a; font-size: 14px; line-height: 26.6px;\">'사이트 바로가기' </span></strong>버튼을 클릭하여 면접을 진행해 주세요.<br />감사합니다.</p>\r\n" + 
						"  </div>\r\n" + 
						"\r\n" + 
						"      </td>\r\n" + 
						"    </tr>\r\n" + 
						"  </tbody>\r\n" + 
						"</table>\r\n" + 
						"\r\n" + 
						"  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\r\n" + 
						"  </div>\r\n" + 
						"</div>\r\n" + 
						"<!--[if (mso)|(IE)]></td><![endif]-->\r\n" + 
						"      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\r\n" + 
						"    </div>\r\n" + 
						"  </div>\r\n" + 
						"</div>\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\r\n" + 
						"  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #ffffff;\">\r\n" + 
						"    <div style=\"border-collapse: collapse;display: table;width: 100%;background-color: transparent;\">\r\n" + 
						"      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-color: #ffffff;\"><![endif]-->\r\n" + 
						"      \r\n" + 
						"<!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\" valign=\"top\"><![endif]-->\r\n" + 
						"<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\r\n" + 
						"  <div style=\"width: 100% !important;\">\r\n" + 
						"  <!--[if (!mso)&(!IE)]><!--><div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\"><!--<![endif]-->\r\n" + 
						"  \r\n" + 
						"<table style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n" + 
						"  <tbody>\r\n" + 
						"    <tr>\r\n" + 
						"      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:'Open Sans',sans-serif;\" align=\"left\">\r\n" + 
						"        \r\n" + 
						"<div align=\"center\">\r\n" + 
						"  <!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-spacing: 0; border-collapse: collapse; mso-table-lspace:0pt; mso-table-rspace:0pt;font-family:'Open Sans',sans-serif;\"><tr><td style=\"font-family:'Open Sans',sans-serif;\" align=\"center\"><v:roundrect xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" href=\"\" style=\"height:47px; v-text-anchor:middle; width:167px;\" arcsize=\"87%\" stroke=\"f\" fillcolor=\"#466b8c\"><w:anchorlock/><center style=\"color:#FFFFFF;font-family:'Open Sans',sans-serif;\"><![endif]-->\r\n" + 
						"    <a href=\"http://localhost:3000/applicant/mypage?Id="+ applicant.getApplyId()+"\" target=\"_blank\" style=\"box-sizing: border-box;display: inline-block;font-family:'Open Sans',sans-serif;text-decoration: none;-webkit-text-size-adjust: none;text-align: center;color: #FFFFFF; background-color: #466b8c; border-radius: 41px; -webkit-border-radius: 41px; -moz-border-radius: 41px; width:auto; max-width:100%; overflow-wrap: break-word; word-break: break-word; word-wrap:break-word; mso-border-alt: none;\">\r\n" + 
						"      <span style=\"display:block;padding:15px 33px;line-height:120%;\"><strong>사이트 바로가기</strong></span>\r\n" + 			
						"    </a>\r\n" + 
						"  <!--[if mso]></center></v:roundrect></td></tr></table><![endif]-->\r\n" + 
						"</div>\r\n" + 
						"\r\n" + 
						"      </td>\r\n" + 
						"    </tr>\r\n" + 
						"  </tbody>\r\n" + 
						"</table>\r\n" + 
						"\r\n" + 
						"<table style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n" + 
						"  <tbody>\r\n" + 
						"    <tr>\r\n" + 
						"      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:'Open Sans',sans-serif;\" align=\"left\">\r\n" + 
						"        \r\n" + 
						"  <table height=\"0px\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;border-top: 1px solid #BBBBBB;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\r\n" + 
						"    <tbody>\r\n" + 
						"      <tr style=\"vertical-align: top\">\r\n" + 
						"        <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;font-size: 0px;line-height: 0px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\r\n" + 
						"          <span>&#160;</span>\r\n" + 
						"        </td>\r\n" + 
						"      </tr>\r\n" + 
						"    </tbody>\r\n" + 
						"  </table>\r\n" + 
						"\r\n" + 
						"      </td>\r\n" + 
						"    </tr>\r\n" + 
						"  </tbody>\r\n" + 
						"</table>\r\n" + 
						"\r\n" + 
						"  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\r\n" + 
						"  </div>\r\n" + 
						"</div>\r\n" + 
						"<!--[if (mso)|(IE)]></td><![endif]-->\r\n" + 
						"      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\r\n" + 
						"    </div>\r\n" + 
						"  </div>\r\n" + 
						"</div>\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\r\n" + 
						"  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">\r\n" + 
						"    <div style=\"border-collapse: collapse;display: table;width: 100%;background-color: transparent;\">\r\n" + 
						"      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-color: transparent;\"><![endif]-->\r\n" + 
						"      \r\n" + 
						"<!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\" valign=\"top\"><![endif]-->\r\n" + 
						"<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\r\n" + 
						"  <div style=\"width: 100% !important;\">\r\n" + 
						"  <!--[if (!mso)&(!IE)]><!--><div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\"><!--<![endif]-->\r\n" + 
						"  \r\n" + 
						"<table style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n" + 
						"  <tbody>\r\n" + 
						"    <tr>\r\n" + 
						"      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:12px 10px;font-family:'Open Sans',sans-serif;\" align=\"left\">\r\n" + 
						"        \r\n" + 
						"  <div style=\"color: #ffffff; line-height: 140%; text-align: left; word-wrap: break-word;\">\r\n" + 
						"    <p style=\"font-size: 14px; line-height: 140%; text-align: center;\"><span style=\"color: #466b8c; font-size: 14px; line-height: 19.6px;\">&copy; SSAFY Company. All Rights Reserved</span></p>\r\n" + 
						"<p style=\"font-size: 14px; line-height: 140%; text-align: center;\"><span style=\"color: #466b8c; font-size: 14px; line-height: 19.6px;\">Talk to us (010) 1212-3333 or </span><br /><span style=\"color: #466b8c; font-size: 14px; line-height: 19.6px;\">Email us ssafytesta405@gmail.com</span></p>\r\n" + 
						"  </div>\r\n" + 
						"\r\n" + 
						"      </td>\r\n" + 
						"    </tr>\r\n" + 
						"  </tbody>\r\n" + 
						"</table>\r\n" + 
						"\r\n" + 
						"  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\r\n" + 
						"  </div>\r\n" + 
						"</div>\r\n" + 
						"<!--[if (mso)|(IE)]></td><![endif]-->\r\n" + 
						"      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\r\n" + 
						"    </div>\r\n" + 
						"  </div>\r\n" + 
						"</div>\r\n" + 
						"\r\n" + 
						"\r\n" + 
						"    <!--[if (mso)|(IE)]></td></tr></table><![endif]-->\r\n" + 
						"    </td>\r\n" + 
						"  </tr>\r\n" + 
						"  </tbody>\r\n" + 
						"  </table>\r\n" + 
						"  <!--[if mso]></div><![endif]-->\r\n" + 
						"  <!--[if IE]></div><![endif]-->");
//				emailContent.append(" <div"
//						+ "	style=\"font-family: 'Apple SD Gothic Neo', 'sans-serif' !important; width: 400px; height: 600px; border-top: 4px solid #02b875; margin: 100px auto; padding: 30px 0; box-sizing: border-box;\">"
//						+ "	<h1 style=\"margin: 0; padding: 0 5px; font-size: 28px; font-weight: 400;\">" + company.getComName()
//						+ "<br /> " + recruit.getReYear() + " " + recruit.getReFlag() + " " + recruit.getReStatus() + " 채용"
//						+ "	</h1>\n" + "	<h2 style=\"margin: 0; padding: 0 5px; font-size: 28px; font-weight: 400;\">"
//						+ "		<span style=\"color: #02b875\">면접일정</span> 안내입니다." + "	</h2>\n"
//						+ "	<p style=\"font-size: 16px; line-height: 26px; margin-top: 50px; padding: 0 5px;\">"
//						+ applicant.getApplyName() + "		님 안녕하세요.<br />" + "		면접 일정 안내입니다.<br />"
//						+ "		면접 일시에  <b style=\"color: #02b875\">'사이트 바로가기'</b> 버튼을 클릭하여 면접을 진행해 주세요.<br />" + "		감사합니다."
//						+ "	</p>" + "	<a style=\"color: #FFF; text-decoration: none; text-align: center;\""
//						+ "	href=\"http://localhost:8080/applicant/mypage?Id=" + applicant.getApplyId()
//						+ "\" target=\"_blank\">" + "		<p"
//						+ "			style=\"display: inline-block; wㅉidth: 210px; height: 45px; margin: 30px 5px 40px; background: #02b875; line-height: 45px; vertical-align: middle; font-size: 16px;\">"
//						+ "			사이트 바로가기</p>" + "	</a>"
//						+ "	<div style=\"border-top: 1px solid #DDD; padding: 5px;\"></div>" + " </div>");
				emailContent.append("</body>");
				emailContent.append("</html>");

				
				emailService.sendMail(applicant.getApplyEmail(), "["+company.getComName()+"] "+recruit.getReYear()+"년 "+recruit.getReFlag()+" "+recruit.getReStatus() +" 채용 면접 일정 안내",
						emailContent.toString());
				
				System.out.println(applicant.getApplyName()+" 이메일 전송 완료");
			}
			
			status=HttpStatus.OK;
			resultMap.put("message", "이메일 전송 성공");		
		}
		catch (RuntimeException e){
			logger.error("이메일 전송 실패", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<>(resultMap, status);

	}

}
