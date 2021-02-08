package com.web.project.model.hr;

import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Valid
@ToString
public class FindPasswordRequest {

	@ApiModelProperty(required = true)
	@NotNull
	String hrName;
	
	@ApiModelProperty(required = true)
	@NotNull
	String hrPhone;
	
	@ApiModelProperty(required = true)
	@NotNull
	String hrEmail;
	

	public String getHrName() {
		return hrName;
	}
	public void setHrName(String hrName) {
		this.hrName = hrName;
	}
	public String getHrPhone() {
		return hrPhone;
	}
	public void setHrPhone(String hrPhone) {
		this.hrPhone = hrPhone;
	}
	public String getHrEmail() {
		return hrEmail;
	}
	public void setHrEmail(String hrEmail) {
		this.hrEmail = hrEmail;
	}
	
}

