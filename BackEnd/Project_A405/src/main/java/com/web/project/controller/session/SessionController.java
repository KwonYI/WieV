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
		OpenViduRole role = OpenViduRole.MODERATOR;
		String type = "manager";
		String token = null;

		if (this.sessions.get(sessionName) != null) {
			try {
				if (interviewerWait == 1) {
					type = "viewer";
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
		} else {
			try {
				if (interviewerWait == 0) {
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
				} else {
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

		printMap();
		return new ResponseEntity<>(resultMap, status);
	}

	@GetMapping("/join")
	@ApiOperation(value = "지원자 세션 입장")
	public Object enterSession(@RequestParam String applicantName, @RequestParam String sessionName) {

		System.out.println(sessionName);
		final BasicResponse result = new BasicResponse();
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;

		Session session = null;
		ConnectionProperties connectionProperties = null;
		String token = null;

		if (this.sessions.get(sessionName) != null) {
			session = this.sessions.get(sessionName);
			connectionProperties = new ConnectionProperties.Builder().type(ConnectionType.WEBRTC)
					.role(OpenViduRole.PUBLISHER).data(applicantName).build();

			try {
				token = session.createConnection(connectionProperties).getToken();

				this.tokensInSession.get(sessionName).put(token, OpenViduRole.PUBLISHER);

				resultMap.put("token", token);
				resultMap.put("type", "viewee");
				resultMap.put("sessionName", sessionName);
				resultMap.put("applicantName", applicantName);

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
		} else {
			logger.error("방이 존재하지않습니다.");
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		printMap();

		return new ResponseEntity<>(resultMap, status);
	}

	@GetMapping("/getToken")
	@ApiOperation(value = "토큰 다시 받기")
	public Object getToken(@RequestParam(name = "sessionName") String sessionName,
			@RequestParam(name = "name") String name, @RequestParam(name = "type") String type) {

		final BasicResponse result = new BasicResponse();
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;

		Session session = null;
		ConnectionProperties connectionProperties = null;
		String token = null;

		if (sessions.get(sessionName) != null) {
			session = this.sessions.get(sessionName);
			connectionProperties = new ConnectionProperties.Builder().type(ConnectionType.WEBRTC)
					.role(OpenViduRole.PUBLISHER).data(name).build();

			try {
				token = session.createConnection(connectionProperties).getToken();

				resultMap.put("token", token);
				resultMap.put("type", type);
				resultMap.put("sessionName", sessionName);
				resultMap.put("name", name);

				result.status = true;
				result.data = "success";
				result.object = resultMap;

				status = HttpStatus.OK;
			} catch (OpenViduJavaClientException e) {
				logger.error("서버 오류", e.getMessage());
				resultMap.put("message", e.getMessage());
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			} catch (OpenViduHttpException e) {
				logger.error("토큰 생성 실패", e.getMessage());
				resultMap.put("message", e.getMessage());
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		} else {
			logger.error("방이 존재하지않습니다.");
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return new ResponseEntity<>(resultMap, status);
	}

	@GetMapping("/removeAllSession")
	@ApiOperation(value = "세션 전부 초기화")
	public Object removeAllSession() {
		sessions.clear();
		tokensInSession.clear();
		
		final BasicResponse result = new BasicResponse();
		HttpStatus status = null;

		if (this.sessions.isEmpty() && this.tokensInSession.isEmpty()) {
			result.status = true;
			result.data = "success";
			result.object = "성공";
			status = HttpStatus.OK;
		} else {
			logger.error("세션 정보 삭제 실패");
			result.status = false;
			result.object = "실패";
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		printMap();

		return new ResponseEntity<>(result, status);
	}
	
	@GetMapping("/removeSession")
	@ApiOperation(value = "해당 세션 초기화")
	public Object removeSession(@RequestParam(name = "sessionName") String sessionName) {
		final BasicResponse result = new BasicResponse();
		HttpStatus status = null;
		
		if(this.sessions.remove(sessionName) != null) {
			if(this.tokensInSession.remove(sessionName) != null) {
				result.status = true;
				result.data = "success";
				result.object = "성공";
				status = HttpStatus.OK;
			}else {
				logger.error("세션안의 토큰이 비어있습니다.");
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		}else {
			if(this.tokensInSession.remove(sessionName) != null) {
				logger.error("세션안의 토큰이 비어있습니다.");
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}else {
				logger.error("해당 세션이 존재하지않습니다.");
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		}
		
		printMap();

		return new ResponseEntity<>(result, status);
	}

	@GetMapping("/leaveSession")
	@ApiOperation(value = "세션 나가기")
	public Object removeUser(@RequestParam(name = "sessionName") String sessionName,
			@RequestParam(name = "token") String token) {

		String msg = null;
		HttpStatus status = null;

		if (this.sessions.get(sessionName) != null && this.tokensInSession.get(sessionName) != null) {

			if (this.tokensInSession.get(sessionName).remove(token) != null) {
				msg = "토큰 제거";
				status = HttpStatus.OK;

				if (this.tokensInSession.get(sessionName).isEmpty()) {

					if (this.sessions.remove(sessionName) != null && this.tokensInSession.remove(sessionName) != null) {
						logger.trace(sessionName + " 방에 아무도 존재하지않아 제거하였습니다.");
					} else {
						logger.error("존재하지 않는 방입니다.");
						msg = "방이 비었지만 유효하지 않은 방입니다.";
						status = HttpStatus.INTERNAL_SERVER_ERROR;
					}
				}
			} else {
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

	public void printMap() {
		System.out.println("모든 세션 출력중....");
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
