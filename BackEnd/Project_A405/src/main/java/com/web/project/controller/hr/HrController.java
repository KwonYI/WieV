package com.web.project.controller.hr;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.web.project.dao.hr.CompanyDao;
import com.web.project.dao.hr.HrDao;
import com.web.project.dao.interview.InterviewerDao;
import com.web.project.model.BasicResponse;
import com.web.project.model.hr.SignupRequest;
import com.web.project.model.interview.Interviewer;
import com.web.project.model.hr.Company;
import com.web.project.model.hr.Hr;
import com.web.project.service.hr.JwtService;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
	
	public static final Logger logger = LoggerFactory.getLogger(HrController.class);
	
	@PostMapping("/login")
	@ApiOperation(value = "로그인")
	public ResponseEntity<Map<String, Object>> login(@RequestParam(required = true) final String email,
			@RequestParam(required = true) final String password) {
		
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;

		Optional<Hr> hrOpt = hrDao.findHrByHrEmailAndHrPassword(email, password);
		Optional<Interviewer> interviewerOpt = interviewerDao.findOptionalInterviewerByViewEmailAndViewPassword(email, password);
		
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
				// email
				resultMap.put("hr-Email", loginHr.getHrEmail());
				// 이름
				resultMap.put("hr-Name", loginHr.getHrName());
				// 핸드폰 번호
				resultMap.put("hr-Phone", loginHr.getHrPhone());
				// 계정 생성 날짜
				resultMap.put("hr-Create-Date", loginHr.getHrCreateDate());
				// 소속 회사 Seq
				resultMap.put("hr-Company-Seq", loginHr.getCompanyComSeq());
				// 소속 회사 이름
				Company company = companyDao.findCompanyByComSeq(loginHr.getCompanyComSeq());
				resultMap.put("hr-Company-Name", company.getComName());
				// 소속 회사 로고
				resultMap.put("hr-Company-Logo", company.getComLogo());
				// 소속 회사 주소
				resultMap.put("hr-Company-Address", company.getComAddress());
				// 소속 회사 홈페이지 주소
				resultMap.put("hr-Company-Homepage", company.getComHomepage());
				
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
				// email
				resultMap.put("interviewer-Email", loginInterviewer.getViewEmail());
				// 이름
				resultMap.put("interviewer-Name", loginInterviewer.getViewName());
				// 핸드폰 번호
				resultMap.put("interviewer-Phone", loginInterviewer.getViewPhone());
				// 면접관(1)인지 대기관(0)인지
				resultMap.put("interviewer-view-wait", loginInterviewer.getViewWait());
				// 소속 회사 Seq
				resultMap.put("interviewer-Company-Seq", loginInterviewer.getCompanyComSeq());
				// 소속 회사 이름
				Company company = companyDao.findCompanyByComSeq(loginInterviewer.getCompanyComSeq());
				resultMap.put("interviewer-Company-Name", company.getComName());
				// 소속 회사 로고
				resultMap.put("interviewer-Company-Logo", company.getComLogo());
				// 소속 회사 주소
				resultMap.put("interviewer-Company-Address", company.getComAddress());
				// 소속 회사 홈페이지 주소
				resultMap.put("interviewer-Company-Homepage", company.getComHomepage());
				
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
	@PutMapping("/update")
	@ApiOperation(value = "정보 수정하기")
	public Object update(@RequestBody Hr hr, HttpServletRequest req) {
		
		ResponseEntity response = null;
		// 토큰에 저장되어 있는 정보를 가져올 map
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		
		// 하나의 요청에서 HttpServletRequest 객체가 소멸하기 까지 상태정보를 유지하고자 할 때, 
		// 한번의 요청으로 실행된 페이지끼리 정보를 공유하고자 할 때 사용되며, 
		// 디스패처에 의한 요청재지정을 하기 전 HttpServletRequest 객체의 setAttribute( ) 메소드로 데이터를 등록하고 
		// 요청 재지정으로 HttpServletRequest 객체가 전달된 페이지에서 getAttribute( ) 메소드로 추출할 수 있다.
		
		try {
        	// 토큰에 저장되어 있는 정보를 가져올 map
            resultMap.putAll(jwtService.get(req.getHeader("auth-token")));
            
            Optional<Hr> hrEmailOpt = hrDao.findHrByHrEmail(resultMap.get("hr-Email").toString());
            
            // UPDATE(U) -> SELECT(R) + INSERT(C)
            // 이름, 비밀번호, 핸드폰 번호 변경만 가능하도록 구현
            hrEmailOpt.ifPresent(selectHr -> {
            	selectHr.setHrName(hr.getHrName());
            	selectHr.setHrPassword(hr.getHrPassword());
            	selectHr.setHrPhone(hr.getHrPhone());
            	
            	hrDao.save(selectHr);
            });
            
            status = HttpStatus.OK;
        } catch (RuntimeException e) {
            logger.error("정보조회 실패 : {}", e);
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
		
		response = new ResponseEntity<>("수정 완료", status);
		return response;
		
	}
	
	// 인사팀 정보 삭제(탈퇴)
	@DeleteMapping("/delete")
	@ApiOperation(value = "정보 삭제하기")
	public void delete(HttpServletRequest req) {
		
		Map<String, Object> resultMap = new HashMap<>();
		
		try {
        	// 토큰에 저장되어 있는 정보를 가져올 map
            resultMap.putAll(jwtService.get(req.getHeader("auth-token")));
            
            Optional<Hr> hrEmailOpt = hrDao.findHrByHrEmail(resultMap.get("hr-Email").toString());
            
            // DELETE(D)
            hrEmailOpt.ifPresent(selectHr -> {
            	hrDao.delete(selectHr);
            });
            
        } catch (RuntimeException e) {
            logger.error("정보 삭제 실패 : {}", e);
            resultMap.put("message", e.getMessage());
        }
		
	}
	
	
}