package com.web.project.model.group;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupAll {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int groupSeq;
	
	private String groupDate;
	private String groupStartTime;
	private int groupDivide;
	private int groupVisible;
	private int groupTypeCnt;
	private int interviewerDivide;
	// 외래키
	private int recruitReSeq;
	private int careerCaSeq;
	
}
