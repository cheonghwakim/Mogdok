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
    public static final String NO_SEAT_AVAILABLE = "이용 가능한 좌석이 없습니다.";
    public static final String SEAT_ALLOCATED = "좌석이 할당되었습니다.";
}
