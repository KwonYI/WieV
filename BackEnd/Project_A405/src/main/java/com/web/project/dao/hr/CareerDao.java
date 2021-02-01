package com.web.project.dao.hr;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.project.model.hr.Career;
import com.web.project.model.hr.Company;

@Repository
public interface CareerDao extends JpaRepository<Career, String>{
	Career findCareerByCaSeq(int caSeq);
	Career findCareerByCaNameAndPartPartSeq(String caName, int partPartSeq);
	
	
}
