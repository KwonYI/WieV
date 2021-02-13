package com.web.project.controller.session;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.web.project.model.SocketVO;

@Controller
public class SocketController {

	// 면접방 대기방 통신
	@MessageMapping("/receive")
	@SendTo("/send")
	public SocketVO viewerToViewee(SocketVO socketVo) {
		String name = socketVo.getName();
		String message = socketVo.getMessage();
		
		SocketVO result = new SocketVO(name, message);
		
		return result;
	}
}
