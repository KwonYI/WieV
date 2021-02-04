package com.web.project.controller.session;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.project.dao.group.DetailOrderDao;
import com.web.project.dao.group.GroupDetailDao;
import com.web.project.dao.group.GroupTypeDao;
import com.web.project.dao.interview.TypeInterviewerDao;
import com.web.project.dao.recruit.ApplicantDao;
import com.web.project.dao.recruit.ApplicantGroupDao;
import com.web.project.model.BasicResponse;
import com.web.project.model.group.GroupType;

import io.openvidu.java.client.ConnectionProperties;
import io.openvidu.java.client.ConnectionType;
import io.openvidu.java.client.OpenVidu;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import io.openvidu.java.client.OpenViduRole;
import io.openvidu.java.client.Session;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
		@ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
		@ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
		@ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })
@CrossOrigin(origins = { "http://localhost:3000" })
@RestController
@RequestMapping("/session")
public class SessionController {

	private OpenVidu openVidu;
	private Map<String, Session> sessions = new ConcurrentHashMap<>(); // 세션이름에 세션객체 매핑
	private Map<String, Map<String, OpenViduRole>> tokensInSession = new ConcurrentHashMap<>(); // 세션이름에 있는 참가자들 관리

	private String OPENVIDU_URL; // 오픈바이두 서버가 수신중인 URL
	private String SECRET; // 서버와 공유하는 비번?

	public static final Logger logger = LoggerFactory.getLogger(SessionController.class);

	@Autowired
	TypeInterviewerDao typeInterviewerDao;

	@Autowired
	GroupTypeDao groupTypeDao;

	@Autowired
	ApplicantDao applicantDao;

	@Autowired
	ApplicantGroupDao applicantGroupDao;

	@Autowired
	DetailOrderDao detailOrderDao;

	@Autowired
	GroupDetailDao groupDetailDao;

	public SessionController(@Value("${openvidu.secret}") String secret, @Value("${openvidu.url}") String openviduUrl) {
		this.SECRET = secret;
		this.OPENVIDU_URL = openviduUrl;
		this.openVidu = new OpenVidu(OPENVIDU_URL, SECRET);
	}

