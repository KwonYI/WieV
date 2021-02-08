package com.web.project.controller.hr;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.web.project.dao.hr.CompanyDao;
import com.web.project.dao.hr.HrDao;
import com.web.project.dao.interview.InterviewerDao;
import com.web.project.model.BasicResponse;
import com.web.project.model.hr.SignupRequest;
import com.web.project.model.hr.UpdateRequest;
import com.web.project.model.interview.Interviewer;
import com.web.project.model.hr.Company;
import com.web.project.model.hr.Hr;
import com.web.project.model.hr.LoginRequest;
import com.web.project.service.hr.EmailService;
import com.web.project.service.hr.JwtService;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
		@ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
		@ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
		@ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })
@CrossOrigin(origins = { "http://localhost:3000" })
@RestController
@RequestMapping("/hr")
public class HrController {

	@Autowired
	private JwtService jwtService;
	
	@Autowired
	HrDao hrDao;
	
	@Autowired
	CompanyDao companyDao;
	
	@Autowired
	InterviewerDao interviewerDao;
	
	@Autowired
	private EmailService emailService;
	
	public static final Logger logger = LoggerFactory.getLogger(HrController.class);
	
	@PostMapping("/login")
	@ApiOperation(value = "로그인")
	public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
		
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;

		Optional<Hr> hrOpt = hrDao.findHrByHrEmailAndHrPassword(loginRequest.getUserEmail(), loginRequest.getUserPassword());
		Optional<Interviewer> interviewerOpt = interviewerDao.findOptionalInterviewerByViewEmailAndViewPassword(loginRequest.getUserEmail(), loginRequest.getUserPassword());
	
