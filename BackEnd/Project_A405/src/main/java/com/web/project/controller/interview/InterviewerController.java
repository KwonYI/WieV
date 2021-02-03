package com.web.project.controller.interview;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.project.controller.hr.HrController;
import com.web.project.dao.group.GroupAllDao;
import com.web.project.dao.group.GroupTypeDao;
import com.web.project.dao.hr.CareerDao;
import com.web.project.dao.hr.PartDao;
import com.web.project.dao.interview.InterviewerDao;
import com.web.project.dao.interview.TypeInterviewerDao;
import com.web.project.dao.recruit.RecruitDao;
import com.web.project.model.BasicResponse;
import com.web.project.model.group.GroupAll;
import com.web.project.model.group.GroupType;
import com.web.project.model.hr.Career;
import com.web.project.model.hr.Part;
import com.web.project.model.interview.InterviewType;
import com.web.project.model.interview.Interviewer;
import com.web.project.model.interview.TypeInterviewer;
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
	RecruitDao recruitDao;

	@Autowired
	GroupTypeDao groupeTypeDao;

	@Autowired
	TypeInterviewerDao typeInterviewerDao;

	public static final Logger logger = LoggerFactory.getLogger(HrController.class);

	@GetMapping("/getList/{comSeq}")
	@ApiOperation(value = "회사에 따른 면접관리스트 모두 가져오기")
	public ResponseEntity<List<Interviewer>> getInterviewerList(@PathVariable("comSeq") int comSeq) {
		List<Interviewer> interviewerList=interviewerDao.findAllInterviewerByCompanyComSeq(comSeq);
		return new ResponseEntity<List<Interviewer>>(interviewerList, HttpStatus.OK);
	}
		
	//@PostMapping("/assign/{groupSeq}")
	//@ApiOperation(value = "면접관 자동 배정")
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
//			status = HttpStatus.OK;
//		} catch (RuntimeException e) {
//			logger.error("면접관 자동 배정 실패", e);
//			status = HttpStatus.INTERNAL_SERVER_ERROR;
//		}
//		return new ResponseEntity<>("면접관 자동 배정 완료", status);
	}

	@PostMapping("/register")
	@ApiOperation(value = "면접관등록")
	public List<Interviewer> interviewerRegister(@Valid @RequestBody Recruit recruit)
			throws EncryptedDocumentException, IOException {
		// 웹상에서 업로드 되어 MultipartFile인 경우 바로 InputStream으로 변경하여 사용.
		// InputStream inputStream = new ByteArrayInputStream(file.getBytes());

		String filePath = "C:\\example.xlsx"; // xlsx 형식
		// String filePath = "D:\\applicant.xls"; // xls 형식

		InputStream inputStream = new FileInputStream(filePath);

		// 엑셀 로드
		Workbook workbook = WorkbookFactory.create(inputStream);

		List<Interviewer> interviewerList = new ArrayList<Interviewer>();

		// 시트 로드 1, 두번째 시트 로드
		Sheet sheet = workbook.getSheetAt(1);
		Iterator<Row> rowItr = sheet.iterator();

		// 행만큼 반복
		while (rowItr.hasNext()) {
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
					interviewer.setViewSeq(((Double) getValueFromCell(cell)).intValue());
					// 셀이 숫자형인 경우 Double형으로 변환 후 int형으로 변환
					break;
				case 1:
					// 성명
					interviewer.setViewName((String) getValueFromCell(cell));
					break;
				case 2:
					// 이메일
					interviewer.setViewEmail((String) getValueFromCell(cell));
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

			int comSeq = recruit.getCompanyComSeq();
			interviewer.setCompanyComSeq(comSeq);

			Part part = partDao.findPartByCompanyComSeqAndPartName(comSeq, partName);
			int partSeq = part.getPartSeq();
			interviewer.setCareerPartPartSeq(partSeq);

			Career career = careerDao.findCareerByCaNameAndPartPartSeq(careerName, partSeq);
			int careerSeq = career.getCaSeq();
			interviewer.setCareerCaSeq(careerSeq);

			interviewer.setViewAssigned(0);
			// insert문
			interviewer.setViewWait(1);
			//insert문
			interviewerDao.save(interviewer);

			interviewerList.add(interviewer);
		}
		return interviewerList;
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
}