	@GetMapping("/create")
	@ApiOperation(value = "면접관/대기관 세션 객체 생성 및 입장")
	public Object createSession(@RequestParam int interviewerWait, @RequestParam String interviewerName,
			@RequestParam String sessionName) {
		final BasicResponse result = new BasicResponse();
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;

		Session session = null;
		ConnectionProperties connectionProperties = null;
		OpenViduRole role = OpenViduRole.MODERATOR; // 기본 세팅은 관리자
		String type = "manager"; // 면접관, 대기실 관리자, 면접자 구분
		String token = null;

		if (this.sessions.get(sessionName) != null) { // 존재한다
			try {
				if (interviewerWait == 1) { // 1이면 면접관
					type = "interviewer";
					role = OpenViduRole.PUBLISHER;
				}

				connectionProperties = new ConnectionProperties.Builder().type(ConnectionType.WEBRTC).role(role)
						.data(interviewerName).build();

				try {
					session = this.sessions.get(sessionName);
					token = session.createConnection(connectionProperties).getToken();
					this.tokensInSession.get(sessionName).put(token, role);

					resultMap.put("type", type);
					resultMap.put("token", token);
					resultMap.put("sessionName", sessionName);
					resultMap.put("interviewerName", interviewerName);

					result.status = true;
					result.data = "success";
					result.object = resultMap;

					status = HttpStatus.OK;
				} catch (OpenViduJavaClientException e) {
					logger.error("서버 오류", e.getMessage());
					resultMap.put("message", e.getMessage());
					status = HttpStatus.INTERNAL_SERVER_ERROR;

				} catch (OpenViduHttpException e) {
					logger.error("방 입장 실패", e.getMessage());
					resultMap.put("message", e.getMessage());
					status = HttpStatus.INTERNAL_SERVER_ERROR;

				}

			} catch (Exception e) {
				logger.error("면접관 정보가 잘못되었습니다.", e);
				resultMap.put("message", e.getMessage());
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		} else { // 없다
			try {
				if (interviewerWait == 0) { // 대기관리자
					try {
						session = this.openVidu.createSession();
						connectionProperties = new ConnectionProperties.Builder().type(ConnectionType.WEBRTC).role(role)
								.data(interviewerName).build();

						token = session.createConnection(connectionProperties).getToken();

						this.sessions.put(sessionName, session);
						this.tokensInSession.put(sessionName, new ConcurrentHashMap<>());
						this.tokensInSession.get(sessionName).put(token, role);

						resultMap.put("type", type);
						resultMap.put("token", token);
						resultMap.put("sessionName", sessionName);
						resultMap.put("interviewerName", interviewerName);

						result.status = true;
						result.data = "success";
						result.object = resultMap;

						status = HttpStatus.OK;
					} catch (OpenViduJavaClientException e) {
						logger.error("서버 오류", e.getMessage());
						resultMap.put("message", e.getMessage());
						status = HttpStatus.INTERNAL_SERVER_ERROR;

					} catch (OpenViduHttpException e) {
						logger.error("방 입장 실패", e.getMessage());
						resultMap.put("message", e.getMessage());
						status = HttpStatus.INTERNAL_SERVER_ERROR;

					}
				} else { // 면접관
					logger.error("방이 아직 존재하지않습니다.");
					resultMap.put("message", "방이 아직 존재하지않습니다");
					status = HttpStatus.INTERNAL_SERVER_ERROR;
				}

			} catch (Exception e) {
				logger.error("면접관 정보가 잘못되었습니다.", e);
				resultMap.put("message", e.getMessage());
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		}

		printMap(); // 방 정보 출력
		return new ResponseEntity<>(resultMap, status);
	}

	@PostMapping("/join/wait")
	@ApiOperation(value = "지원자 대기방 세션 입장")
	// 해당 지원자의 이름과 속한 세부그룹의 면접방seq가 필요
	public Object enterWaitSession(@RequestParam(name = "group_type_group_type_seq") int groupTypeGroupTypeSeq,
			@RequestParam(name = "applicant_name") String applicantName) {

		final BasicResponse result = new BasicResponse();
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;

		Session session = null;
		ConnectionProperties connectionProperties = null;
		String token = null;

		try {
//			Applicant applicant = applicantDao.findApplicantByapplyId(applyId);
//			int applySeq = applicant.getApplySeq(); 
//		
//			ApplicantGroup applicantGroup = applicantGroupDao.findApplicantGroupByApplicantApplySeq(applySeq);
//			int groupDetailDetailSeq = applicantGroup.getGroupDetailDetailSeq();

			GroupType groupType = groupTypeDao.findGroupTypeByGroupTypeSeq(groupTypeGroupTypeSeq);
			String waitSessionName = groupType.getWaitSessionName();

			if (this.sessions.get(waitSessionName) != null) {
				session = this.sessions.get(waitSessionName);
				connectionProperties = new ConnectionProperties.Builder().type(ConnectionType.WEBRTC)
						.role(OpenViduRole.PUBLISHER).data(applicantName).build();

				token = session.createConnection(connectionProperties).getToken();
				this.tokensInSession.get(waitSessionName).put(token, OpenViduRole.PUBLISHER);

				resultMap.put("token", token);
				resultMap.put("type", "interviewee");
				resultMap.put("sessionName", waitSessionName);
				resultMap.put("applicantName", applicantName);

				result.status = true;
				result.data = "success";
				result.object = resultMap;

				status = HttpStatus.OK;

			} else {
				logger.error("방이 존재하지않습니다.");
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		} catch (Exception e) {
//			logger.error("지원자 정보가 잘못되었습니다.", e);
//			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logger.error("방번호가 잘못되었습니다.", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		printMap();

		return new ResponseEntity<>(resultMap, status);
	}
	
	@PostMapping("/join/interview")
	@ApiOperation(value = "지원자 대기방 세션 입장")
	public Object enterInterviewSession(@RequestParam(name = "group_type_group_type_seq") int groupTypeGroupTypeSeq,
			@RequestParam(name = "applicant_name") String applicantName) {

		final BasicResponse result = new BasicResponse();
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;

		Session session = null;
		ConnectionProperties connectionProperties = null;
		String token = null;

		try {
//			Applicant applicant = applicantDao.findApplicantByapplyId(applyId);
//			int applySeq = applicant.getApplySeq(); 
//		
//			ApplicantGroup applicantGroup = applicantGroupDao.findApplicantGroupByApplicantApplySeq(applySeq);
//			int groupDetailDetailSeq = applicantGroup.getGroupDetailDetailSeq();

			GroupType groupType = groupTypeDao.findGroupTypeByGroupTypeSeq(groupTypeGroupTypeSeq);
			String interviewSessionName = groupType.getInterviewSessionName();

			if (this.sessions.get(interviewSessionName) != null) {
				session = this.sessions.get(interviewSessionName);
				connectionProperties = new ConnectionProperties.Builder().type(ConnectionType.WEBRTC)
						.role(OpenViduRole.PUBLISHER).data(applicantName).build();

				token = session.createConnection(connectionProperties).getToken();
				this.tokensInSession.get(interviewSessionName).put(token, OpenViduRole.PUBLISHER);

				resultMap.put("token", token);
				resultMap.put("type", "interviewee");
				resultMap.put("sessionName", interviewSessionName);
				resultMap.put("applicantName", applicantName);

				result.status = true;
				result.data = "success";
				result.object = resultMap;

				status = HttpStatus.OK;

			} else {
				logger.error("방이 존재하지않습니다.");
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		} catch (Exception e) {
//			logger.error("지원자 정보가 잘못되었습니다.", e);
//			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logger.error("방번호가 잘못되었습니다.", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		printMap();

		return new ResponseEntity<>(resultMap, status);
	}

	@GetMapping("/leaveSession")
	@ApiOperation(value = "세션 나가기")
	public Object removeUser(@RequestParam(name = "sessionName") String sessionName,
			@RequestParam(name = "token") String token) {

		String msg = null;
		HttpStatus status = null;

		if (this.sessions.get(sessionName) != null && this.tokensInSession.get(sessionName) != null) { // 방이 존재하는지

			if (this.tokensInSession.get(sessionName).remove(token) != null) { // 입력으로 들어온 토큰 제거
				msg = "토큰 제거";
				status = HttpStatus.OK;

				if (this.tokensInSession.get(sessionName).isEmpty()) { // 제거 후 방이 빈다면

					if (this.sessions.remove(sessionName) != null && this.tokensInSession.remove(sessionName) != null) {
						logger.trace(sessionName + " 방에 아무도 존재하지않아 제거하였습니다.");
					} else {
						logger.error("존재하지 않는 방입니다.");
						msg = "방이 비었지만 유효하지 않은 방입니다.";
						status = HttpStatus.INTERNAL_SERVER_ERROR;
					}
				}
//				else {
//					if (!checkRemainAdmin(sessionName)) { // 비지않았지만 관리자가 없다면 방 제거
//
//						if (this.tokensInSession.remove(sessionName) != null
//								&& this.sessions.remove(sessionName) != null) {
//							msg = "관리자 토큰 전부 제거"; // 이때 클라이언트에서 처리 필요
//							logger.trace("관리자가 나와 방을 제거하였습니다.");
//						} else {
//							logger.error("에러가 발생했습니다.");
//							status = HttpStatus.INTERNAL_SERVER_ERROR;
//						}
//					}
//				}
			} else { // 유효하지않은 토큰
				logger.error("존재하지 않는 토큰입니다.");
				msg = "존재하지 않는 토큰입니다.";
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		} else {
			logger.error("세션 검색 실패.");
			msg = "존재하지 않는 방입니다.";
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		printMap();

		return new ResponseEntity<>(msg, status);
	}

	public boolean checkRemainAdmin(String sessionName) { // 방에 관리자가 남아있는지 확인, 남은 관리자가 있다면 true
		for (OpenViduRole role : this.tokensInSession.get(sessionName).values()) {
			if (role == OpenViduRole.MODERATOR)
				return true;
		}
		return false;
	}

	public void printMap() { // 모든 방과 방에 있는 사람 정보 출력
		sessions.forEach((sessionName, session) -> {
			System.out.println("세션 UUID : " + sessionName);
			System.out.println("세션에 있는 사람 수 : " + tokensInSession.get(sessionName).size());
			System.out.println("세션에 있는 사람들 : ");
			tokensInSession.get(sessionName).forEach((name, role) -> {
				if (role == OpenViduRole.MODERATOR)
					System.out.print("대기관 : ");

				System.out.println(name);
			});
			System.out.println();
		});
	}
}