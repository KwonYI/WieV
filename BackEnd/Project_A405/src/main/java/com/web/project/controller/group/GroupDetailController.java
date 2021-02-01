package com.web.project.controller.group;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.web.project.controller.hr.HrController;
import com.web.project.dao.group.DetailOrderDao;
import com.web.project.dao.group.GroupAllDao;
import com.web.project.dao.group.GroupDetailDao;
import com.web.project.dao.group.GroupTypeDao;
import com.web.project.model.BasicResponse;
import com.web.project.model.group.DetailOrder;
import com.web.project.model.group.GroupDetail;
import com.web.project.model.group.GroupType;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
		@ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
		@ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
		@ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })
@CrossOrigin(origins = { "http://localhost:3000" }) 
@RestController
@RequestMapping("/groupDetail")
public class GroupDetailController {

	@Autowired
	GroupAllDao groupAllDao;
	
	@Autowired
	GroupDetailDao groupDetailDao;
	
	@Autowired
	DetailOrderDao detailOrderDao;
	
	@Autowired
	GroupTypeDao groupTypeDao;
	
	public static final Logger logger = LoggerFactory.getLogger(HrController.class);
	
	@PostMapping("/divide/{divideNum}")
	@ApiOperation(value = "세부 그룹 인원수로 세부 그룹 나누기")
	public Object divideDetail(@PathVariable("divideNum") int divideNum, int groupSeq) {
		HttpStatus status = null;
		GroupDetail groupDetail = null;
		
		try {
			// 1. 각 그룹의 인원 수를 가져온다.
			int groupAllNum = groupAllDao.findGroupAllByGroupSeq(groupSeq).getGroupDivide();
			
			// 2. 각 세부 그룹으로 나눠준다. => 나머지 인원에 대해서 생각 X
			int detailGroupCount = groupAllNum / divideNum;
			
			// 3. 세부 그룹에 인원을 부여한다.
			for (int i = 0; i < detailGroupCount; i++) {
				groupDetail = new GroupDetail();
				
				groupDetail.setDetailDivide(divideNum);
				groupDetail.setGroupGroupSeq(groupSeq);
				
				groupDetailDao.save(groupDetail);
			}
			
			// 3. 나머지 인원은 따로 처리해야 된다.
			if(groupAllNum % divideNum != 0) {
				int remainderNum = groupAllNum - (detailGroupCount * divideNum);
				
				groupDetail = new GroupDetail();
				
				groupDetail.setDetailDivide(remainderNum);
				groupDetail.setGroupGroupSeq(groupSeq);
				
				groupDetailDao.save(groupDetail);
			}
			
			status = HttpStatus.OK;
		} catch (RuntimeException e) {
			logger.error("세부 그룹 등록 실패", e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		return new ResponseEntity<> ("세부 그룹 등록 완료", status);
	}
	
	@GetMapping("/calculate/divideCount/{divideNum}")
	@ApiOperation(value = "세부 그룹이 몇 개인지 확인하기")
	public Object getDivideCount(@PathVariable("divideNum") int divideNum, int groupSeq) {
		int divideCount = 0;
		HttpStatus status = null;
		
		try {
			// 그룹의 인원을 세부 그룹의 인원으로 나눠줌
			// 1. 각 그룹의 인원 수를 가져온다.
			int groupAllNum = groupAllDao.findGroupAllByGroupSeq(groupSeq).getGroupDivide();
			
			// 2. 각 세부 그룹으로 나눠준다.
			divideCount = groupAllNum / divideNum;
			
			// 3. 남은 인원이 있는지 확인해준다.
			if(groupAllNum % divideNum != 0) divideCount++;
			
			status = HttpStatus.OK;
		} catch (RuntimeException e) {
			logger.error("세부 그룹 개수 계산 실패", e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		return new ResponseEntity<Integer> (divideCount, status);
	}
	
	@PostMapping("/allocate/detailOrder/{groupSeq}")
	@ApiOperation(value = "세부 그룹에 순서 부여하기")
	public Object allocateDetailOrder(@PathVariable("groupSeq") int groupSeq) {
		HttpStatus status = null;
		
		try {
			// 1. 각 그룹의 면접 리스트, 세부 그룹 리스트를 구해준다.
			List<GroupDetail> groupDetailList = groupDetailDao.findListGroupDetailByGroupGroupSeq(groupSeq);
			List<GroupType> groupTypeList = groupTypeDao.findListGroupTypeByGroupGroupSeq(groupSeq);
			
			// 2. 면접 개수, 세부 그룹 개수를 구해준다.
			int groupDetailCount = groupDetailList.size();
			int interviewCount = groupTypeList.size();
			
			// 3. 순서를 부여하기 위한 Queue 생성
			Queue<Integer> sequenceQ = new LinkedList<Integer>();
			for (int i = 1; i <= interviewCount; i++) {
				sequenceQ.offer(i);
			}
			
			// 4. 세부 그룹의 순서를 부여한다.
			for (int i = 0; i < groupDetailCount; i++) {
				GroupDetail groupDetail = groupDetailList.get(i);
				
				// 5. 세부 그룹 순서 저장
				for (int j = 0; j < interviewCount; j++) {
					GroupType groupType = groupTypeList.get(j);
					
					DetailOrder detailOrder = new DetailOrder();
					
					// 외래키 설정
					detailOrder.setGroupDetailDetailSeq(groupDetail.getDetailSeq());
					detailOrder.setGroupTypeGroupTypeSeq(groupType.getGroupTypeSeq());
					detailOrder.setGroupTypeInterviewTypeTypeSeq(groupType.getInterviewTypeTypeSeq());
					
					// 순서 설정
					int order = sequenceQ.poll();
					detailOrder.setTrueOrder(order);
					sequenceQ.offer(order);
					
					detailOrderDao.save(detailOrder);
				}
				
				// 6. 순서 조정
				sequenceQ.offer(sequenceQ.poll());
			}
			
			status = HttpStatus.OK;
		} catch (RuntimeException e) {
			logger.error("세부 그룹 순서 부여 실패", e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		return new ResponseEntity<> ("순서 부여 완료", status);
	}
	
	@GetMapping("/show/detailOrder/{groupSeq}")
	@ApiOperation(value = "세부 그룹의 순서 확인하기")
	public ResponseEntity<List<DetailOrder>> getDetailOrder(@PathVariable("groupSeq") int groupSeq) {
		HttpStatus status = null;
		List<DetailOrder> totalDetailOrderList = new ArrayList<DetailOrder>();
		
		try {
			// 1. 그룹에 해당하는 세부 그룹들을 찾는다. => 세부 그룹의 seq를 찾을 수 있다.
			List<GroupDetail> groupDetailSeqList = groupDetailDao.findListGroupDetailByGroupGroupSeq(groupSeq);
			
			// 2. 각 세부 그룹의 면접 유형들을 찾는다. => 세부 그룹 면접 순서의 seq를 찾을 수 있다.
			for (int i = 0; i < groupDetailSeqList.size(); i++) {
				List<DetailOrder> detailOrderList = detailOrderDao.findListDetailOrderByGroupDetailDetailSeq(groupDetailSeqList.get(i).getDetailSeq());
				
				// 3. 각 면접 유형을 true order를 통해 정렬한다.
				Collections.sort(detailOrderList, (o1 , o2) -> {
					return o1.getTrueOrder() - o2.getTrueOrder();
				});
				
				// 4. 모든 세부 그룹의 순서를 넣는다.
				for (int j = 0; j < detailOrderList.size(); j++) {
					totalDetailOrderList.add(detailOrderList.get(j));
				}
			}
			
			status = HttpStatus.OK;
		} catch (RuntimeException e) {
			logger.error("세부 그룹 조회 실패", e);
            status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		return new ResponseEntity<List<DetailOrder>> (totalDetailOrderList, status);
	}
	
}
