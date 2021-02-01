package com.web.project.dao.group;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.project.model.group.DetailOrder;

@Repository
public interface DetailOrderDao extends JpaRepository<DetailOrder, String>{

	List<DetailOrder> findListDetailOrderByGroupDetailDetailSeq(int groupDetailDetailSeq);
	
}
