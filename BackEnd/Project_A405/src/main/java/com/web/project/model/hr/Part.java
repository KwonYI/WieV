package com.web.project.model.hr;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Part {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int partSeq;
	
	private String partName;
	
	private int companyComSeq;

	public int getPartSeq() {
		return partSeq;
	}

	public void setPartSeq(int partSeq) {
		this.partSeq = partSeq;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public int getCompanyComSeq() {
		return companyComSeq;
	}

	public void setCompanyComSeq(int companyComSeq) {
		this.companyComSeq = companyComSeq;
	}
}
