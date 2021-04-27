package com.mongdok.desk.model.response.desk;

import java.util.List;

import javax.persistence.OneToMany;

import com.mongdok.desk.model.response.dday.DdayResponse;
import com.mongdok.desk.model.response.guestbook.GuestBookOnlyIdResponse;
import com.mongdok.desk.model.response.memo.MemoResponse;

import lombok.Data;

@Data
public class DeskAllResponse {
	private int deskId;
	
	private String promise;
	
	private List<MemoResponse> memoList;
	
	private List<DdayResponse> ddayList;
	
	private List<GuestBookOnlyIdResponse> guestbookList;
}
