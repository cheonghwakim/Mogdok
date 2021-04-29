package com.mongdok.desk.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Entity(name = "desk")
public class Desk {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long deskId;
	
	private String promise;
	
	private String userId;
	
	@OneToMany(mappedBy = "deskId")
	private List<Memo> memoList;
	
	@OneToMany(mappedBy = "deskId")
	private List<Dday> ddayList;
	
	@OneToMany(mappedBy = "deskId")
	private List<Guestbook> guestbookList;
	
	
}
