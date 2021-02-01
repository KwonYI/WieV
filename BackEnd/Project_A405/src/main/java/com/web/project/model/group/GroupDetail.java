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
public class GroupDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int detailSeq;
	
	private int groupGroupSeq;
	private int detailDivide;
	
	public int getDetailSeq() {
		return detailSeq;
	}
	public void setDetailSeq(int detailSeq) {
		this.detailSeq = detailSeq;
	}
	public int getGroupGroupSeq() {
		return groupGroupSeq;
	}
	public void setGroupGroupSeq(int groupGroupSeq) {
		this.groupGroupSeq = groupGroupSeq;
	}
	public int getDetailDivide() {
		return detailDivide;
	}
	public void setDetailDivide(int detailDivide) {
		this.detailDivide = detailDivide;
	}
}
