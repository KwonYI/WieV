package com.web.project.model.group;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupInfo {

	// 그룹 Seq
	private int groupSeq;
	// 그룹 면접 실시 날짜
	private String groupDate;
	// 그룹 면접 시작 시간
	private String groupStartTime;
	// 그룹 부서
	private String groupPart;
	// 그룹 직무
	private String groupCareer;
	// 면접 유형 목록
	private List<String> interviewTypeList = new ArrayList<String>();
	// 그룹 지원자 목록
	private List<String> intervieweeList = new ArrayList<String>();
	
	// 그룹 대기관 목록 -> 면접 유형, 대기관 이름
	private List<Map<String, Object>> waitInterviewerList = new ArrayList<Map<String, Object>>();
	// 그룹 면접관 목록 -> 면접 유형, 면접관 이름
	private List<Map<String, Object>> InterviewerList = new ArrayList<Map<String, Object>>();
	
	// 각 세부 그룹 정보 -> 세부 그룹 seq, 세부 그룹 면접 순서, 지원자 목록
	private List<Map<String, Object>> groupDetailList = new ArrayList<Map<String,Object>>();
	
	
}
