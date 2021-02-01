package io.openvidu.mvc.java;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import io.openvidu.java.client.OpenViduRole;

// 로그인, 로그아웃 관리
@Controller
public class LoginController {

	public class MyUser {

		String name;
		String pass;
		OpenViduRole role;

		public MyUser(String name, String pass, OpenViduRole role) {
			this.name = name;
			this.pass = pass;
			this.role = role;
		}
	}

	public static Map<String, MyUser> users = new ConcurrentHashMap<>();

	// id : user(유저이름, 비밀번호, 역할)
	public LoginController() {
		users.put("p", new MyUser("publisher", "p", OpenViduRole.PUBLISHER));
		users.put("m", new MyUser("moderator", "m", OpenViduRole.MODERATOR));
		users.put("s", new MyUser("subscriber", "s", OpenViduRole.SUBSCRIBER));
	}

	@RequestMapping(value = "/")
	public String logout(HttpSession httpSession) {
		if (checkUserLogged(httpSession)) {
			return "redirect:/dashboard";
		} else {
			httpSession.invalidate();
			return "index";
		}
	}

	@RequestMapping(value = "/dashboard", method = { RequestMethod.GET, RequestMethod.POST })
	public String login(@RequestParam(name = "user", required = false) String user,
			@RequestParam(name = "pass", required = false) String pass, Model model, HttpSession httpSession) {
		// 로그인된 사람 있는지 확인
		String userName = (String) httpSession.getAttribute("loggedUser");
		if (userName != null) {
			// 로그인되어있다면 대쉬보드(방설정)로 가
			model.addAttribute("username", userName);
			return "dashboard";
		}
		
		// 로그인 안되어있어
		if (login(user, pass)) { // 밑의 login 함수 실행
			// user와 userName ==> loggedUser, username 모두 id(로그인페이지에 입력한 user) 를 의미한다
			httpSession.setAttribute("loggedUser", user);
			model.addAttribute("username", user);
			// 로그인되어있다면 대쉬보드(방설정)로 가
			return "dashboard";

		} else {// 틀리면 redirect
			httpSession.invalidate();
			return "redirect:/";
		}
	}

	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public String logout(Model model, HttpSession httpSession) { // 로그아웃 호출
		httpSession.invalidate();
		return "redirect:/";
	}

	private boolean login(String user, String pass) { // 로그인, 아이디와 비밀번호 일치하는지 확인
		return (user != null && pass != null && users.containsKey(user) && users.get(user).pass.equals(pass));
	}

	private boolean checkUserLogged(HttpSession httpSession) { // 이미 로그인되어있는지 확인
		return !(httpSession == null || httpSession.getAttribute("loggedUser") == null);
	}
}