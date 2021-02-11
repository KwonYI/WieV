package com.web.project.controller.session;

import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.web.project.model.SocketVO;

@Controller
public class SocketController {

	// receive로 메시지를 받는다
	@MessageMapping("/receiveViewee")
	// send로 메시지 반환
	@SendTo("/sendToViewee")
	public SocketVO sendViewee(SocketVO socketVo) {
		String message = socketVo.getMessage();
		List<String> target = socketVo.getTarget();
		boolean flag = socketVo.isSignal();
		
		SocketVO result = new SocketVO(message, target, flag);
		
		return result;
	}
	
	// receive로 메시지를 받는다
	@MessageMapping("/receiveViewer")
	// send로 메시지 반환
	@SendTo("/sendToViewer")
	public SocketVO sendviewer(SocketVO socketVo) {
		String message = socketVo.getMessage();
		List<String> target = socketVo.getTarget();
		boolean flag = socketVo.isSignal();
		
		SocketVO result = new SocketVO(message, target, flag);
		
		return result;
	}

}
