package com.mongdok.roomapi.model;

import com.mongdok.roomapi.model.enums.StudyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * author: pinest94
 * since: 2021-04-28
 */

@Data
@Builder
@RedisHash("Seats")
@NoArgsConstructor
@AllArgsConstructor
public class Seat implements Serializable {

    /***
     * 좌석의 기본키
     */
    @Id
    private String id;

    /***
     * 좌석에 착석한 유저이름
     */
    private String userName;

    /***
     * 좌석에 착석한 유저아이디
     */
    private String userId;

    /***
     * 해당 좌석의 세션아이디
     */
    private String sessionId;

    /***
     * 해당 좌석의 번호
     */
    private int seatNo;

    /***
     * 해당 좌석의 사용자 상태
     */
    private StudyType studyType;

    /***
     * 착석 시간
     */
    private LocalDateTime allocateTime;

    /***
     * 공부 시간 기록 리스트
     */
    private List<Timestamp> timestampList;
}
