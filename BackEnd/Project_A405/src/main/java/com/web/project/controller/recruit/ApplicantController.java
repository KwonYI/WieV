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
import com.web.project.dao.group.GroupAllDao;
import com.web.project.dao.group.GroupDetailDao;
import com.web.project.dao.group.GroupTypeDao;
import com.web.project.dao.hr.CareerDao;
import com.web.project.dao.hr.CompanyDao;
import com.web.project.dao.hr.PartDao;
import com.web.project.dao.recruit.ApplicantDao;
import com.web.project.dao.recruit.ApplicantGroupDao;
import com.web.project.dao.recruit.RecruitDao;
import com.web.project.model.BasicResponse;
import com.web.project.model.group.GroupAll;
import com.web.project.model.group.GroupAllRequest;
import com.web.project.model.group.GroupDetail;
import com.web.project.model.group.GroupType;
import com.web.project.model.hr.Career;
import com.web.project.model.hr.Company;
import com.web.project.model.hr.Part;
import com.web.project.model.interview.InterviewType;
import com.web.project.model.interview.Interviewer;
import com.web.project.model.interview.TypeInterviewer;
import com.web.project.model.recruit.Applicant;
import com.web.project.model.recruit.ApplicantGroup;
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
	ApplicantGroupDao applicantGroupDao;

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

	@PostMapping("/send")
	@ApiOperation(value = "인증 메일 보내기")
	public void sendMail(@Valid @RequestBody Applicant applicant) throws MessagingException {
		Recruit recruit = recruitDao.findRecruitByReSeq(applicant.getRecruitReSeq());
		Company company = companyDao.findCompanyByComSeq(recruit.getCompanyComSeq());

		StringBuffer emailContent = new StringBuffer();
		emailContent.append("<!DOCTYPE html>");
		emailContent.append("<html>");
		emailContent.append("<head>");
		emailContent.append("</head>");
		emailContent.append("<body>");
		emailContent.append(" <div"
				+ "	style=\"font-family: 'Apple SD Gothic Neo', 'sans-serif' !important; width: 400px; height: 600px; border-top: 4px solid #02b875; margin: 100px auto; padding: 30px 0; box-sizing: border-box;\">"
				+ "	<h1 style=\"margin: 0; padding: 0 5px; font-size: 28px; font-weight: 400;\">" + company.getComName()
				+ "<br /> " + recruit.getReYear() + " " + recruit.getReFlag() + " " + recruit.getReStatus() + " 채용"
				+ "	</h1>\n" + "	<h2 style=\"margin: 0; padding: 0 5px; font-size: 28px; font-weight: 400;\">"
				+ "		<span style=\"color: #02b875\">면접일정</span> 안내입니다." + "	</h2>\n"
				+ "	<p style=\"font-size: 16px; line-height: 26px; margin-top: 50px; padding: 0 5px;\">"
				+ applicant.getApplyName() + "		님 안녕하세요.<br />" + "		면접 일정 안내입니다.<br />"
				+ "		면접 일시에  <b style=\"color: #02b875\">'사이트 바로가기'</b> 버튼을 클릭하여 면접을 진행해 주세요.<br />" + "		감사합니다."
				+ "	</p>" + "	<a style=\"color: #FFF; text-decoration: none; text-align: center;\""
				+ "	href=\"http://localhost:8080/applicant/mypage?Id=" + applicant.getApplyId()
				+ "\" target=\"_blank\">" + "		<p"
				+ "			style=\"display: inline-block; width: 210px; height: 45px; margin: 30px 5px 40px; background: #02b875; line-height: 45px; vertical-align: middle; font-size: 16px;\">"
				+ "			사이트 바로가기</p>" + "	</a>"
				+ "	<div style=\"border-top: 1px solid #DDD; padding: 5px;\"></div>" + " </div>");
		emailContent.append("</body>");
		emailContent.append("</html>");

		// Recruit recruit=RecruitDao.findByreSeq(applicant.getRecruitReSeq());

		emailService.sendMail(applicant.getApplyEmail(), "[" + applicant.getApplyName() + "님 면접정보]",
				emailContent.toString());
	}

	@GetMapping("/mypage/{applyId}")
	@ApiOperation(value = "지원자마이페이지")
	public Object applicantMypage(@RequestParam(required = true) final String applyId) throws MessagingException {

		ResponseEntity response = null;

		Applicant applicant = applicantDao.findApplicantByapplyId(applyId);
		System.out.println(applicant.getApplyName() + "님 접속했습니다.");
		// 지원자에 해당하는 면접일정 갖고오기

		// 단순 확인 용!
		final BasicResponse result = new BasicResponse();
		result.status = true;
		result.data = "success";
		result.object = applicant;

		response = new ResponseEntity<>(result, HttpStatus.OK);

		return response;

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

}
