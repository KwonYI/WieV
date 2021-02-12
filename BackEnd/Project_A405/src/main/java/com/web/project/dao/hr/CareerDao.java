package com.web.project.dao.hr;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.project.model.hr.Career;

@Repository
public interface CareerDao extends JpaRepository<Career, String>{
	Career findCareerByCaSeq(int caSeq);
	Career findCareerByCaNameAndPartPartSeq(String caName, int partPartSeq);
	
	List<Career> findListCareerByPartPartSeq(int partPartSeq);
}
