package com.web.project.dao.hr;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.project.model.hr.Part;

@Repository
public interface PartDao extends JpaRepository<Part, String>{
	Part findPartByPartName(String partName);
	Part findPartByCompanyComSeqAndPartName(int companyComSeq, String partName);
	
	Part findPartByPartSeq(int partSeq);
	
	List<Part> findListPartByCompanyComSeq(int companyComSeq);
}
