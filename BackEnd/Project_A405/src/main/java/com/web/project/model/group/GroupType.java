package com.web.project.model.group;

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
public class GroupType {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int groupTypeSeq;

	private int interviewTypeTypeSeq;
	private int groupGroupSeq;

	private String waitSessionName;
	private String interviewSessionName;

	public int getGroupTypeSeq() {
		return groupTypeSeq;
	}

	public void setGroupTypeSeq(int groupTypeSeq) {
		this.groupTypeSeq = groupTypeSeq;
	}

	public int getInterviewTypeTypeSeq() {
		return interviewTypeTypeSeq;
	}

	public void setInterviewTypeTypeSeq(int interviewTypeTypeSeq) {
		this.interviewTypeTypeSeq = interviewTypeTypeSeq;
	}

	public int getGroupGroupSeq() {
		return groupGroupSeq;
	}

	public void setGroupGroupSeq(int groupGroupSeq) {
		this.groupGroupSeq = groupGroupSeq;
	}

	public String getWaitSessionName() {
		return waitSessionName;
	}

	public void setWaitSessionName(String waitSessionName) {
		this.waitSessionName = waitSessionName;
	}

	public String getInterviewSessionName() {
		return interviewSessionName;
	}

	public void setInterviewSessionName(String interviewSessionName) {
		this.interviewSessionName = interviewSessionName;
	}

}
