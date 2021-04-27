package com.mongdok.desk.model.response;

import java.util.List;

import javax.persistence.OneToMany;

import lombok.Data;

@Data
public class DeskAllResponse {
	private int deskId;
	
	private String promise;
	
	private List<MemoResponse> memoList;
	
	private List<DdayResponse> ddayList;
	
	private List<GuestBookOnlyIdResponse> guestbookList;
}
