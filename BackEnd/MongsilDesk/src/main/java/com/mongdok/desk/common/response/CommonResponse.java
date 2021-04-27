package com.mongdok.desk.common.response;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommonResponse<T> extends BasicResponse {
	private int size;
	private T data;

	public CommonResponse(T data) {
		this.data = data;
		if(data instanceof List) {
			this.size = ((List<?>)data).size();
		} else {
			this.size = 1;
		}
	}
}


