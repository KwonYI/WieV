package com.web.project.dao.interview;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.project.model.interview.InterviewType;


@Repository
public interface InterviewTypeDao extends JpaRepository<InterviewType, String>{
	InterviewType findInterviewTypeByTypeSeq(int typeSeq);
}
