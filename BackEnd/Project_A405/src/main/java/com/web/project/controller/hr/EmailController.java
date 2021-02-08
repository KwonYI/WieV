package com.web.project.controller.hr;

import java.util.Optional;
import java.util.Random;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.web.project.dao.hr.HrDao;
import com.web.project.model.BasicResponse;
import com.web.project.model.hr.Hr;
import com.web.project.model.hr.SignupRequest;
import com.web.project.service.hr.EmailService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
		@ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
		@ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
		@ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })
@CrossOrigin(origins = { "http://localhost:3000" })
@RestController
@RequestMapping("/email")
public class EmailController {

	@Autowired
	private HrDao hrDao;
	
	@Autowired
	private EmailService emailService;

	@PostMapping("/send")
	@ApiOperation(value = "인증 메일 보내기")
	public String sendMail(@RequestBody SignupRequest request) throws MessagingException {
		String email=request.getHrEmail();
		StringBuffer emailContent = new StringBuffer();
		String certifiedKey=certified_key();
		
		emailContent.append("<!DOCTYPE html>");
		emailContent.append("<html>");
		emailContent.append("<head>");
		emailContent.append("</head>");
		emailContent.append("<body>");
		emailContent.append(" <div"
				+ "	style=\"font-family: 'Apple SD Gothic Neo', 'sans-serif' !important; width: 400px; height: 600px; border-top: 4px solid #02b875; margin: 100px auto; padding: 30px 0; box-sizing: border-box;\">"
				+ "	<h1 style=\"margin: 0; padding: 0 5px; font-size: 28px; font-weight: 400;\">"
				+ "		<span style=\"font-size: 15px; margin: 0 0 10px 3px;\">Test A405</span><br />"
				+ "		<span style=\"color: #02b875\">메일인증</span> 안내입니다." + "	</h1>\n"
				+ "	<p style=\"font-size: 16px; line-height: 26px; margin-top: 50px; padding: 0 5px;\">"
				+"		Test A405에 가입해 주셔서 진심으로 감사드립니다.<br />"
				+ "		아래 <b style=\"color: #02b875\">'메일 인증 번호'</b>를 입력해 주세요.<br />" + "		감사합니다.<br/>"
				+ "	</p>" 
				+" <h3><b style=\"color: #02b875\">"+certifiedKey+"</b></h3>" 
				+ " </div>");
		emailContent.append("</body>");
		emailContent.append("</html>");
		emailService.sendMail(email, "[Test A405 이메일 인증]", emailContent.toString());
		
		return certifiedKey;
	}
	
	// 10자리 인증키 만들어주는 Method
		private String certified_key() {
			Random random = new Random();
			StringBuilder sb = new StringBuilder();
			int num = 0;

			do {
				num = random.nextInt(75) + 48;
				// 숫자, 대문자, 소문자
				if ((num >= 48 && num <= 57) || (num >= 65 && num <= 90) || (num >= 97 && num <= 122)) {
					sb.append((char) num);
				} else
					continue;
			} while (sb.length() < 10);

			return sb.toString();
		}
	
//	@GetMapping("/certified")
//	@ApiOperation(value = "인증 확인하기")
//	public Object checkMail(@RequestParam(required = true) final String email,
//            @RequestParam(required = true) final String certified) throws MessagingException {
//		
//		ResponseEntity response = null;
//		
//		// 현재는 "N"을 가져옴
//		Optional<Hr> hrCertified = hrDao.findHrByHrCertified(certified);
//		
//		// UPDATE(U) -> SELECT(R) + INSERT(C)
//		hrCertified.ifPresent(selectHr->{
//			// 인증 여부 변경
//			selectHr.setHrCertified("Y");
//			
//			// INSERT!
//			hrDao.save(selectHr);
//		});
//		
//		
//		// 단순 확인 용!
//		if(hrCertified.isPresent()) {
//			final BasicResponse result = new BasicResponse();
//            result.status = true;
//            result.data = "success";
//            result.object = hrCertified;
//            
//            response = new ResponseEntity<>(result, HttpStatus.OK);
//		}else {
//			response = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//		
//		return response;
//	}
}
