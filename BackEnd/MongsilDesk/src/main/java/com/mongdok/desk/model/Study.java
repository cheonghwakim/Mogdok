package com.mongdok.desk.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Entity
public class Study {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long studyId;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date studyTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;

	private String status;

	private String userId;
}
