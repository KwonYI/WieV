package com.web.project.model.recruit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Applicant {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int applySeq;

	private String applyId;
	private String applyName;
	private String applyEmail;
	private String applyBirth;
	private String applyPhone;
	private String applyUniversity;
	private String applyMajor;
	private Double applyGrade;
	private String applyResume1;
	private String applyResume2;
	private String applyResume3;
	private String applyResume4;
	private int applyAssigned;

	private int careerCaSeq;
	private int recruitReSeq;

}
