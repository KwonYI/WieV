package com.web.project.controller.recruit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.project.controller.hr.HrController;
import com.web.project.dao.hr.CompanyDao;
import com.web.project.dao.recruit.RecruitDao;
import com.web.project.model.BasicResponse;
import com.web.project.model.hr.Company;
import com.web.project.model.hr.Hr;
import com.web.project.model.recruit.Recruit;
import com.web.project.model.recruit.RegisterRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
		@ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
		@ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
		@ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })
@CrossOrigin(origins = { "http://localhost:3000" })
@RestController
@RequestMapping("/recruit")
public class RecruitController {//공고 등록

	@Autowired
	RecruitDao recruitDao;
	
	@Autowired
	CompanyDao companyDao;

	public static final Logger logger = LoggerFactory.getLogger(HrController.class);
	
	@GetMapping(value="/getList/{comSeq}")
	@ApiOperation(value = "공고리스트 모두 가져오기")
	public ResponseEntity<List<Recruit>> getRecruitList(@PathVariable("comSeq") int comSeq){
		HttpStatus status = null;
		List<Recruit> recruitList = null;
				
		try {
			recruitList = recruitDao.findListRecruitByCompanyComSeq(comSeq);
			
			if(recruitList != null) {
				status = HttpStatus.OK;
			}else {
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		} catch (RuntimeException e) {
			logger.error("공고리스트 가져오기 실패", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		return new ResponseEntity<List<Recruit>>(recruitList, status);
	}
	
	// 공고 삭제
	@DeleteMapping("/delete/{reSeq}")
	@ApiOperation(value = "공고 삭제하기")
	public Object delete(@PathVariable("reSeq") int reSeq) {
		HttpStatus status = null;
		String s = null;
		
		System.out.println("delete");
		
		try {
            Optional<Recruit> recruitOpt=recruitDao.findOptionalRecruitByReSeq(reSeq);
            // DELETE(D)
            recruitOpt.ifPresent(selectRecruit -> {
            	recruitDao.delete(selectRecruit);
            });
            s = "Success";
            status = HttpStatus.OK;
        } catch (RuntimeException e) {
            logger.error("정보 삭제 실패 ", e);
            s = "Failure";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
		
		return new ResponseEntity<String> (s, status);
	}
	
	
	@PostMapping("/register/{comSeq}")
	@ApiOperation(value = "공고등록")
	public ResponseEntity<Recruit> recruitRegister(@PathVariable("comSeq") int comSeq, @Valid @RequestBody RegisterRequest registerRequest) {
		HttpStatus status = null;
		Recruit recruit = null;
		
		System.out.println(registerRequest);

		try {
			// Recruit INSERT 수행 부분
			Recruit recruitTemp = new Recruit();
			recruitTemp.setReYear(registerRequest.getReYear());
			recruitTemp.setReFlag(registerRequest.getReFlag());
			recruitTemp.setReStatus(registerRequest.getReStatus());			
			recruitTemp.setReStartDate(registerRequest.getReStartDate());
			recruitTemp.setReEndDate(registerRequest.getReEndDate());
			
			// 외래키
			recruitTemp.setCompanyComSeq(comSeq);

			recruit = recruitDao.save(recruitTemp);
			
			status = HttpStatus.OK;
		} catch (RuntimeException e) {
			logger.error("공고 등록 실패", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return new ResponseEntity<Recruit> (recruit, status);
	}
	
	@GetMapping(value="/companyNameList")
	@ApiOperation(value = "회사리스트 이름 모두 가져오기")
	public ResponseEntity<List<String>> getCompanyNameList(){
		HttpStatus status = null;
		List<String> companyList = new ArrayList<String>();
				
		try {
			List<Company> comList = companyDao.findAll();
			
			for (int i = 0; i < comList.size(); i++) {
				companyList.add(comList.get(i).getComName());
			}
			
			status = HttpStatus.OK;
		} catch (RuntimeException e) {
			logger.error("회사리스트 가져오기 실패", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		return new ResponseEntity<List<String>>(companyList, status);
	}
	
	@GetMapping(value="/companyList")
	@ApiOperation(value = "회사리스트 모두 가져오기")
	public ResponseEntity<List<Company>> getCompanyList(){
		HttpStatus status = null;
		List<Company> companyList = null;
				
		try {
			companyList = companyDao.findAll();
			status = HttpStatus.OK;
		} catch (RuntimeException e) {
			logger.error("회사리스트 가져오기 실패", e);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		return new ResponseEntity<List<Company>>(companyList, status);
	}
}