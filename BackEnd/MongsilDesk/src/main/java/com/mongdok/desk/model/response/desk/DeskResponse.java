package com.mongdok.desk.model.response.desk;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeskResponse {
	private String promise;
}
