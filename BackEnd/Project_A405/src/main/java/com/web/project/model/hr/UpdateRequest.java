package com.web.project.model.hr;

import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Valid
@ToString
public class UpdateRequest {
	@ApiModelProperty(required = true)
	@NotNull
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d$@$!%*#?&]{8,}$")
	String hrPassword;
	@ApiModelProperty(required = true)
	@NotNull
	String hrName;
	@ApiModelProperty(required = true)
	@NotNull
	String hrPhone;
	
	public String getHrPassword() {
		return hrPassword;
	}
	public void setHrPassword(String hrPassword) {
		this.hrPassword = hrPassword;
	}
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
	
}
