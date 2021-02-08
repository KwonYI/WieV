package com.web.project.dao.recruit;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.project.model.recruit.Applicant;

@Repository
public interface ApplicantDao extends JpaRepository<Applicant, String> {
	Applicant findApplicantByapplyId(String id);
	List<Applicant> findAllApplicantByRecruitReSeq(int recruitReSeq);
	
	
	Applicant findApplicantByRecruitReSeqAndCareerCaSeq(int recruitReSeq, int careerCaSeq);

	Applicant findApplicantByApplySeq(int applySeq);
	
	
	Optional<Applicant> findOptionalApplicantByApplySeq(int applySeq);
	Optional<Applicant> findOptionalApplicantByApplyEmail(String applyEmail);

	
	List<Applicant> findApplicantByRecruitReSeqAndCareerCaSeqAndApplyAssigned(int recruitReSeq, int careerCaSeq,int applyAssigned);
	long countByRecruitReSeqAndCareerCaSeq(int recruitReSeq, int careerCaSeq);
}
