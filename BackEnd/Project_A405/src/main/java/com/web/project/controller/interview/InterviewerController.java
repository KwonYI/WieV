package com.web.project.controller.interview;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.apache.ibatis.annotations.Param;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.web.project.controller.hr.HrController;
import com.web.project.dao.group.GroupAllDao;
import com.web.project.dao.group.GroupTypeDao;
import com.web.project.dao.hr.CareerDao;
import com.web.project.dao.hr.CompanyDao;
import com.web.project.dao.hr.PartDao;
import com.web.project.dao.interview.InterviewTypeDao;
import com.web.project.dao.interview.InterviewerDao;
import com.web.project.dao.interview.TypeInterviewerDao;
import com.web.project.dao.recruit.RecruitDao;
import com.web.project.model.BasicResponse;
import com.web.project.model.group.GroupAll;
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
@RequestMapping("/interviewer")
//엑셀의 applicant, interviewer 등록
public class InterviewerController {

	@Autowired
	InterviewerDao interviewerDao;

	@Autowired
	PartDao partDao;

	@Autowired
	CareerDao careerDao;

	@Autowired
	GroupAllDao groupAllDao;

	@Autowired
	GroupTypeDao groupTypeDao;

	@Autowired
	RecruitDao recruitDao;

	@Autowired
	GroupTypeDao groupeTypeDao;

	@Autowired
	TypeInterviewerDao typeInterviewerDao;

	@Autowired
	InterviewTypeDao interviewTypeDao;

	@Autowired
	CompanyDao companyDao;

	@Autowired
	EmailService emailService;

	public static final Logger logger = LoggerFactory.getLogger(HrController.class);
	
