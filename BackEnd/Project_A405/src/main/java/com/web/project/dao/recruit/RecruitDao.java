package com.web.project.dao.recruit;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.project.model.recruit.Recruit;

@Repository
public interface RecruitDao extends JpaRepository<Recruit, String> {

	Recruit findRecruitByReSeq(int reSeq);
	Optional<Recruit> findOptionalRecruitByReSeq(int reSeq);
	List<Recruit> findListRecruitByCompanyComSeq(int companyComSeq);
}
