package com.mongdok.roomapi.model;

/**
 * author: pinest94
 * since: 2021-04-29
 */
public class SeatElements {

    // ----------------------- SEAT INFO ------------------------ //
    public static final String SEAT_HEADER = "Seats:";

    public static final String SESSION_A = "sessionA"; // A Session name
    public static final int SESSION_A_SEAT_SIZE = 16; // A Session seat size

    public static final String SESSION_B = "sessionB"; // A Session name
    public static final int SESSION_B_SEAT_SIZE = 16; // A Session seat size

    public static final String SESSION_C = "sessionC"; // A Session name
    public static final int SESSION_C_SEAT_SIZE = 16; // A Session seat size

    public static final String ID_DELIMETER = "###";


    // ------------------------ RESPONSE --------------------------- //
    public static final String SEAT_ALLOCATED_SUCCESS = "좌석이 할당되었습니다.";
    public static final String SEAT_ALLOCATED_NONE = "좌석을 가지고 있지 않습니다.";
    public static final String SEAT_ALLOCATED_NOT = "이용 가능한 좌석이 없습니다.";
    public static final String SEAT_ALLOCATED_FAIL = "좌석 할당에 실패했습니다.";
    public static final String SEAT_RELEASE_SUCCESS = "좌석이 반납되었습니다.";
    public static final String SEAT_RELEASE_NONE = "반납할 좌석이 없습니다.";
    public static final String SEAT_RELEASE_FAIL = "좌석 반납에 실패했습니다.";

}