		try {
			if (hrOpt.isPresent()) {
				// Swagger용!
				final BasicResponse result = new BasicResponse();
				result.status = true;
				result.data = "success";
				
				// Optional -> 일반 객체
				Hr loginHr = hrOpt.get();
				
				// login token
				String token = jwtService.create(loginHr);
				logger.trace("로그인 토큰 정보 : {}", token);
				resultMap.put("auth-token", token);
				resultMap.put("user-Seq", loginHr.getHrSeq());
				// email
				resultMap.put("user-Email", loginHr.getHrEmail());
				// 이름
				resultMap.put("user-Name", loginHr.getHrName());
				// 핸드폰 번호
				resultMap.put("user-Phone", loginHr.getHrPhone());
				// 면접관(1)인지 대기관(0)인지
				resultMap.put("user-View-Wait", -1);
				// 소속 회사 Seq
				resultMap.put("user-Company-Seq", loginHr.getCompanyComSeq());
				// 소속 회사 이름
				Company company = companyDao.findCompanyByComSeq(loginHr.getCompanyComSeq());
				resultMap.put("user-Company-Name", company.getComName());
				// 소속 회사 로고
				resultMap.put("user-Company-Logo", company.getComLogo());
				// 소속 회사 주소
				resultMap.put("user-Company-Address", company.getComAddress());
				// 소속 회사 홈페이지 주소
				resultMap.put("user-Company-Homepage", company.getComHomepage());
				
				result.object = resultMap;
				status = HttpStatus.OK;
			} else if(interviewerOpt.isPresent()) {
				// Swagger용!
				final BasicResponse result = new BasicResponse();
				result.status = true;
				result.data = "success";
				
				Interviewer loginInterviewer = interviewerOpt.get();
				
				// login token
				String token = jwtService.create(loginInterviewer);
				logger.trace("로그인 토큰 정보 : {}", token);
				resultMap.put("auth-token", token);
				resultMap.put("user-Seq", loginInterviewer.getViewSeq());
				// email
				resultMap.put("user-Email", loginInterviewer.getViewEmail());
				// 이름
				resultMap.put("user-Name", loginInterviewer.getViewName());
				// 핸드폰 번호
				resultMap.put("user-Phone", loginInterviewer.getViewPhone());
				// 면접관(1)인지 대기관(0)인지
				resultMap.put("user-View-Wait", loginInterviewer.getViewWait());
				// 소속 회사 Seq
				resultMap.put("user-Company-Seq", loginInterviewer.getCompanyComSeq());
				// 소속 회사 이름
				Company company = companyDao.findCompanyByComSeq(loginInterviewer.getCompanyComSeq());
				resultMap.put("user-Company-Name", company.getComName());
				// 소속 회사 로고
				resultMap.put("user-Company-Logo", company.getComLogo());
				// 소속 회사 주소
				resultMap.put("user-Company-Address", company.getComAddress());
				// 소속 회사 홈페이지 주소
				resultMap.put("user-Company-Homepage", company.getComHomepage());
				
				result.object = resultMap;
				status = HttpStatus.OK;
			}else {
				resultMap.put("message", "로그인 실패");
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		} catch (RuntimeException e) {
            logger.error("로그인 실패 : {}", e);
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
		
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

//	@GetMapping("/logout")
//	@ApiOperation(value = "로그아웃")
//	public void logout(HttpServletRequest req) {
//		
//	}
	
	@PostMapping("/signup")
	@ApiOperation(value = "가입하기")
	public Object signup(@Valid @RequestBody SignupRequest request) {
		
		ResponseEntity response = null;
		HttpStatus status = null;
		final BasicResponse result = new BasicResponse();
		
		Optional<Hr> hrEmailOpt = hrDao.findHrByHrEmail(request.getHrEmail());
		
		try {
			// 이메일 중복 확인
			if (hrEmailOpt.isPresent()) {
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			} else {
				result.status = true;
				result.data = "success";
				
				// INSERT 수행 부분
				Hr hr = new Hr();
				System.out.println(request.getHrEmail());
				// 1. 이메일 입력 -> 이메일 인증 받기(어떻게?)
				hr.setHrEmail(request.getHrEmail());
				// -----------------------------------
				// 수정 필요 -> 이메일 인증 받아서 처리하는 방식으로!
				hr.setHrCertified("N");
				// -----------------------------------
				// 2. 비밀번호 입력 -> 비밀번호 확인
				hr.setHrPassword(request.getHrPassword());
				// 3. 기업명 입력 -> 기업명으로 company table에서 seq 찾아오기
				String companyName = request.getComName();
				Company company = companyDao.findCompanyByComName(companyName);
				hr.setCompanyComSeq(company.getComSeq());
				// 4. 이름 입력
				hr.setHrName(request.getHrName());
				// 5. 번호 입력
				hr.setHrPhone(request.getHrPhone());
			
				result.object = hrDao.save(hr);
				status = HttpStatus.OK;
			}
		} catch (RuntimeException e) {
            logger.error("정보조회 실패 : {}", e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
		
		response = new ResponseEntity<>(result, status);
		return response;
		
	}
	
	// 인사팀 정보를 확인하는 Method
	@GetMapping("/mypage")
	@ApiOperation(value = "마이페이지")
    public ResponseEntity<Map<String, Object>> mypage(HttpServletRequest req) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;
        try {
        	// 토큰에 저장되어 있는 정보를 가져올 map
            resultMap.putAll(jwtService.get(req.getHeader("auth-token")));
            status = HttpStatus.OK;
        } catch (RuntimeException e) {
            logger.error("정보조회 실패 : {}", e);
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, status);
    }

	// 인사팀 정보 수정
	@PutMapping("/update/{userSeq}")
	@ApiOperation(value = "정보 수정하기")
	public ResponseEntity<Map<String, Object>> update(@RequestBody UpdateRequest updateRequest, @PathVariable("userSeq") int userSeq) {
		// 토큰에 저장되어 있는 정보를 가져올 map
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;
		
		try {
			Optional<Hr> hrOpt = hrDao.findHrByHrSeq(userSeq);
            
            // UPDATE(U) -> SELECT(R) + INSERT(C)
            // 이름, 비밀번호, 핸드폰 번호 변경만 가능하도록 구현
            hrOpt.ifPresent(selectHr -> {
            	selectHr.setHrName(updateRequest.getHrName());
            	// 이름
				resultMap.put("user-Name", updateRequest.getHrName());
            	selectHr.setHrPassword(updateRequest.getHrPassword());
            	selectHr.setHrPhone(updateRequest.getHrPhone());
            	// 핸드폰 번호
				resultMap.put("user-Phone", updateRequest.getHrPhone());
            	
            	hrDao.save(selectHr);
            });
            
            status = HttpStatus.OK;
        } catch (RuntimeException e) {
            logger.error("정보 수정 실패 : {}", e);
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
		
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
		
	}
	
	// 인사팀 정보 삭제(탈퇴)
	@DeleteMapping("/delete/{userSeq}")
	@ApiOperation(value = "정보 삭제하기")
	public void delete(@PathVariable("userSeq") int userSeq) {
		
		Map<String, Object> resultMap = new HashMap<>();
		
		try {
            Optional<Hr> hrOpt = hrDao.findHrByHrSeq(userSeq);
          
            // DELETE(D)
            hrOpt.ifPresent(selectHr -> {
            	hrDao.delete(selectHr);
            });
            
        } catch (RuntimeException e) {
            logger.error("정보 삭제 실패 : {}", e);
            resultMap.put("message", e.getMessage());
        }
		
	}
	
//	@PostMapping("/send")
//	@ApiOperation(value = "인증 메일 보내기")
//	public String sendMail(@RequestBody SignupRequest request) throws MessagingException {
//		String email=request.getHrEmail();
//		StringBuffer emailContent = new StringBuffer();
//		String certifiedKey=certified_key();
//		
//		emailContent.append("<!DOCTYPE html>");
//		emailContent.append("<html>");
//		emailContent.append("<head>");
//		emailContent.append("</head>");
//		emailContent.append("<body>");
//		emailContent.append(" <div"
//				+ "	style=\"font-family: 'Apple SD Gothic Neo', 'sans-serif' !important; width: 400px; height: 600px; border-top: 4px solid #02b875; margin: 100px auto; padding: 30px 0; box-sizing: border-box;\">"
//				+ "	<h1 style=\"margin: 0; padding: 0 5px; font-size: 28px; font-weight: 400;\">"
//				+ "		<span style=\"font-size: 15px; margin: 0 0 10px 3px;\">Test A405</span><br />"
//				+ "		<span style=\"color: #02b875\">메일인증</span> 안내입니다." + "	</h1>\n"
//				+ "	<p style=\"font-size: 16px; line-height: 26px; margin-top: 50px; padding: 0 5px;\">"
//				+"		Test A405에 가입해 주셔서 진심으로 감사드립니다.<br />"
//				+ "		아래 <b style=\"color: #02b875\">'메일 인증 번호'</b>를 입력해 주세요.<br />" + "		감사합니다.<br/>"
//				+ "	</p>" 
//				+" <h3><b style=\"color: #02b875\">"+certifiedKey+"</b></h3>" 
//				+ " </div>");
//		emailContent.append("</body>");
//		emailContent.append("</html>");
//		emailService.sendMail(email, "[Test A405 이메일 인증]", emailContent.toString());
//		
//		return certifiedKey;
//	}
	
	
	@PostMapping("/send")
	@ApiOperation(value = "인증 메일 보내기")
	public String sendMail(@Valid @RequestBody SignupRequest request) throws MessagingException {

		String email=request.getHrEmail();
		StringBuffer emailContent = new StringBuffer();
		String certifiedKey=certified_key();
		
		emailContent.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional //EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
		emailContent.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">");
		emailContent.append("<head>");
		emailContent.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n" + 
				"  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n" + 
				"  <meta name=\"x-apple-disable-message-reformatting\">\r\n" + 
				"<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\r\n" + 
				"  <title></title>\r\n" + 
				"  \r\n" + 
				"    <style type=\"text/css\">\r\n" + 
				"      a { color: #5f5f8d; text-decoration: none; }\r\n" + 
				"@media only screen and (min-width: 620px) {\r\n" + 
				"  .u-row {\r\n" + 
				"    width: 600px !important;\r\n" + 
				"  }\r\n" + 
				"  .u-row .u-col {\r\n" + 
				"    vertical-align: top;\r\n" + 
				"  }\r\n" + 
				"\r\n" + 
				"  .u-row .u-col-100 {\r\n" + 
				"    width: 600px !important;\r\n" + 
				"  }\r\n" + 
				"\r\n" + 
				"}\r\n" + 
				"\r\n" + 
				"@media (max-width: 620px) {\r\n" + 
				"  .u-row-container {\r\n" + 
				"    max-width: 100% !important;\r\n" + 
				"    padding-left: 0px !important;\r\n" + 
				"    padding-right: 0px !important;\r\n" + 
				"  }\r\n" + 
				"  .u-row .u-col {\r\n" + 
				"    min-width: 320px !important;\r\n" + 
				"    max-width: 100% !important;\r\n" + 
				"    display: block !important;\r\n" + 
				"  }\r\n" + 
				"  .u-row {\r\n" + 
				"    width: calc(100% - 40px) !important;\r\n" + 
				"  }\r\n" + 
				"  .u-col {\r\n" + 
				"    width: 100% !important;\r\n" + 
				"  }\r\n" + 
				"  .u-col > div {\r\n" + 
				"    margin: 0 auto;\r\n" + 
				"  }\r\n" + 
				"}\r\n" + 
				"body {\r\n" + 
				"  margin: 0;\r\n" + 
				"  padding: 0;\r\n" + 
				"}\r\n" + 
				"\r\n" + 
				"table,\r\n" + 
				"tr,\r\n" + 
				"td {\r\n" + 
				"  vertical-align: top;\r\n" + 
				"  border-collapse: collapse;\r\n" + 
				"}\r\n" + 
				"\r\n" + 
				"p {\r\n" + 
				"  margin: 0;\r\n" + 
				"}\r\n" + 
				"\r\n" + 
				".ie-container table,\r\n" + 
				".mso-container table {\r\n" + 
				"  table-layout: fixed;\r\n" + 
				"}\r\n" + 
				"\r\n" + 
				"* {\r\n" + 
				"  line-height: inherit;\r\n" + 
				"}\r\n" + 
				"\r\n" + 
				"a[x-apple-data-detectors='true'] {\r\n" + 
				"  color: inherit !important;\r\n" + 
				"  text-decoration: none !important;\r\n" + 
				"}\r\n" + 
				"\r\n" + 
				"</style>\r\n" + 
				"  \r\n" + 
				"  \r\n" + 
				"\r\n" + 
				"<link href=\"https://fonts.googleapis.com/css?family=Open+Sans:400,700\" rel=\"stylesheet\" type=\"text/css\">\r\n");
		emailContent.append("</head>");
		emailContent.append("<body class=\"clean-body\" style=\"margin: 0;padding: 0;-webkit-text-size-adjust: 100%;background-color: #fbfbfb\">");
		emailContent.append("  <table style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;min-width: 320px;Margin: 0 auto;background-color: #fbfbfb;width:100%\" cellpadding=\"0\" cellspacing=\"0\">\r\n" + 
				"  <tbody>\r\n" + 
				"  <tr style=\"vertical-align: top\">\r\n" + 
				"    <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top\">\r\n" + 
				"    <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td align=\"center\" style=\"background-color: #fbfbfb;\"><![endif]-->\r\n" + 
				"    \r\n" + 
				"\r\n" + 
				"<div class=\"u-row-container\" style=\"padding: 0px;background-color: #ffffff\">\r\n" + 
				"  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #ffffff;\">\r\n" + 
				"    <div style=\"border-collapse: collapse;display: table;width: 100%;background-image: url('https://ifh.cc/g/O3IRC5.jpg');background-repeat: no-repeat;background-position: center top;background-color: transparent;\">\r\n" + 
				"      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: #ffffff;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-image: url('images/image-2.png');background-repeat: no-repeat;background-position: center top;background-color: #ffffff;\"><![endif]-->\r\n" + 
				"      \r\n" + 
				"<!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\" valign=\"top\"><![endif]-->\r\n" + 
				"<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\r\n" + 
				"  <div style=\"width: 100% !important;\">\r\n" + 
				"  <!--[if (!mso)&(!IE)]><!--><div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\"><!--<![endif]-->\r\n" + 
				"  \r\n" + 
				"<table style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n" + 
				"  <tbody>\r\n" + 
				"    <tr>\r\n" + 
				"      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:11px;font-family:'Open Sans',sans-serif;\" align=\"left\">\r\n" + 
				"        \r\n" + 
				"<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\r\n" + 
				"  <tr>\r\n" + 
				"    <td style=\"padding-right: 0px;padding-left: 0px;\" align=\"center\">\r\n" + 
				"      \r\n" + 
				"      <img align=\"center\" border=\"0\" src=\"https://ifh.cc/g/VvzR6M.png\" alt=\"Image\" title=\"Image\" style=\"outline: none;text-decoration: none;-ms-interpolation-mode: bicubic;clear: both;display: inline-block !important;border: none;height: auto;float: none;width: 100%;max-width: 50px;\" width=\"50\"/>\r\n" + 
				"      \r\n" + 
				"    </td>\r\n" + 
				"  </tr>\r\n" + 
				"</table>\r\n" + 
				"\r\n" + 
				"      </td>\r\n" + 
				"    </tr>\r\n" + 
				"  </tbody>\r\n" + 
				"</table>\r\n" + 
				"\r\n" + 
				"<table style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n" + 
				"  <tbody>\r\n" + 
				"    <tr>\r\n" + 
				"      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:20px 10px 50px;font-family:'Open Sans',sans-serif;\" align=\"left\">\r\n" + 
				"        \r\n" + 
				"  <div style=\"color: #425b8d; line-height: 130%; text-align: center; word-wrap: break-word;\">\r\n" + 
				"    <p style=\"font-size: 14px; line-height: 130%; text-align: center;\">&nbsp;</p>\r\n" + 
				"<p style=\"font-size: 14px; line-height: 130%; text-align: center;\"><span style=\"font-size: 28px; line-height: 36.4px; color: #466b8c;\"><strong><span style=\"font-size: 18px; line-height: 23.4px; font-family: 'Open Sans', sans-serif;\">V<span style=\"font-size: 18px; line-height: 23.4px;\">iew Everywhere </span></span></strong></span></p>\r\n" + 
				"<p style=\"font-size: 14px; line-height: 130%; text-align: center;\"><span style=\"font-size: 44px; line-height: 57.2px; font-family: 'Open Sans', sans-serif; color: #466b8c;\"><strong>WieV</strong></span></p>\r\n" + 
				"<p style=\"font-size: 14px; line-height: 130%; text-align: center;\">&nbsp;</p>\r\n" + 
				"  </div>\r\n" + 
				"\r\n" + 
				"      </td>\r\n" + 
				"    </tr>\r\n" + 
				"  </tbody>\r\n" + 
				"</table>\r\n" + 
				"\r\n" + 
				"  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\r\n" + 
				"  </div>\r\n" + 
				"</div>\r\n" + 
				"<!--[if (mso)|(IE)]></td><![endif]-->\r\n" + 
				"      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\r\n" + 
				"    </div>\r\n" + 
				"  </div>\r\n" + 
				"</div>\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"<div class=\"u-row-container\" style=\"padding: 0px;background-color: #ffffff\">\r\n" + 
				"  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #ffffff;\">\r\n" + 
				"    <div style=\"border-collapse: collapse;display: table;width: 100%;background-color: transparent;\">\r\n" + 
				"      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: #ffffff;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-color: #ffffff;\"><![endif]-->\r\n" + 
				"      \r\n" + 
				"<!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"background-color: #ffffff;width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\" valign=\"top\"><![endif]-->\r\n" + 
				"<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\r\n" + 
				"  <div style=\"background-color: #ffffff;width: 100% !important;\">\r\n" + 
				"  <!--[if (!mso)&(!IE)]><!--><div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\"><!--<![endif]-->\r\n" + 
				"  \r\n" + 
				"<table style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n" + 
				"  <tbody>\r\n" + 
				"    <tr>\r\n" + 
				"      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:'Open Sans',sans-serif;\" align=\"left\">\r\n" + 
				"        \r\n" + 
				"  <table height=\"0px\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;border-top: 1px solid #BBBBBB;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\r\n" + 
				"    <tbody>\r\n" + 
				"      <tr style=\"vertical-align: top\">\r\n" + 
				"        <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;font-size: 0px;line-height: 0px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\r\n" + 
				"          <span>&#160;</span>\r\n" + 
				"        </td>\r\n" + 
				"      </tr>\r\n" + 
				"    </tbody>\r\n" + 
				"  </table>\r\n" + 
				"\r\n" + 
				"      </td>\r\n" + 
				"    </tr>\r\n" + 
				"  </tbody>\r\n" + 
				"</table>\r\n" + 
				"\r\n" + 
				"<table style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n" + 
				"  <tbody>\r\n" + 
				"    <tr>\r\n" + 
				"      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:35px 44px 10px;font-family:'Open Sans',sans-serif;\" align=\"left\">\r\n" + 
				"        \r\n" + 
				"  <div style=\"color: #34495e; line-height: 140%; text-align: left; word-wrap: break-word;\">\r\n" + 
				"    <p style=\"font-size: 14px; line-height: 140%;\"><span style=\"color: #4d6381; font-size: 14px; line-height: 19.6px;\"><strong><span style=\"font-size: 22px; line-height: 30.8px;\"><span style=\"color: #466b8c; font-size: 22px; line-height: 30.8px;\">"
				+"</span></span><span style=\"font-size: 22px; line-height: 30.8px; color: #3b7df0;\">메일 인증</span>"
				+ "<span style=\"font-size: 22px; line-height: 30.8px; color: #466b8c;\"> 안내입니다.</span><br /><br />"
				+ "</strong></span></p>\r\n" + 
				"  </div>\r\n" + 
				"\r\n" + 
				"      </td>\r\n" + 
				"    </tr>\r\n" + 
				"  </tbody>\r\n" + 
				"</table>\r\n" + 
				"\r\n" + 
				"<table style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n" + 
				"  <tbody>\r\n" + 
				"    <tr>\r\n" + 
				"      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px 44px 22px;font-family:'Open Sans',sans-serif;\" align=\"left\">\r\n" + 
				"        \r\n" + 
				"  <div style=\"color: #000000; line-height: 190%; text-align: left; word-wrap: break-word;\">\r\n" + 
				"  <p style=\"font-size: 14px; line-height: 190%;\">WieV에 가입해 주셔서 진심으로 감사드립니다.<br />아래<strong><span style=\"color: #363d5a; font-size: 14px; line-height: 26.6px;\\\">'이메일 인증 번호'</span></strong>를 사이트에 입력해 주세요.<br />감사합니다.</p>\r\n" +
				"  <br/><strong><span style=\"color: #363d5a; font-size: 14px; line-height: 26.6px;\">"+certifiedKey+"</span></strong>" + 
				"  </div>\r\n" + 
				"\r\n" + 
				"      </td>\r\n" + 
				"    </tr>\r\n" + 
				"  </tbody>\r\n" + 
				"</table>\r\n" + 
				"\r\n" + 
				"  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\r\n" + 
				"  </div>\r\n" + 
				"</div>\r\n" + 
				"<!--[if (mso)|(IE)]></td><![endif]-->\r\n" + 
				"      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\r\n" + 
				"    </div>\r\n" + 
				"  </div>\r\n" + 
				"</div>\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\r\n" + 
				"  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #ffffff;\">\r\n" + 
				"    <div style=\"border-collapse: collapse;display: table;width: 100%;background-color: transparent;\">\r\n" + 
				"      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-color: #ffffff;\"><![endif]-->\r\n" + 
				"      \r\n" + 
				"<!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\" valign=\"top\"><![endif]-->\r\n" + 
				"<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\r\n" + 
				"  <div style=\"width: 100% !important;\">\r\n" + 
				"  <!--[if (!mso)&(!IE)]><!--><div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\"><!--<![endif]-->\r\n" + 
				"  \r\n" + 
				"<table style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n" + 
				"  <tbody>\r\n" + 
				"    <tr>\r\n" + 
				"      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:'Open Sans',sans-serif;\" align=\"left\">\r\n" + 
				"        \r\n" + 
				"  <table height=\"0px\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;border-top: 1px solid #BBBBBB;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\r\n" + 
				"    <tbody>\r\n" + 
				"      <tr style=\"vertical-align: top\">\r\n" + 
				"        <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;font-size: 0px;line-height: 0px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\r\n" + 
				"          <span>&#160;</span>\r\n" + 
				"        </td>\r\n" + 
				"      </tr>\r\n" + 
				"    </tbody>\r\n" + 
				"  </table>\r\n" + 
				"\r\n" + 
				"      </td>\r\n" + 
				"    </tr>\r\n" + 
				"  </tbody>\r\n" + 
				"</table>\r\n" + 
				"\r\n" + 
				"  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\r\n" + 
				"  </div>\r\n" + 
				"</div>\r\n" + 
				"<!--[if (mso)|(IE)]></td><![endif]-->\r\n" + 
				"      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\r\n" + 
				"    </div>\r\n" + 
				"  </div>\r\n" + 
				"</div>\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\r\n" + 
				"  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">\r\n" + 
				"    <div style=\"border-collapse: collapse;display: table;width: 100%;background-color: transparent;\">\r\n" + 
				"      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-color: transparent;\"><![endif]-->\r\n" + 
				"      \r\n" + 
				"<!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\" valign=\"top\"><![endif]-->\r\n" + 
				"<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\r\n" + 
				"  <div style=\"width: 100% !important;\">\r\n" + 
				"  <!--[if (!mso)&(!IE)]><!--><div style=\"padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\"><!--<![endif]-->\r\n" + 
				"  \r\n" + 
				"<table style=\"font-family:'Open Sans',sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\r\n" + 
				"  <tbody>\r\n" + 
				"    <tr>\r\n" + 
				"      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:12px 10px;font-family:'Open Sans',sans-serif;\" align=\"left\">\r\n" + 
				"        \r\n" + 
				"  <div style=\"color: #ffffff; line-height: 140%; text-align: left; word-wrap: break-word;\">\r\n" + 
				"    <p style=\"font-size: 14px; line-height: 140%; text-align: center;\"><span style=\"color: #466b8c; font-size: 14px; line-height: 19.6px;\">&copy; SSAFY Company. All Rights Reserved</span></p>\r\n" + 
				"<p style=\"font-size: 14px; line-height: 140%; text-align: center;\"><span style=\"color: #466b8c; font-size: 14px; line-height: 19.6px;\">Talk to us (010) 1212-3333 or </span><br /><span style=\"color: #466b8c; font-size: 14px; line-height: 19.6px;\">Email us ssafytesta405@gmail.com</span></p>\r\n" + 
				"  </div>\r\n" + 
				"\r\n" + 
				"      </td>\r\n" + 
				"    </tr>\r\n" + 
				"  </tbody>\r\n" + 
				"</table>\r\n" + 
				"\r\n" + 
				"  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\r\n" + 
				"  </div>\r\n" + 
				"</div>\r\n" + 
				"<!--[if (mso)|(IE)]></td><![endif]-->\r\n" + 
				"      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\r\n" + 
				"    </div>\r\n" + 
				"  </div>\r\n" + 
				"</div>\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"    <!--[if (mso)|(IE)]></td></tr></table><![endif]-->\r\n" + 
				"    </td>\r\n" + 
				"  </tr>\r\n" + 
				"  </tbody>\r\n" + 
				"  </table>\r\n" + 
				"  <!--[if mso]></div><![endif]-->\r\n" + 
				"  <!--[if IE]></div><![endif]-->");
		emailContent.append("</body>");
		emailContent.append("</html>");

		
		emailService.sendMail(email, "[WieV]이메일 인증 번호 안내", emailContent.toString());
		
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
	
	
}