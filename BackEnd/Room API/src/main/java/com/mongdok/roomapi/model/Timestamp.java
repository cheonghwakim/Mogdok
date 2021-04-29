package com.mongdok.roomapi.model;

import com.mongdok.roomapi.model.enums.StudyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * author: pinest94
 * since: 2021-04-29
 */

@Data
@Builder
@AllArgsConstructor
public class Timestamp {

    private LocalDateTime time;

    private StudyType type;
}
