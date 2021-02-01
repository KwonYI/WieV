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
public class ApplicantGroup {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int applyGroupSeq;
	
	private int applicantApplySeq;
	private int applicantCareerCaSeq;
	private int applicantRecruitReSeq;
	private int groupDetailDetailSeq;
	private int groupDetailGroupGroupSeq;
}
