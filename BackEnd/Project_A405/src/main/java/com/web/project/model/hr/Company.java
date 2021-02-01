package com.web.project.model.hr;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Company {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int comSeq;
	
	private String comName;
	private String comLogo;
	private String comAddress;
	private String comHomepage;
}
