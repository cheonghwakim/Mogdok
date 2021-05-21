package com.mongdok.websocket.exception;

/**
 * author: pinest94
 * since: 2021-05-21
 */
public class ConnectionFailException extends Exception{

    public ConnectionFailException() {
        super("[DENY] 계정 중복으로 인한 연결 거절");
    }
}
