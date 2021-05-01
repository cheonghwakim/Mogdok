package com.mongdok.desk.model.response.desk;

import java.util.List;

import com.mongdok.desk.model.response.board.BoardOnlyIdResponse;
import com.mongdok.desk.model.response.dday.DdayResponse;
import com.mongdok.desk.model.response.memo.MemoResponse;

import lombok.Data;

@Data
public class DeskAllResponse {
	private long deskId;
	
	private String promise;
	
	private List<MemoResponse> memoList;
	
	private List<DdayResponse> ddayList;
	
	private List<BoardOnlyIdResponse> guestbookList;
}
