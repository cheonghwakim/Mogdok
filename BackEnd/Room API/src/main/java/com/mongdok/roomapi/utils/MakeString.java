package com.mongdok.roomapi.utils;

import com.mongdok.roomapi.model.SeatElements;

/**
 * author: pinest94
 * since: 2021-04-29
 */
public class MakeString {

    public static String makeId(String sessionId, String userId) {
        StringBuffer sb = new StringBuffer();
        sb
                .append(sessionId)
                .append(SeatElements.ID_DELIMETER)
                .append(userId);

        return sb.toString();
    }

    public static String makeKey(String sessionId, String userId) {
        StringBuffer sb = new StringBuffer();
        sb.append(SeatElements.SEAT_HEADER)
                .append(sessionId)
                .append(SeatElements.ID_DELIMETER)
                .append(userId);

        return sb.toString();
    }
}
