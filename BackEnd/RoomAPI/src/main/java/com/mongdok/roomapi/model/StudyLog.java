package com.mongdok.roomapi.model;

import com.mongdok.roomapi.model.enums.StudyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * author: pinest94
 * since: 2021-04-29
 */

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "study")
public class StudyLog {

    /***
     * 공부기록 기본키 id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /***
     * 사용자 아이디
     */
    private String userId;

    /***
     * 시간 기록
     */
    private LocalDateTime studyTime;

    /***
     * 상태
     */
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StudyType studyType;

    /***
     * 기준 시작 시간
     */
    private LocalDateTime startTime;
}