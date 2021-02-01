package io.openvidu.mvc.java;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import io.openvidu.java.client.ConnectionProperties;
import io.openvidu.java.client.ConnectionType;
import io.openvidu.java.client.OpenVidu;
import io.openvidu.java.client.OpenViduRole;
import io.openvidu.java.client.Session;

/*
 *  토큰 관리해준다 - 화상채팅할 방 + 방에 연결된 사용자 저장
 * 
 *  방이름과 방세션은  1:1 -> 여기서는 방이름으로 방 구분해(이름이 다르다 = 방 세션이 다르다 = 다른 방이다)
 *  
 *  mapSessions - 방 이름 : 방세션 매핑
 *  mapSessionNamesTokens - 방 이름 : 방에 참가한 참가자들의 토큰과 역할들 매핑
 *  
 *  1. 방 관련 정보들
 *     1) sessionName : 방 이름, dashboard.html에서 작성한 방 이름
 *     2) mapSessions.get(sessionName) : 방이름을 이용해서 방의 토큰을 얻어와
 *     3) session : 방 세션 - io.openvidu.java.client.Session@55d3d561 이런식으로 생겼다
 *						      참가자 토큰에 적힌 sessionId랑은 다르게 생겼다
 *						      같은 방끼린 session이 같고 sessionId도 같다
 *
 *  2. 사람 관련 정보
 *     1) clientData(= nickname) : 입장할때 작성하는 닉네임
 *     2) loggedUser(= username) : 로그인 정보에 작성된 이름
 *     3) token : 방에 입장한 사람의 토큰  wss://localhost:4443?
 *         - 세션아이디                          sessionId=ses_A3ylUwLW6Y&   -> 방이 같으면 동일해
 *           참가자 토큰                         token=tok_HO36RTnCjIeftoyi& -> 참가자 토큰이기때문에 다 달라
 *           방에서 역할                         role=PUBLISHER&
 *           버젼이 담겨있어                    version=2.16.0
 *
 *
 *  웹엑스 세션 = 세션만든다 -> 새로운 세션(방)
 *  	 -> 새로운 방을 판다
 *  
 *
 *  방 연결 & 생성 과정, 내맘대로 해석해버리기
 *  ConnectionProperties : 새로운 연결 생성( 참가자에 대한 설정인거 같다, 어떤 연결(WebRTC)인지, 어떤 역할을 할지 등등)
 *  openVidu.createSession() : 새로운 방 생성, 기존에 있는 방의 경우 mapSessions.get(sessionName)으로 세션 얻어와
 *  
 *   1. ConnectionProperties 생성
 *   2. 기존 or 새로만든 방 세션에 연결 등록(session.createConnection(ConnectionProperties) )
 *   3. 세션에 연결 후 토큰 정보 가져와(session.getToken) 
 *      -> 연결된 세션 아이디( 방 아이디 ) + 토큰( 연결에 대한 고유값 ) + 연결 역할(퍼블리셔, 모더레이더 )
 *   4. 토큰 정보와 방 이름( 이예제에선 이름이 다르면 다른 방이기떄문에 방이름을 넣어준다) model에 넣어줘
 *   5. 별도로 맵에서 생성된 세션들(mapSessions) 과 세션에 속해있는 참가자들 토큰(mapSessionNamesTokens) 관리
 *   
 *   ## 토큰은 OpenVidu Browser메서드 Session.connect에서 사용할 클라이언트 측으로 전송해야 하는 값이다
 *   https://docs.openvidu.io/en/2.16.0/api/openvidu-browser/classes/session.html#connect
 */

@Controller
public class SessionController {

	private OpenVidu openVidu; // OpenVidu object as entrypoint of the SDK

	// 방 이름 : 방의 세션, 방 이름과 방 세션은 1:1 -> 방 세션( 방 이름 )으로 방들 구분
	private Map<String, Session> mapSessions = new ConcurrentHashMap<>();
	// 방 이름 : { 참가자의 토큰 : 참가자 역할 }
	private Map<String, Map<String, OpenViduRole>> mapSessionNamesTokens = new ConcurrentHashMap<>();

