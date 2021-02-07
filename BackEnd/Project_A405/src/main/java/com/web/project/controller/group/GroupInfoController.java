package com.web.project.controller.group;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.web.project.model.group.GroupDetail;
import com.web.project.model.group.GroupType;
import com.web.project.model.interview.InterviewType;
import com.web.project.model.interview.Interviewer;
import com.web.project.model.interview.TypeInterviewer;
import com.web.project.model.recruit.ApplicantGroup;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
		@ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
		@ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
		@ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })
@CrossOrigin(origins = { "http://localhost:3000" })
@RestController
@RequestMapping("/groupInfo")
public class GroupInfoController {
	@Autowired
	GroupAllDao groupAllDao;

	@Autowired
	RecruitDao recruitDao;

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
	
	@Autowired
	CareerDao careerDao;

	public static final Logger logger = LoggerFactory.getLogger(HrController.class);

	@GetMapping("/totalList/{recruitSeq}")
	@ApiOperation(value = "공고 그룹 모두 가져오기")
	public ResponseEntity<JSONArray> totalList(@PathVariable("recruitSeq") int recruitSeq) {
		HttpStatus status = null;
		JSONArray jsonArray = new JSONArray();

		try {
			List<GroupAll> groupAllList = groupAllDao.findListGroupAllByRecruitReSeq(recruitSeq);

			for (int i = 0; i < groupAllList.size(); i++) {

				// 1. 공고에 연결된 그룹 중 하나 가져오기
				GroupAll groupAll = groupAllList.get(i);

				// 2. Json Object 생성
				JSONObject jsonObject = new JSONObject();

				// 3. 그룹 Seq
				jsonObject.put("groupSeq", groupAll.getGroupSeq());

				// 4. 그룹 면접 실시 날짜
				jsonObject.put("groupDate", groupAll.getGroupDate());

				// 5. 그룹 면접 시작 시간
				jsonObject.put("groupStartTime", groupAll.getGroupStartTime());

				// 6. 그룹 면접 유형 목록
				List<String> interviewTypeList = new ArrayList<String>();

				// 6-1. groupType 테이블에서 그룹에 연결된 면접 유형 seq 얻어오기
				List<GroupType> groupTypeList = groupTypeDao.findListGroupTypeByGroupGroupSeq(groupAll.getGroupSeq());

				// 6-2. 면접 유형 seq로 면접 유형 String 저장
				for (int j = 0; j < groupTypeList.size(); j++) {
					interviewTypeList.add(interviewTypeDao
							.findInterviewTypeByTypeSeq(groupTypeList.get(j).getInterviewTypeTypeSeq()).getTypeName());
				}
				jsonObject.put("interviewTypeList", interviewTypeList);

				// 7. 그룹 지원자 목록 => 세부 그룹 만들면서 진행
				List<String> groupApplicantList = new ArrayList<String>();

				// 7-1. applicant_group 테이블에서 지원자 Seq를 가져온다.
				// 7-2. 지원자 seq로 지원자 이름을 찾아 저장

				// 8. 면접관, 대기관 목록 저장
				List<Map<String, Object>> waitInterviewerList = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> interviewerList = new ArrayList<Map<String, Object>>();

				// 8-1. 이전의 groupTypeList를 활용해서 groupTypeSeq에 연결된 면접관, 대기관을 불러온다.
				for (int j = 0; j < groupTypeList.size(); j++) {
					List<TypeInterviewer> typeInterviewerList = typeInterviewerDao
							.findListTypeInterviewerByGroupTypeGroupTypeSeq(groupTypeList.get(j).getGroupTypeSeq());

					// 8-2. typeInterviewList에서 각 Interviewer의 정보를 가져와서 대기관과 면접관을 구분해서 List에 등록한다.
					for (int k = 0; k < typeInterviewerList.size(); k++) {
						TypeInterviewer typeInterviewer = typeInterviewerList.get(k);

						// 8-2-1. 면접 유형과 이름을 연결해주는 map
						Map<String, Object> interviewerMap = new HashMap<>();

						// 8-2-2. 면접 유형 저장
						interviewerMap.put("interviewType",
								interviewTypeDao
										.findInterviewTypeByTypeSeq(typeInterviewer.getGroupTypeInterviewTypeTypeSeq())
										.getTypeName());

						// 8-2-3. 면접관 정보 가져오기
						Interviewer interviewer = interviewerDao
								.findInterviewerByViewSeq(typeInterviewer.getInterviewerViewSeq());

						// 8-2-4. 면접관 이름 저장하기
						interviewerMap.put("interviewerName", interviewer.getViewName());

						// 8-2-5. 대기관인지 면접관인지 판단해서 List에 등록
						if (interviewer.getViewWait() == 0) { // 대기관
							waitInterviewerList.add(interviewerMap);
						} else { // 면접관
							interviewerList.add(interviewerMap);
						}
					}
				}
				jsonObject.put("waitInterviewerList", waitInterviewerList);
				jsonObject.put("interviewerList", interviewerList);

				// 9. 세부 그룹 정보 넣기
				List<Map<String, Object>> groupDetailList = new ArrayList<Map<String, Object>>();
				// 9-1. 그룹의 세부 그룹들 가져오기
				List<GroupDetail> detailList = groupDetailDao
						.findListGroupDetailByGroupGroupSeq(groupAll.getGroupSeq());
				// 9-2. 세부 그룹의 정보 가져오기
				for (int j = 0; j < detailList.size(); j++) {
					// 9-2-1. 세부 그룹 seq, 세부 그룹 면접 순서, 지원자 목록을 연결해주는 map
					Map<String, Object> groupDetailMap = new HashMap<>();

					GroupDetail groupDetail = detailList.get(j);

					// 9-2-2. 세부 그룹 seq 저장
					groupDetailMap.put("groupDetailSeq", groupDetail.getDetailSeq());

					// 9-2-3. 세부 그룹 면접 순서
					List<String> detailOrder = new ArrayList<String>();

					// 9-2-3-1. 세부 그룹 면접 순서 데이터 가져오기
					List<DetailOrder> detailOrderList = detailOrderDao
							.findListDetailOrderByGroupDetailDetailSeq(groupDetail.getDetailSeq());

					// 9-2-3-2. 순서대로 재배치
					Collections.sort(detailOrderList, (o1, o2) -> {
						return o1.getTrueOrder() - o2.getTrueOrder();
					});

					for (int k = 0; k < detailOrderList.size(); k++) {
						detailOrder.add(interviewTypeDao
								.findInterviewTypeByTypeSeq(detailOrderList.get(k).getGroupTypeInterviewTypeTypeSeq())
								.getTypeName());
					}

					groupDetailMap.put("detailOrder", detailOrder);

					// 9-2-4. 세부 그룹에 연결되어 있는 지원자 목록 불러오기
					List<String> applicantList = new ArrayList<String>();

					// 9-2-4-1. 세부 그룹에 연결되어 있는 지원자 Seq 찾아오기
					List<ApplicantGroup> applicantGroupList = applicantGroupDao
							.findListApplicantGroupByGroupDetailDetailSeq(groupDetail.getDetailSeq());

					// 9-2-4-2. 지원자 Seq로 이름 찾아오기
					for (int k = 0; k < applicantGroupList.size(); k++) {
						ApplicantGroup applicantGroup = applicantGroupList.get(k);
						
						String name = applicantDao.findApplicantByApplySeq(applicantGroup.getApplicantApplySeq())
								.getApplyName();
						
						applicantList.add(name);

						// 7번 과정 해결
						groupApplicantList.add(name);
					}
					groupDetailMap.put("groupDetailApplicant", applicantList);

					// 9-2-5. List에 저장
					groupDetailList.add(groupDetailMap);
				}
				jsonObject.put("groupApplicantList", groupApplicantList);
				jsonObject.put("groupDetailList", groupDetailList);
				
				// 10. 해당 그룹이 어떤 직무인지 저장
				jsonObject.put("groupCareerName", careerDao.findCareerByCaSeq(groupAll.getCareerCaSeq()).getCaName());

				// 11. JSON Array에 넣는다.
				jsonArray.put(jsonObject);
				System.out.println(jsonObject);
			}
			status = HttpStatus.OK;
		} catch (RuntimeException e) {
			jsonArray = null;
			logger.error("공고에 해당하는 모든 정보 가져오기 실패", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return new ResponseEntity<JSONArray>(jsonArray, status);
	}
}
