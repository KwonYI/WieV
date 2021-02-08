package com.web.project.dao.recruit;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.project.model.recruit.Certificate;

public interface CertificateDao extends JpaRepository<Certificate, String> {
	List<Certificate> findListCertificateByApplicantApplySeq(int applySeq);
}
