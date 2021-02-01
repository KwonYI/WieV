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
public class DetailOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int orderSeq;
	
	// 외래키
	private int groupTypeGroupTypeSeq;
	private int groupTypeInterviewTypeTypeSeq;
	private int groupDetailDetailSeq;
	
	private int trueOrder;

	public int getOrderSeq() {
		return orderSeq;
	}

	public void setOrderSeq(int orderSeq) {
		this.orderSeq = orderSeq;
	}

	public int getGroupTypeGroupTypeSeq() {
		return groupTypeGroupTypeSeq;
	}

	public void setGroupTypeGroupTypeSeq(int groupTypeGroupTypeSeq) {
		this.groupTypeGroupTypeSeq = groupTypeGroupTypeSeq;
	}

	public int getGroupTypeInterviewTypeTypeSeq() {
		return groupTypeInterviewTypeTypeSeq;
	}

	public void setGroupTypeInterviewTypeTypeSeq(int groupTypeInterviewTypeTypeSeq) {
		this.groupTypeInterviewTypeTypeSeq = groupTypeInterviewTypeTypeSeq;
	}

	public int getGroupDetailDetailSeq() {
		return groupDetailDetailSeq;
	}

	public void setGroupDetailDetailSeq(int groupDetailDetailSeq) {
		this.groupDetailDetailSeq = groupDetailDetailSeq;
	}

	public int getTrueOrder() {
		return trueOrder;
	}

	public void setTrueOrder(int trueOrder) {
		this.trueOrder = trueOrder;
	}
	
}
