package com.web.project.model.interview;

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
public class TypeInterviewer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int typeViewerSeq;

	private int interviewerViewSeq;
	private int groupTypeGroupTypeSeq;
	private int groupTypeInterviewTypeTypeSeq;
	private int interviewerCareerCaSeq;
	private int interviewerCareerPartPartSeq;
	
}
