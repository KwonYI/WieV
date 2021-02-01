package com.web.project.controller.recruit;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.project.dao.recruit.RecruitDao;
import com.web.project.model.BasicResponse;
import com.web.project.model.recruit.Applicant;
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
@RequestMapping("/recuit")
public class RecruitController {//공고 등록

	@Autowired
	RecruitDao recruitDao;

	
	@GetMapping(value="/getList")
	@ApiOperation(value = "공고리스트 모두 가져오기")
	public ResponseEntity<List<Recruit>> getRecruitList(){
		return new ResponseEntity<List<Recruit>>(recruitDao.findAll(), HttpStatus.OK);
	}
	
	@PostMapping("/register")
	@ApiOperation(value = "공고등록")
	public Object recruitRegister(@Valid @RequestBody RegisterRequest request) {
		
		ResponseEntity response = null;

			final BasicResponse result = new BasicResponse();
			result.status = true;
			result.data = "success";
			
			// Recruit INSERT 수행 부분
			Recruit recruit = new Recruit();
			recruit.setReYear(request.getReYear());
			recruit.setReFlag(request.getReFlag());
			recruit.setReStatus(request.getReStatus());
			recruit.setReStartdate(request.getReStartdate());
			recruit.setReEnddate(request.getReEnddate());
			
			//외래키 나중에 수정하기
			recruit.setCompanyComSeq(1);
			result.object=recruitDao.save(recruit);
			
			response = new ResponseEntity<>(result, HttpStatus.OK);

		return response;
	}
}