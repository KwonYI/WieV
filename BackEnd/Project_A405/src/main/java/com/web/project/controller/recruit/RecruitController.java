package com.web.project.controller.recruit;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
	
	@PostMapping("/register")
	@ApiOperation(value = "공고등록")
	public ResponseEntity<Recruit> recruitRegister(@Valid @RequestBody RegisterRequest RegisterRequest, int comSeq) {
		HttpStatus status = null;
		Recruit recruit = null;

		try {
			// Recruit INSERT 수행 부분
			Recruit recruitTemp = new Recruit();
			recruitTemp.setReYear(RegisterRequest.getReYear());
			recruitTemp.setReFlag(RegisterRequest.getReFlag());
			recruitTemp.setReStatus(RegisterRequest.getReStatus());
			recruitTemp.setReStartdate(RegisterRequest.getReStartdate());
			recruitTemp.setReEnddate(RegisterRequest.getReEnddate());
			
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
}