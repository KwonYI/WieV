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
	String reStartdate;
	@ApiModelProperty(required = true)
	@NotNull
	String reEnddate;
	
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
	public String getReStartdate() {
		return reStartdate;
	}
	public void setReStartdate(String reStartdate) {
		this.reStartdate = reStartdate;
	}
	public String getReEnddate() {
		return reEnddate;
	}
	public void setReEnddate(String reEnddate) {
		this.reEnddate = reEnddate;
	}

}
