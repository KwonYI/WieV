package com.web.project.dao.recruit;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.project.model.recruit.ApplicantGroup;

@Repository
public interface ApplicantGroupDao extends JpaRepository<ApplicantGroup, String> {

	List<ApplicantGroup> findAllApplicantGroupByGroupDetailDetailSeq(int groupDetailDetailSeq);
	ApplicantGroup findApplicantGroupByApplicantApplySeq(int applicantApplySeq);
}