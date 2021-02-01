package com.web.project.model.interview;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Interviewer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int viewSeq;

	private String viewName;
	private String viewEmail;
	private String viewPassword;
	private String viewPhone;
	private int viewWait;
	private int viewAssigned;

	private int companyComSeq;
	private int careerCaSeq;
	private int careerPartPartSeq;
	
}