	private String OPENVIDU_URL; // URL where our OpenVidu server is listening
	private String SECRET; // Secret shared with our OpenVidu server

	// 스프링부트 돌아갈때 실행
	public SessionController(@Value("${openvidu.secret}") String secret, @Value("${openvidu.url}") String openviduUrl) {
		this.SECRET = secret;
		this.OPENVIDU_URL = openviduUrl;
		this.openVidu = new OpenVidu(OPENVIDU_URL, SECRET);
	}

	// sessionName : dashboard에서 작성한 입장할 방이름
	// clientData  : dashboard에서 작성한 이름
	@RequestMapping(value = "/session", method = RequestMethod.POST)
	public String joinSession(@RequestParam(name = "data") String clientData,
			@RequestParam(name = "session-name") String sessionName, Model model, HttpSession httpSession) {

		// 가장 먼저 로그인은 했는지 확인
		try {checkUserLogged(httpSession);} 
		catch (Exception e) { return "index";}

		// 입장할 사람의 역할과 입장할 사람의 이름 가져와( 로그인 정보에 있는 이름 )
		OpenViduRole role = LoginController.users.get(httpSession.getAttribute("loggedUser")).role;
		String serverData = "{\"serverData\": \"" + httpSession.getAttribute("loggedUser") + "\"}";

		/*
		 * 
		 * ?? Optional data to be passed to other users when this user connects to the
		 * video-call. In this case, a JSON with the value we stored in the HttpSession
		 * object on login
		 * 
		 * https://docs.openvidu.io/en/2.16.0/api/openvidu-java-client/io/openvidu/java/client/ConnectionProperties.Builder.html
		 * ConnectionProperties.Builder() : 커넥션 타입 설정
		 * ConnectionType.WEBRTC: 
		 * data, record, role, kurentoOptions 
		 * data : 연결할 사람인거 같은데 이해는 잘 안된다 
		 * record : 녹화할지말지(true/false)로 준다
		 * role : 디폴트는 PUBLISHER / PUBLISHER, SUBSCRIBER, MODERATOR -> MODERATOR는 강제 연결 해제가능
		 * kurentoOptions : 서버에서/서버로 수신/전송할 수 있는 최대최소 Kbps 설정 + 필터 설정 
		 * 					필터 : 비디오/오디오 설정(화면에 시간, 시계 올리기 / 줄이기,회전 등등인듯하다)
		 */

		// 이 부분이 연결 생성하는 곳인거같다( webRTC 커넥션에 넣어줄 사람의 이름과 역할을 넣어줘서 만든다, 위에 적혀있는 내용 )
		ConnectionProperties connectionProperties = new ConnectionProperties.Builder()
														.type(ConnectionType.WEBRTC)
														.role(role).data(serverData).build();

		// System.out.println(connectionProperties.getData()); -> "serverData"
		// System.out.println(connectionProperties.getRole()); -> role
		// System.out.println(connectionProperties.getRtspUri()); -> null
		// System.out.println(connectionProperties.getKurentoOptions()); -> null
		// System.out.println(connectionProperties.getType()); -> WEBRTC
		// System.out.println(connectionProperties.getNetworkCache()); -> null

		// 방 이름으로 검색 -> 이미 있으면 그 방의 세션을 얻어와서 그 방에 넣어줘
		if (this.mapSessions.get(sessionName) != null) {
			try {
				// createConnection(connectionProperties) - 방 세션에 새로운 연결 이어주고(새로운 사람 넣어주고) 그 사람의 토큰을 가져와
				String token = this.mapSessions.get(sessionName).createConnection(connectionProperties).getToken();

				/*
				 * 입장할 사람의 토큰
				 * wss://localhost:4443?
				 * sessionId=ses_IehaKYQSVe 방의 토큰값                 -> 이미 있는 세션의 경우 동일
				 * &token=tok_Js7KVlXoh9XRWS0P 입장한 사람의 토큰값 -> 입장한 사람마다 달라
				 * &role=PUBLISHER &version=2.16.0
				 */

				System.out.println(sessionName + " 방은 이미 있어 ");
				System.out.println("이 방의 세션은 " + this.mapSessions.get(sessionName) + " 이야");
				System.out.println("너의 토큰 정보는");
				System.out.println(token + " 이고 ");
				System.out.println(" 그 방에 보내줄게 ");

				// 같은 방에 접속된 사람들 정보에 넣어준다 mapSessionNamesTokens - 방세션 : {입장한 사람들의 토큰 : 역할}
				this.mapSessionNamesTokens.get(sessionName).put(token, role);

				// model에는 방이름과 방에 들어간 사람의 토큰값, 그 사람의 원래 아이디와 입장시 작성한 아이디가 들어가
				model.addAttribute("sessionName", sessionName);
				model.addAttribute("token", token);
				model.addAttribute("nickName", clientData); // 입장시 작성한 이름
				model.addAttribute("userName", httpSession.getAttribute("loggedUser")); // 로그인 정보에 있는 이름
				System.out.println();

				// 방으로 들어가
				return "session";

			} catch (Exception e) {
				// If error just return dashboard.html template
				model.addAttribute("username", httpSession.getAttribute("loggedUser"));
				return "dashboard";
			}
		} else { // 새로운 방 생성하는 곳
			try {
				// 새로운 방을 판다! -> 새 세션을 만든다
				Session session = this.openVidu.createSession();
				// 그 방에 넣어줄 사람의 토큰을 발행
				String token = session.createConnection(connectionProperties).getToken();

				System.out.println("새방 팠다 방 이름은  " + sessionName + " 이야");
				System.out.println("이 방의 세션 " + session + " 이고");
				System.out.println("처음 들어가는 너의 토큰 정보는");
				System.out.println(token + " 이야");
				System.out.println();

				this.mapSessions.put(sessionName, session);
				this.mapSessionNamesTokens.put(sessionName, new ConcurrentHashMap<>()); // 방 세션에 넣어줄 참가자들 정보 저장
				this.mapSessionNamesTokens.get(sessionName).put(token, role);

				model.addAttribute("sessionName", sessionName);
				model.addAttribute("token", token);
				model.addAttribute("nickName", clientData); // 입장시 작성한 이름
				model.addAttribute("userName", httpSession.getAttribute("loggedUser")); // 로그인 정보에 있는 이름

				// Return session.html template
				return "session";

			} catch (Exception e) {
				// If error just return dashboard.html template
				model.addAttribute("username", httpSession.getAttribute("loggedUser"));
				return "dashboard";
			}
		}
	}
	
