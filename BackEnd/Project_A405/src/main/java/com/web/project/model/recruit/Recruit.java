package com.web.project.model.recruit;


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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Recruit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int reSeq;

	private int reYear;
	private String reFlag;
	private String reStatus;
	private String reStartDate;
	private String reEndDate;

	private int companyComSeq;
}
