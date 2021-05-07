package com.mongdok.websocket.model;

import com.mongdok.websocket.model.enums.StudyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * author: pinest94
 * since: 2021-05-06
 */

@Data
@Builder
@AllArgsConstructor
public class Timestamp implements Serializable {

    private LocalDateTime time;

    private StudyType type;
}