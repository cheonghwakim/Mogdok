package com.mongdok.roomapi.service;

import com.mongdok.roomapi.model.Seat;
import com.mongdok.roomapi.model.SeatElements;
import com.mongdok.roomapi.model.enums.StudyType;
import com.mongdok.roomapi.repository.SeatRedisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class SeatServiceImplTest {

    private SeatService seatService;

    @Mock
    private SeatRedisRepository seatRedisRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        seatService = new SeatServiceImpl();
    }

    @Test
    @DisplayName("좌석할당 테스트 😀")
    public void allocateTest() {
        Seat seat = Seat.builder()
                .id(SeatElements.SESSION_A+SeatElements.ID_DELIMETER+"1004")
                .seatNo(1)
                .sessionId(SeatElements.SESSION_A)
                .userId("1004")
                .userName("김한솔")
                .studyType(StudyType.PAUSE)
                .build();

        seatRedisRepository.save(seat);
    }

}