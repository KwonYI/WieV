package com.web.project.dao.group;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.project.model.group.GroupAll;
import com.web.project.model.recruit.Applicant;

@Repository
public interface GroupAllDao extends JpaRepository<GroupAll, String>{
	GroupAll findGroupAllByGroupDateAndCareerCaSeqAndRecruitReSeq(String groupDate, int careerCaSeq, int recruitReSeq);
	GroupAll findGroupAllByGroupSeq(int groupSeq);
	
	List<GroupAll> findListGroupAllByRecruitReSeq(int recruitReSeq);
}