	@GetMapping("/getList/{comSeq}")
	@ApiOperation(value = "회사에 따른 면접관 리스트 모두 가져오기")
	public ResponseEntity<List<Map<String, Object>>> getInterviewerListByRecruit(@PathVariable("comSeq") int comSeq){
		List<Map<String, Object>> interviewerList = new ArrayList<Map<String, Object>>();
		HttpStatus status = null;
		
		try {
			List<Interviewer> interviewerTempList = interviewerDao.findAllInterviewerByCompanyComSeq(comSeq);

			for (int i = 0; i < interviewerTempList.size(); i++) {
				Interviewer interviewer = interviewerTempList.get(i);
				
				Map<String, Object> resultMap = new HashMap<String, Object>();
				
				// 면접관 seq
				resultMap.put("interviewer-Seq", interviewer.getViewSeq());
				// 면접관 이름
				resultMap.put("interviewer-Name", interviewer.getViewName());
				// 면접관 email
				resultMap.put("interviewer-Email", interviewer.getViewEmail());
				// 면접관 비밀번호
				resultMap.put("interviewer-Password", interviewer.getViewPassword());
				// 면접관 핸드폰 번호
				resultMap.put("interviewer-Phone", interviewer.getViewPhone());
				// 면접관 대기관 여부
				resultMap.put("interviewer-Wait", interviewer.getViewWait());
				// 면접관 할당 여부
				resultMap.put("interviewer-Assigned", interviewer.getViewAssigned());
				// 면접관 회사 정보
				resultMap.put("interviewer-Company-Seq", interviewer.getCompanyComSeq());
				// 면접관 회사 이름
				resultMap.put("interviewer-Company-Name",companyDao.findCompanyByComSeq(interviewer.getCompanyComSeq()).getComName());
				// 면접관 부서 seq
				resultMap.put("interviewer-Part-Seq", interviewer.getCareerPartPartSeq());
				// 면접관 부서 이름
				resultMap.put("interviewer-Part-Name", partDao.findPartByPartSeq(interviewer.getCareerPartPartSeq()).getPartName());
				// 면접관 직무 seq
				resultMap.put("interviewer-Career-Seq", interviewer.getCareerCaSeq());
				// 면접관 직무 이름
				resultMap.put("interviewer-Career-Name", careerDao.findCareerByCaSeq(interviewer.getCareerCaSeq()).getCaName());
				
				interviewerList.add(resultMap);
			}
			
			status = HttpStatus.OK;
		} catch (RuntimeException e) {
			logger.error("공고의 면접관 리스트 가져오기 실패", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		return new ResponseEntity<List<Map<String, Object>>>(interviewerList, status);
	}

//	@GetMapping("/getList/{comSeq}")
//	@ApiOperation(value = "회사에 따른 면접관리스트 모두 가져오기")
//	public ResponseEntity<List<Interviewer>> getInterviewerList(@PathVariable("comSeq") int comSeq) {
//		List<Interviewer> interviewerList = interviewerDao.findAllInterviewerByCompanyComSeq(comSeq);
//		return new ResponseEntity<List<Interviewer>>(interviewerList, HttpStatus.OK);
//	}

	@DeleteMapping("/delete/{comSeq}")
	@ApiOperation(value = "공고에 따른 지원자 전체 삭제하기")
	public Object delete(@PathVariable("comSeq") int comSeq) {
		HttpStatus status = null;
		Map<String, Object> resultMap = new HashMap<>();

		try {
			List<Interviewer> interviewerList = interviewerDao.findAllInterviewerByCompanyComSeq(comSeq);
			if (!interviewerList.isEmpty()) {

				for (int i = 0; i < interviewerList.size(); i++) {
					Interviewer interviewer = interviewerList.get(i);
					if (interviewer.getViewAssigned() == 0) {
						System.out.println(interviewer.getViewName() + "삭제");
						interviewerDao.delete(interviewer);
					}
				}
				resultMap.put("message", "정보 삭제 성공");
				status = HttpStatus.OK;
			} else {
				logger.error("정보 삭제 실패");
				resultMap.put("message", "정보 삭제 실패");
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		} catch (RuntimeException e) {
			logger.error("정보 삭제 실패 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return new ResponseEntity<>(resultMap, status);
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
		groupTypeList = groupeTypeDao.findListGroupTypeByGroupGroupSeq(nowGroupAll.getGroupSeq());

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
	}

	@PostMapping("/register/{reSeq}")
	@ApiOperation(value = "면접관등록")
	public ResponseEntity<List<Interviewer>> interviewerRegister(@PathVariable("reSeq") int reSeq, MultipartFile files)
			throws EncryptedDocumentException, IOException {
		System.out.println("면접관등록시작");

		HttpStatus status = HttpStatus.ACCEPTED;
		// 웹상에서 업로드 되어 MultipartFile인 경우 바로 InputStream으로 변경하여 사용.
		InputStream inputStream = new ByteArrayInputStream(files.getBytes());

		// 엑셀 로드
		Workbook workbook = WorkbookFactory.create(inputStream);

		List<Interviewer> interviewerList = new ArrayList<Interviewer>();

		// 시트 로드 1, 두번째 시트 로드
		Sheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowItr = sheet.iterator();

		try {
			// 행만큼 반복
			Loop1: while (rowItr.hasNext()) {
				Interviewer interviewer = new Interviewer();
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
						// interviewer.setViewSeq(((Double) getValueFromCell(cell)).intValue());
						// 셀이 숫자형인 경우 Double형으로 변환 후 int형으로 변환
						break;
					case 1:
						// 성명
						interviewer.setViewName((String) getValueFromCell(cell));
						break;
					case 2:
						// 이메일
						String email = (String) getValueFromCell(cell);
						Optional<Interviewer> interviewerEmailOpt = interviewerDao
								.findOptionalInterviewerByViewEmail(email);
						if (interviewerEmailOpt.isPresent()) {
							System.out.println("이메일 중복 되었음");
							continue Loop1;
						} else {
							interviewer.setViewEmail((String) getValueFromCell(cell));
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
						// 폰번호
						interviewer.setViewPhone((String) getValueFromCell(cell));
						break;
					case 6:
						// 관리자(0) or 면접관(1)
						String tmp = (String) getValueFromCell(cell);
						if (tmp.equals("관리자")) {
							interviewer.setViewWait(0);
						} else {
							interviewer.setViewWait(1);
						}
						break;
					}
				}

				Recruit recruit = recruitDao.findRecruitByReSeq(reSeq);

				int comSeq = recruit.getCompanyComSeq();
				interviewer.setCompanyComSeq(comSeq);

				Part part = partDao.findPartByCompanyComSeqAndPartName(comSeq, partName);
				int partSeq = part.getPartSeq();
				interviewer.setCareerPartPartSeq(partSeq);

				Career career = careerDao.findCareerByCaNameAndPartPartSeq(careerName, partSeq);
				int careerSeq = career.getCaSeq();
				interviewer.setCareerCaSeq(careerSeq);

				interviewer.setViewAssigned(0);
				interviewer.setViewPassword(getUUID().substring(0, 10) + "!");
				// insert문
				// interviewerDao.save(interviewer);
				interviewerList.add(interviewer);
			}

			for (int i = 0; i < interviewerList.size(); i++) {
				interviewerDao.save(interviewerList.get(i));
			}
			status = HttpStatus.OK;

		} catch (Exception e) {
			logger.error("면접관 등록 실패 : {}", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			interviewerList = null;
		}

		return new ResponseEntity<List<Interviewer>>(interviewerList, status);

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

	@GetMapping("/getMyInterview")
	@ApiOperation(value = "면접관에게 할당된 면접방 리스트")
	public Object getMyInterview(@RequestParam String userComName, @RequestParam String interviewerEmail) {

		final BasicResponse result = new BasicResponse();
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.OK;

		Interview interviewInfo = null;

		try {
			Interviewer interviewer = interviewerDao.findInterviewerByViewEmail(interviewerEmail);
			if (interviewer != null) {
				int interviewSeq = interviewer.getViewSeq();

				// 면접관 한명은 하나의 면접만 담당한다, list말고 객체로 받아온다
				TypeInterviewer typeInterviewer = typeInterviewerDao
						.findTypeInterviewerByInterviewerViewSeq(interviewSeq);

				String careerName = careerDao.findCareerByCaSeq(typeInterviewer.getInterviewerCareerCaSeq())
						.getCaName(); // 직군 이름
				String partName = partDao.findPartByPartSeq(typeInterviewer.getInterviewerCareerPartPartSeq())
						.getPartName(); // 부서 이름
				String interviewType = interviewTypeDao
						.findInterviewTypeByTypeSeq(typeInterviewer.getGroupTypeInterviewTypeTypeSeq()).getTypeName(); // 면접
																														// 종류

				GroupType groupeType = groupeTypeDao
						.findGroupTypeByGroupTypeSeq(typeInterviewer.getGroupTypeGroupTypeSeq());

				String waitSessionName = groupeType.getWaitSessionName(); // 대기방세션이름
				String interviewSessionName = groupeType.getInterviewSessionName(); // 면접방세션이름
				int groupTypeSeq = groupeType.getGroupTypeSeq();

				GroupAll groupAll = groupAllDao.findGroupAllByGroupSeq(groupeType.getGroupGroupSeq());
				Recruit recruit = recruitDao.findRecruitByReSeq(groupAll.getRecruitReSeq()); // 공고 정보

				interviewInfo = new Interview();
				interviewInfo.setComName(userComName);
				interviewInfo.setGroupDate(groupAll.getGroupDate());
				interviewInfo.setGroupStartTime(groupAll.getGroupStartTime());
				interviewInfo.setRecruitYear(recruit.getReYear());
				interviewInfo.setRecruitStartDate(recruit.getReStartDate());
				interviewInfo.setRecruitEndDate(recruit.getReEndDate());
				interviewInfo.setRecruitFlag(recruit.getReFlag());
				interviewInfo.setRecruitStatus(recruit.getReStatus());
				interviewInfo.setCareerName(careerName);
				interviewInfo.setPartName(partName);
				interviewInfo.setInterviewType(interviewType);
				interviewInfo.setWaitSessionName(waitSessionName);
				interviewInfo.setInterviewSessionName(interviewSessionName);

				resultMap.put("interview", interviewInfo);
				resultMap.put("groupTypeSeq", groupTypeSeq);

				result.status = true;
				result.data = "success";
				result.object = resultMap;

				status = HttpStatus.OK;
			} else {
				logger.error("조회 에러 발생");
				resultMap.put("message", "조회실패");
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}

		} catch (Exception e) {
			logger.error("조회 에러 발생", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return new ResponseEntity<>(resultMap, status);
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
			List<GroupAll> groupAllList = groupAllDao.findListGroupAllByRecruitReSeq(reSeq);
			Recruit recruit = recruitDao.findRecruitByReSeq(reSeq);
			int comSeq = recruit.getCompanyComSeq();
			Company company = companyDao.findCompanyByComSeq(comSeq);

			if (!groupAllList.isEmpty()) {
				System.out.println("공고에 따른 면접 일정 있음");

				for (int i = 0; i < groupAllList.size(); i++) {
					GroupAll groupAll = groupAllList.get(i);
					List<GroupType> groupTypeList = groupTypeDao
							.findListGroupTypeByGroupGroupSeq(groupAll.getGroupSeq());
					for (int j = 0; j < groupTypeList.size(); j++) {
						GroupType groupType = groupTypeList.get(j);
						List<TypeInterviewer> typeInterviewerList = typeInterviewerDao
								.findListTypeInterviewerByGroupTypeGroupTypeSeq(groupType.getGroupTypeSeq());
						for (int k = 0; k < typeInterviewerList.size(); k++) {
							TypeInterviewer typeInterviewer = typeInterviewerList.get(k);
							Interviewer interviewer = interviewerDao.findInterviewerByViewSeq(typeInterviewer.getInterviewerViewSeq());

							StringBuffer emailContent = new StringBuffer();
							emailContent.append(
									"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional //EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
							emailContent.append(
									"<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">");
							emailContent.append("<head>");
							emailContent.append(
									"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n"
											+ "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
											+ "  <meta name=\"x-apple-disable-message-reformatting\">\r\n"
											+ "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\r\n"
											+ "  <title></title>\r\n" + "  \r\n" + "    <style type=\"text/css\">\r\n"
											+ "      a { color: #5f5f8d; text-decoration: none; }\r\n"
											+ "@media only screen and (min-width: 620px) {\r\n" + "  .u-row {\r\n"
											+ "    width: 600px !important;\r\n" + "  }\r\n" + "  .u-row .u-col {\r\n"
											+ "    vertical-align: top;\r\n" + "  }\r\n" + "\r\n"
											+ "  .u-row .u-col-100 {\r\n" + "    width: 600px !important;\r\n"
											+ "  }\r\n" + "\r\n" + "}\r\n" + "\r\n" + "@media (max-width: 620px) {\r\n"
											+ "  .u-row-container {\r\n" + "    max-width: 100% !important;\r\n"
											+ "    padding-left: 0px !important;\r\n"
											+ "    padding-right: 0px !important;\r\n" + "  }\r\n"
											+ "  .u-row .u-col {\r\n" + "    min-width: 320px !important;\r\n"
											+ "    max-width: 100% !important;\r\n"
											+ "    display: block !important;\r\n" + "  }\r\n" + "  .u-row {\r\n"
											+ "    width: calc(100% - 40px) !important;\r\n" + "  }\r\n"
											+ "  .u-col {\r\n" + "    width: 100% !important;\r\n" + "  }\r\n"
											+ "  .u-col > div {\r\n" + "    margin: 0 auto;\r\n" + "  }\r\n" + "}\r\n"
											+ "body {\r\n" + "  margin: 0;\r\n" + "  padding: 0;\r\n" + "}\r\n" + "\r\n"
											+ "table,\r\n" + "tr,\r\n" + "td {\r\n" + "  vertical-align: top;\r\n"
											+ "  border-collapse: collapse;\r\n" + "}\r\n" + "\r\n" + "p {\r\n"
											+ "  margin: 0;\r\n" + "}\r\n" + "\r\n" + ".ie-container table,\r\n"
											+ ".mso-container table {\r\n" + "  table-layout: fixed;\r\n" + "}\r\n"
											+ "\r\n" + "* {\r\n" + "  line-height: inherit;\r\n" + "}\r\n" + "\r\n"
											+ "a[x-apple-data-detectors='true'] {\r\n"
											+ "  color: inherit !important;\r\n"
											+ "  text-decoration: none !important;\r\n" + "}\r\n" + "\r\n"
											+ "</style>\r\n" + "  \r\n" + "  \r\n" + "\r\n"
											+ "<link href=\"https://fonts.googleapis.com/css?family=Open+Sans:400,700\" rel=\"stylesheet\" type=\"text/css\">\r\n");
							emailContent.append("</head>");
							emailContent.append(
									"<body class=\"clean-body\" style=\"margin: 0;padding: 0;-webkit-text-size-adjust: 100%;background-color: #fbfbfb\">");
							emailContent.append(
									"  <table style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;min-width: 320px;Margin: 0 auto;background-color: #fbfbfb;width:100%\" cellpadding=\"0\" cellspacing=\"0\">\r\n"
											+ "  <tbody>\r\n" + "  <tr style=\"vertical-align: top\">\r\n"
											+ "    <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top\">\r\n"
											+ "    <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td align=\"center\" style=\"background-color: #fbfbfb;\"><![endif]-->\r\n"
											+ "    \r\n" + "\r\n"
											+ "<div class=\"u-row-container\" style=\"padding: 0px;background-color: #ffffff\">\r\n"
											+ "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #ffffff;\">\r\n"
											+ "    <div style=\"border-collapse: collapse;display: table;width: 100%;background-image: url('https://ifh.cc/g/O3IRC5.jpg');background-repeat: no-repeat;background-position: center top;background-color: transparent;\">\r\n"
											+ "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: #ffffff;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-image: url('images/image-2.png');background-repeat: no-repeat;background-position: center top;background-color: #ffffff;\"><![endif]-->\r\n"
											+ "      \r\n"
											+ "<!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\" valign=\"top\"><![endif]-->\r\n"
											+ "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\r\n"
											+ "  <div style=\"width: 100% !important;\">\r\n"
											+ "  <!--[if (!mso)&(!IE)]><!--><div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\"><!--<![endif]-->\r\n"
											+ "  \r\n"
											+ "<table style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n"
											+ "  <tbody>\r\n" + "    <tr>\r\n"
											+ "      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:11px;font-family:'Open Sans',sans-serif;\" align=\"left\">\r\n"
											+ "        \r\n"
											+ "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\r\n"
											+ "  <tr>\r\n"
											+ "    <td style=\"padding-right: 0px;padding-left: 0px;\" align=\"center\">\r\n"
											+ "      \r\n"
											+ "      <img align=\"center\" border=\"0\" src=\"https://ifh.cc/g/VvzR6M.png\" alt=\"Image\" title=\"Image\" style=\"outline: none;text-decoration: none;-ms-interpolation-mode: bicubic;clear: both;display: inline-block !important;border: none;height: auto;float: none;width: 100%;max-width: 50px;\" width=\"50\"/>\r\n"
											+ "      \r\n" + "    </td>\r\n" + "  </tr>\r\n" + "</table>\r\n" + "\r\n"
											+ "      </td>\r\n" + "    </tr>\r\n" + "  </tbody>\r\n" + "</table>\r\n"
											+ "\r\n"
											+ "<table style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n"
											+ "  <tbody>\r\n" + "    <tr>\r\n"
											+ "      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:20px 10px 50px;font-family:'Open Sans',sans-serif;\" align=\"left\">\r\n"
											+ "        \r\n"
											+ "  <div style=\"color: #425b8d; line-height: 130%; text-align: center; word-wrap: break-word;\">\r\n"
											+ "    <p style=\"font-size: 14px; line-height: 130%; text-align: center;\">&nbsp;</p>\r\n"
											+ "<p style=\"font-size: 14px; line-height: 130%; text-align: center;\"><span style=\"font-size: 28px; line-height: 36.4px; color: #466b8c;\"><strong><span style=\"font-size: 18px; line-height: 23.4px; font-family: 'Open Sans', sans-serif;\">V<span style=\"font-size: 18px; line-height: 23.4px;\">iew Everywhere </span></span></strong></span></p>\r\n"
											+ "<p style=\"font-size: 14px; line-height: 130%; text-align: center;\"><span style=\"font-size: 44px; line-height: 57.2px; font-family: 'Open Sans', sans-serif; color: #466b8c;\"><strong>WieV</strong></span></p>\r\n"
											+ "<p style=\"font-size: 14px; line-height: 130%; text-align: center;\">&nbsp;</p>\r\n"
											+ "  </div>\r\n" + "\r\n" + "      </td>\r\n" + "    </tr>\r\n"
											+ "  </tbody>\r\n" + "</table>\r\n" + "\r\n"
											+ "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\r\n" + "  </div>\r\n"
											+ "</div>\r\n" + "<!--[if (mso)|(IE)]></td><![endif]-->\r\n"
											+ "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\r\n"
											+ "    </div>\r\n" + "  </div>\r\n" + "</div>\r\n" + "\r\n" + "\r\n"
											+ "\r\n"
											+ "<div class=\"u-row-container\" style=\"padding: 0px;background-color: #ffffff\">\r\n"
											+ "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #ffffff;\">\r\n"
											+ "    <div style=\"border-collapse: collapse;display: table;width: 100%;background-color: transparent;\">\r\n"
											+ "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: #ffffff;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-color: #ffffff;\"><![endif]-->\r\n"
											+ "      \r\n"
											+ "<!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"background-color: #ffffff;width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\" valign=\"top\"><![endif]-->\r\n"
											+ "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\r\n"
											+ "  <div style=\"background-color: #ffffff;width: 100% !important;\">\r\n"
											+ "  <!--[if (!mso)&(!IE)]><!--><div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\"><!--<![endif]-->\r\n"
											+ "  \r\n"
											+ "<table style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n"
											+ "  <tbody>\r\n" + "    <tr>\r\n"
											+ "      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:'Open Sans',sans-serif;\" align=\"left\">\r\n"
											+ "        \r\n"
											+ "  <table height=\"0px\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;border-top: 1px solid #BBBBBB;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\r\n"
											+ "    <tbody>\r\n" + "      <tr style=\"vertical-align: top\">\r\n"
											+ "        <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;font-size: 0px;line-height: 0px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\r\n"
											+ "          <span>&#160;</span>\r\n" + "        </td>\r\n"
											+ "      </tr>\r\n" + "    </tbody>\r\n" + "  </table>\r\n" + "\r\n"
											+ "      </td>\r\n" + "    </tr>\r\n" + "  </tbody>\r\n" + "</table>\r\n"
											+ "\r\n"
											+ "<table style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n"
											+ "  <tbody>\r\n" + "    <tr>\r\n"
											+ "      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:35px 44px 10px;font-family:'Open Sans',sans-serif;\" align=\"left\">\r\n"
											+ "        \r\n"
											+ "  <div style=\"color: #34495e; line-height: 140%; text-align: left; word-wrap: break-word;\">\r\n"
											+ "    <p style=\"font-size: 14px; line-height: 140%;\"><span style=\"color: #4d6381; font-size: 14px; line-height: 19.6px;\"><strong><span style=\"font-size: 22px; line-height: 30.8px;\"><span style=\"color: #466b8c; font-size: 22px; line-height: 30.8px;\">"
											+ company.getComName()
											+ "</span><br /><span style=\"color: #466b8c; font-size: 22px; line-height: 30.8px;\">"
											+ "</span></span><span style=\"font-size: 22px; line-height: 30.8px; color: #3b7df0;\">면접 계정</span>"
											+ "<span style=\"font-size: 22px; line-height: 30.8px; color: #466b8c;\"> 안내입니다.</span><br /><br />"
											+ "<span style=\"color: #363d5a; font-size: 14px; line-height: 26.6px;\">이메일 : "
											+ interviewer.getViewEmail() + "</span><br />"
											+ "<span style=\"color: #363d5a; font-size: 14px; line-height: 26.6px;\">비밀번호 : "
											+ interviewer.getViewPassword() + "</span><br />"
											+ "</strong></span></p>\r\n" + "  </div>\r\n" + "\r\n" + "      </td>\r\n"
											+ "    </tr>\r\n" + "  </tbody>\r\n" + "</table>\r\n" + "\r\n"
											+ "<table style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n"
											+ "  <tbody>\r\n" + "    <tr>\r\n"
											+ "      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px 44px 22px;font-family:'Open Sans',sans-serif;\" align=\"left\">\r\n"
											+ "        \r\n"
											+ "  <div style=\"color: #000000; line-height: 190%; text-align: left; word-wrap: break-word;\">\r\n"
											+ "    <p style=\"font-size: 14px; line-height: 190%;\">"
											+ interviewer.getViewName()
											+ "님 안녕하세요.<br />위의 계정으로 로그인 후 면접을 진행해주세요.<br />감사합니다.</p>\r\n"
											+ "  </div>\r\n" + "\r\n" + "      </td>\r\n" + "    </tr>\r\n"
											+ "  </tbody>\r\n" + "</table>\r\n" + "\r\n"
											+ "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\r\n" + "  </div>\r\n"
											+ "</div>\r\n" + "<!--[if (mso)|(IE)]></td><![endif]-->\r\n"
											+ "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\r\n"
											+ "    </div>\r\n" + "  </div>\r\n" + "</div>\r\n" + "\r\n" + "\r\n"
											+ "\r\n"
											+ "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\r\n"
											+ "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #ffffff;\">\r\n"
											+ "    <div style=\"border-collapse: collapse;display: table;width: 100%;background-color: transparent;\">\r\n"
											+ "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-color: #ffffff;\"><![endif]-->\r\n"
											+ "      \r\n"
											+ "<!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\" valign=\"top\"><![endif]-->\r\n"
											+ "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\r\n"
											+ "  <div style=\"width: 100% !important;\">\r\n"
											+ "  <!--[if (!mso)&(!IE)]><!--><div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\"><!--<![endif]-->\r\n"
											+ "  \r\n"
											+ "<table style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n"
											+ "  <tbody>\r\n" + "    <tr>\r\n"
											+ "      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:'Open Sans',sans-serif;\" align=\"left\">\r\n"
											+ "        \r\n" + "<div align=\"center\">\r\n"
											+ "  <!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-spacing: 0; border-collapse: collapse; mso-table-lspace:0pt; mso-table-rspace:0pt;font-family:'Open Sans',sans-serif;\"><tr><td style=\"font-family:'Open Sans',sans-serif;\" align=\"center\"><v:roundrect xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" href=\"\" style=\"height:47px; v-text-anchor:middle; width:167px;\" arcsize=\"87%\" stroke=\"f\" fillcolor=\"#466b8c\"><w:anchorlock/><center style=\"color:#FFFFFF;font-family:'Open Sans',sans-serif;\"><![endif]-->\r\n"
											+ "    <a href=\"http://localhost:3000/\" target=\"_blank\" style=\"box-sizing: border-box;display: inline-block;font-family:'Open Sans',sans-serif;text-decoration: none;-webkit-text-size-adjust: none;text-align: center;color: #FFFFFF; background-color: #466b8c; border-radius: 41px; -webkit-border-radius: 41px; -moz-border-radius: 41px; width:auto; max-width:100%; overflow-wrap: break-word; word-break: break-word; word-wrap:break-word; mso-border-alt: none;\">\r\n"
											+ "      <span style=\"display:block;padding:15px 33px;line-height:120%;\"><strong>사이트 바로가기</strong></span>\r\n"
											+ "    </a>\r\n"
											+ "  <!--[if mso]></center></v:roundrect></td></tr></table><![endif]-->\r\n"
											+ "</div>\r\n" + "\r\n" + "      </td>\r\n" + "    </tr>\r\n"
											+ "  </tbody>\r\n" + "</table>\r\n" + "\r\n"
											+ "<table style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n"
											+ "  <tbody>\r\n" + "    <tr>\r\n"
											+ "      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:'Open Sans',sans-serif;\" align=\"left\">\r\n"
											+ "        \r\n"
											+ "  <table height=\"0px\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;border-top: 1px solid #BBBBBB;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\r\n"
											+ "    <tbody>\r\n" + "      <tr style=\"vertical-align: top\">\r\n"
											+ "        <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;font-size: 0px;line-height: 0px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\r\n"
											+ "          <span>&#160;</span>\r\n" + "        </td>\r\n"
											+ "      </tr>\r\n" + "    </tbody>\r\n" + "  </table>\r\n" + "\r\n"
											+ "      </td>\r\n" + "    </tr>\r\n" + "  </tbody>\r\n" + "</table>\r\n"
											+ "\r\n" + "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\r\n"
											+ "  </div>\r\n" + "</div>\r\n"
											+ "<!--[if (mso)|(IE)]></td><![endif]-->\r\n"
											+ "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\r\n"
											+ "    </div>\r\n" + "  </div>\r\n" + "</div>\r\n" + "\r\n" + "\r\n"
											+ "\r\n"
											+ "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\r\n"
											+ "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">\r\n"
											+ "    <div style=\"border-collapse: collapse;display: table;width: 100%;background-color: transparent;\">\r\n"
											+ "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-color: transparent;\"><![endif]-->\r\n"
											+ "      \r\n"
											+ "<!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\" valign=\"top\"><![endif]-->\r\n"
											+ "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\r\n"
											+ "  <div style=\"width: 100% !important;\">\r\n"
											+ "  <!--[if (!mso)&(!IE)]><!--><div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\"><!--<![endif]-->\r\n"
											+ "  \r\n"
											+ "<table style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n"
											+ "  <tbody>\r\n" + "    <tr>\r\n"
											+ "      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:12px 10px;font-family:'Open Sans',sans-serif;\" align=\"left\">\r\n"
											+ "        \r\n"
											+ "  <div style=\"color: #ffffff; line-height: 140%; text-align: left; word-wrap: break-word;\">\r\n"
											+ "    <p style=\"font-size: 14px; line-height: 140%; text-align: center;\"><span style=\"color: #466b8c; font-size: 14px; line-height: 19.6px;\">&copy; SSAFY Company. All Rights Reserved</span></p>\r\n"
											+ "<p style=\"font-size: 14px; line-height: 140%; text-align: center;\"><span style=\"color: #466b8c; font-size: 14px; line-height: 19.6px;\">Talk to us (010) 1212-3333 or </span><br /><span style=\"color: #466b8c; font-size: 14px; line-height: 19.6px;\">Email us wievservice@gmail.com</span></p>\r\n"
											+ "  </div>\r\n" + "\r\n" + "      </td>\r\n" + "    </tr>\r\n"
											+ "  </tbody>\r\n" + "</table>\r\n" + "\r\n"
											+ "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\r\n" + "  </div>\r\n"
											+ "</div>\r\n" + "<!--[if (mso)|(IE)]></td><![endif]-->\r\n"
											+ "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\r\n"
											+ "    </div>\r\n" + "  </div>\r\n" + "</div>\r\n" + "\r\n" + "\r\n"
											+ "    <!--[if (mso)|(IE)]></td></tr></table><![endif]-->\r\n"
											+ "    </td>\r\n" + "  </tr>\r\n" + "  </tbody>\r\n" + "  </table>\r\n"
											+ "  <!--[if mso]></div><![endif]-->\r\n"
											+ "  <!--[if IE]></div><![endif]-->");
							emailContent.append("</body>");
							emailContent.append("</html>");

							emailService.sendMail(interviewer.getViewEmail(),
									"[" + company.getComName() + "] " + interviewer.getViewName() + "님 면접 계정 안내",
									emailContent.toString());

							System.out.println(interviewer.getViewName() + " 이메일 전송 완료");
						}
					}
				}
				status = HttpStatus.OK;
				resultMap.put("message", "이메일 전송 성공");
			}
			else {
				status = HttpStatus.OK;
				resultMap.put("message", "면접관 존재하지 않음");
				
			}
		} catch (RuntimeException e) {
			logger.error("이메일 전송 실패", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<>(resultMap, status);
	}
}
