package com.mongdok.websocket.service;

import com.mongdok.websocket.model.RoomMessage;

/**
 * author: pinest94
 * since: 2021-05-06
 */
public interface RoomService {

    /***
     * destination에서 sessionId 추출
     * @param dest
     * @return
     */
    String getSessionId(String dest);

    /***
     * 열람실에 메시지 발송
     * @param roomMessage
     */
    void sendMessage(RoomMessage roomMessage);
}
