package com.web.project.controller.session;

import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.web.project.model.SocketVO;

@Controller
public class SocketController {

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

}
