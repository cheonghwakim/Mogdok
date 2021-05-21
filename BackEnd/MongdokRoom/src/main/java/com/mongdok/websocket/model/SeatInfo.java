package com.mongdok.websocket.model;

import com.mongdok.websocket.model.enums.StudyType;
import lombok.Builder;
import lombok.Data;

/**
 * author: pinest94
 * since: 2021-05-05
 */

@Data
@Builder
public class SeatInfo {

    private int seatNo;

    private StudyType studyType;
}
