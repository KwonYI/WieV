
package com.web.project.dao.hr;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.project.model.hr.Hr;

@Repository
public interface HrDao extends JpaRepository<Hr, String> {
//    Hr getUserByUserEmail(String userEmail);
//
    Optional<Hr> findHrByHrEmailAndHrPassword(String hrEmail, String hrPassword);
    Optional<Hr> findHrByHrEmail(String hrEmail);
    Optional<Hr> findHrByHrCertified(String hrCertified);
    Optional<Hr> findHrByHrSeq(int hrSeq);
    Optional<Hr> findHrByHrEmailAndHrNameAndHrPhone(String hrEmail, String hrName, String hrPhone);
}