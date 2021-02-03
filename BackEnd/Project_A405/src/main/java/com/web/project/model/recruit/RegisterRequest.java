package com.web.project.model.recruit;


import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Valid
@ToString
//공고등록 request
public class RegisterRequest {

	@ApiModelProperty(required = true)
	@NotNull
	int reYear;
	@ApiModelProperty(required = true)
	@NotNull
	String reFlag;
	@ApiModelProperty(required = true)
	@NotNull
	String reStatus;
	@ApiModelProperty(required = true)
	@NotNull
	String reStartDate;
	@ApiModelProperty(required = true)
	@NotNull
	String reEndDate;
	
	public int getReYear() {
		return reYear;
	}
	public void setReYear(int reYear) {
		this.reYear = reYear;
	}
	public String getReFlag() {
		return reFlag;
	}
	public void setReFlag(String reFlag) {
		this.reFlag = reFlag;
	}
	public String getReStatus() {
		return reStatus;
	}
	public void setReStatus(String reStatus) {
		this.reStatus = reStatus;
	}
	public String getReStartDate() {
		return reStartDate;
	}
	public void setReStartDate(String reStartDate) {
		this.reStartDate = reStartDate;
	}
	public String getReEndDate() {
		return reEndDate;
	}
	public void setReEndDate(String reEndDate) {
		this.reEndDate = reEndDate;
	}
	

}
