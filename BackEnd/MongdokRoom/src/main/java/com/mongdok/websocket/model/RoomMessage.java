package com.mongdok.websocket.model;

import com.mongdok.websocket.model.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * author: pinest94
 * since: 2021-05-04
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomMessage {
    
    private MessageType type; // 메시지 타입
    private String roomId; // 방 번호
    private String userId; // 사용자 아이디
    private String sender; // 메시지 보낸 사람
    private String message; // 메시지
    private Long userCount; // 접속자 수
    private SeatInfo seatInfo; // 좌석정보
}
