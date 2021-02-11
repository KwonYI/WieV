package com.web.project.controller.session;

import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.web.project.model.SocketVO;

@Controller
public class SocketController {

	// 면접관이 지원자에게 응답
	@MessageMapping("/receiveViewerToViewee")
	@SendTo("/sendViewerToViewee")
	public SocketVO viewerToViewee(SocketVO socketVo) {
		String name = socketVo.getName();
		String message = socketVo.getMessage();
		List<String> target = socketVo.getTarget();
		boolean signal = socketVo.isSignal();
		
		SocketVO result = new SocketVO(name, message, target, signal);
		
		return result;
	}
	
	// 지원자가 면접관에게 응답
	@MessageMapping("/receiveVieweeToViewer")
	@SendTo("/sendVieweeToViewer")
	public SocketVO vieweeToViewer(SocketVO socketVo) {
		String name = socketVo.getName();
		String message = socketVo.getMessage();
		List<String> target = socketVo.getTarget();
		boolean signal = socketVo.isSignal();

		SocketVO result = new SocketVO(name, message, target, signal);
		
		return result;
	}
	
	// 대기방-면접방 통신
	@MessageMapping("/receiveViewerToViewer")
	@SendTo("/sendViewerToViewer")
	public SocketVO viewerToViewer(SocketVO socketVo) {
		String name = socketVo.getName();
		String message = socketVo.getMessage();
		List<String> target = socketVo.getTarget();
		boolean signal = socketVo.isSignal();

		SocketVO result = new SocketVO(name, message, target, signal);
		
		return result;
	}
	
	// 버튼 활성 비활성
	@MessageMapping("/receiveInWaitSession")
	@SendTo("/sendInWaitSession")
	public SocketVO WaitSession(SocketVO socketVo) {
		String name = socketVo.getName();
		String message = socketVo.getMessage();
		List<String> target = socketVo.getTarget();
		boolean signal = socketVo.isSignal();

		SocketVO result = new SocketVO(name, message, target, signal);
		
		return result;
	}
	
	// 버튼 활성 비활성
	@MessageMapping("/receiveInInterviewSession")
	@SendTo("/sendInInterviewSession")
	public SocketVO InInterviewSession(SocketVO socketVo) {
		String name = socketVo.getName();
		String message = socketVo.getMessage();
		List<String> target = socketVo.getTarget();
		boolean signal = socketVo.isSignal();

		SocketVO result = new SocketVO(name, message, target, signal);
		
		return result;
	}
}