	@RequestMapping(value = "/leave-session", method = RequestMethod.POST)
	public String removeUser(@RequestParam(name = "session-name") String sessionName,
			@RequestParam(name = "token") String token, Model model, HttpSession httpSession) throws Exception {

		// 마찬가지로 먼저 로그인되어있는지 확인
		try { checkUserLogged(httpSession);} 
		catch (Exception e) {return "index";}
		
		System.out.println("Removing user | sessioName=" + sessionName + ", token=" + token);

		// 해당 이름의 방이 있거나 || 해당 방에 연결된 사람이 있는경우
		if (this.mapSessions.get(sessionName) != null && this.mapSessionNamesTokens.get(sessionName) != null) {

			// 참가자가 아직 있을 경우, remove하면 제거할 객체를 반환하나보네
			if (this.mapSessionNamesTokens.get(sessionName).remove(token) != null) {
				// 방이 비어있으면 방 자체를 지워준다
				if (this.mapSessionNamesTokens.get(sessionName).isEmpty()) {
					this.mapSessions.remove(sessionName);
				}
				return "redirect:/dashboard";

			} else {
				// The TOKEN wasn't valid
				System.out.println("Problems in the app server: the TOKEN wasn't valid");
				return "redirect:/dashboard";
			}

		} else {
			// The SESSION does not exist
			System.out.println("Problems in the app server: the SESSION does not exist");
			return "redirect:/dashboard";
		}
	}
	
	// 로그인한 계정이 있는지 확인, 로그인했을때만 방생성, 입장 가능
	private void checkUserLogged(HttpSession httpSession) throws Exception {
		if (httpSession == null || httpSession.getAttribute("loggedUser") == null) {
			throw new Exception("User not logged");
		}
	}
}