package com.web.project.dao.hr;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.project.model.hr.Company;

@Repository
public interface CompanyDao extends JpaRepository<Company, String>{
	Company findCompanyByComName(String comName);
	Company findCompanyByComSeq(int comSeq);
}
