package com.web.project.model.hr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Hr {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String hrSeq;

	private String hrEmail;
	
	@JsonIgnore
	private String hrPassword;
	
	private String hrPhone;
	
	@Column(insertable = false, updatable = false)
	private LocalDateTime hrCreateDate;
	
	// Y, N
	private String hrCertified;
	
	private int companyComSeq;
	
	private String hrName;
}
