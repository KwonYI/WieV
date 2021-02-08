package com.web.project.model.recruit;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Certificate {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int cerSeq;
	
	private String cerName;
	private String cerGrade;
	private String cerDate;
	private int applicantApplySeq;
}
