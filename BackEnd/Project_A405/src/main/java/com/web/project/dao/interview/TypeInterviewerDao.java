package com.web.project.dao.interview;


import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.web.project.model.interview.TypeInterviewer;


@Repository
public interface TypeInterviewerDao extends JpaRepository<TypeInterviewer, String>{

	List<TypeInterviewer> findAllTypeInterviewerByGroupTypeGroupTypeSeqAndGroupTypeInterviewTypeTypeSeq(int groupTypeGroupTypeSeq,int groupTypeInterviewTypeTypeSeq);
	TypeInterviewer findTypeInterviewerByInterviewerViewSeq(int interviewViewSeq);
}