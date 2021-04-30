//package com.mongdok.roomapi.controller;
//
//import com.mongdok.roomapi.model.Seat;
//import com.mongdok.roomapi.model.SeatElements;
//import com.mongdok.roomapi.model.enums.StudyType;
//import com.mongdok.roomapi.service.SeatService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(SeatController.class)
//class SeatControllerTest {
//
//    @Autowired
//    MockMvc mvc;
//
//    @MockBean
//    private SeatService seatService;
//
//    @Test
//    public void allocateTest() throws Exception {
////        Seat resource = Seat.builder()
////                .seatNo(1)
////                .sessionId(SeatElements.SESSION_A)
////                .userId(SeatElements.SESSION_A+ SeatElements.ID_DELIMETER+"1004")
////                .userName("안양불빠따")
////                .build();
////
////        given(seatService.allocateSeat(resource))
////                .willReturn(new ResponseEntity<>(HttpStatus.CREATED));
//
//        mvc.perform(post("/roomapi/v1/seats")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("{ \"seatNo\":1, " +
//                        "\"userName\":\"안양불빠따\", " +
//                        "\"sessionId\":\"sessionA\", " +
//                        "\"userId\":\"1004\"}"))
//                .andExpect(status().isCreated());
//    }
//}