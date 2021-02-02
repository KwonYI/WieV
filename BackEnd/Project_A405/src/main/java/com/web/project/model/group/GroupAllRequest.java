package com.web.project.model.group;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.web.project.model.interview.InterviewType;

import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

@Valid
@ToString
public class GroupAllRequest {
	// 부서, 직무, 시작 시간, 종료 시간, 한 그룹에 들어갈 명수, 소요 시간, 면접관수
	@ApiModelProperty(required = true)
	@NotNull
	private String part;
	@ApiModelProperty(required = true)
	@NotNull
	private String career;
	@ApiModelProperty(required = true)
	@NotNull
	private List<InterviewType> interviewTypeList;
	// 시작 시간이랑 끝 시간 포멧 맞춰야된다.
	// 1시간 단위로 생각했음
	@ApiModelProperty(required = true)
	@NotNull
	private String startTime;
	@ApiModelProperty(required = true)
	@NotNull
	private String endTime;
	@ApiModelProperty(required = true)
	@NotNull
	private int divideNumber;
	@ApiModelProperty(required = true)
	@NotNull
	private int divideTime;
	@ApiModelProperty(required = true)
	@NotNull
	private int divideVisible;
	
	@ApiModelProperty(required = true)
	@NotNull
	private int divideInterviewer;
	
	@ApiModelProperty(required = true)
	@NotNull
	private int divideDetailNum;
	
	public String getPart() {
		return part;
	}

	public void setPart(String part) {
		this.part = part;
	}

	public String getCareer() {
		return career;
	}

	public void setCareer(String career) {
		this.career = career;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getDivideNumber() {
		return divideNumber;
	}

	public void setDivideNumber(int divideNumber) {
		this.divideNumber = divideNumber;
	}

	public int getDivideTime() {
		return divideTime;
	}

	public void setDivideTime(int divideTime) {
		this.divideTime = divideTime;
	}

	public int getDivideVisible() {
		return divideVisible;
	}

	public void setDivideVisible(int divideVisible) {
		this.divideVisible = divideVisible;
	}

	public List<InterviewType> getInterviewTypeList() {
		return interviewTypeList;
	}

	public void setInterviewTypeList(List<InterviewType> interviewTypeList) {
		this.interviewTypeList = interviewTypeList;
	}

	public int getDivideInterviewer() {
		return divideInterviewer;
	}

	public void setDivideInterviewer(int divideInterviewer) {
		this.divideInterviewer = divideInterviewer;
	}

	public int getDivideDetailNum() {
		return divideDetailNum;
	}

	public void setDivideDetailNum(int divideDetailNum) {
		this.divideDetailNum = divideDetailNum;
	}
}
