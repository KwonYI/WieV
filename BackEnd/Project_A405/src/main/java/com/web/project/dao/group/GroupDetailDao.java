package com.web.project.dao.group;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.project.model.group.GroupDetail;

@Repository
public interface GroupDetailDao extends JpaRepository<GroupDetail, String>{

	List<GroupDetail> findListGroupDetailByGroupGroupSeq(int groupGroupSeq);
	List<GroupDetail> findAllGroupDetailByGroupGroupSeq(int groupGroupSeq);
	
	GroupDetail findGroupDetailByDetailSeq(int detailSeq);
}
