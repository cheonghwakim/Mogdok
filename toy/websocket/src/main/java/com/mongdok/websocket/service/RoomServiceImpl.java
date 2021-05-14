package com.mongdok.websocket.service;

import com.mongdok.websocket.model.RoomMessage;
import com.mongdok.websocket.model.Seat;
import com.mongdok.websocket.model.SeatInfo;
import com.mongdok.websocket.model.enums.MessageType;
import com.mongdok.websocket.repository.RoomRepository;
import com.mongdok.websocket.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

/**
 * author: pinest94
 * since: 2021-05-06
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService{

    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private StudyLogService studyLogService;

    /**
     * destination정보에서 sessionId추출
     * @param dest
     * @return
     */
    @Override
    public String getRoomId(String dest) {
        int lastIndex = dest.lastIndexOf("/");
        if(lastIndex != -1) {
            return dest.substring(lastIndex + 1);
        } else return "";
    }

    /***
     * 열람실에 메시지 발송
     * @param roomMessage
     */
    @Override
    public void sendMessage(RoomMessage roomMessage) {
        roomMessage.setUserCount(roomRepository.getUserCount(roomMessage.getRoomId()));
        Seat seat = null;
        SeatInfo seatInfo = null;

        switch (roomMessage.getType()) {
            case ENTER:
                roomMessage.setMessage(roomMessage.getSender() + "님이 열람실에 입장했습니다. ");
                break;
            case QUIT:
                // TODO: MariaDB에 공부기록 저장
                roomMessage.setMessage(roomMessage.getSender() + "님이 열람실에 퇴장했습니다. ");
                break;
            case SEAT_ALLOCATED:
                // TODO: 좌석착석의 경우
                boolean check = seatRepository.setSeatInfo(roomMessage.getRoomId(),
                                           roomMessage.getUserId(),
                                           roomMessage.getSender(),
                                           roomMessage.getSeatInfo());
                // 좌석에 착석할 수 없는 경우
                if(!check) {
                    roomMessage.setType(MessageType.SEAT_ALLOCATE_FAIL);
                    roomMessage.setMessage("착석에 실패했습니다.");
                }
                break;
            case SEAT_STATUS:
                // TODO: 공부시작, 휴식시작시간 저장 호출
                log.info("[STUDY TYPE] : {}", roomMessage.getSeatInfo().getStudyType());
                seat = seatRepository.findSeatByUserId(roomMessage.getRoomId(), roomMessage.getUserId());
                log.info("[SEAT NO] : {}", seat.getSeatNo());

                seatRepository.updateSeatInfo(roomMessage.getRoomId(),
                                              roomMessage.getUserId(),
                                              roomMessage.getSeatInfo().getStudyType());

                seatInfo = SeatInfo.builder()
                        .seatNo(seat.getSeatNo())
                        .studyType(roomMessage.getSeatInfo().getStudyType())
                        .build();
                roomMessage.setSeatInfo(seatInfo);
                break;
            case END:
                // TODO: 좌석반납처리 구현
                log.info("***** END *****");
                seat = seatRepository.findSeatByUserId(roomMessage.getRoomId(), roomMessage.getUserId());
                
                // 가지고 있는 좌석이 없는 경우
                if(seat == null) {
                    log.info("가지고 있는 좌석이 없습니다.");
                    return;
                }
                seatInfo = SeatInfo.builder().seatNo(seat.getSeatNo()).build();
                roomMessage.setSeatInfo(seatInfo);

                // 좌석정보가 있는 경우 ----> 시간 정보 저장
                log.info("SEAT USER ID : {}", seat.getUserId());
                studyLogService.saveLog(seat.getUserId(), seat.getTimestampList(), seat.getAllocateTime());
                log.info("***** 공부기록 저장 *****");

                seatRepository.removeSeatInfo(roomMessage.getRoomId(), roomMessage.getUserId());
                log.info("***** 좌석정보 삭제 *****");

        }

        redisTemplate.convertAndSend(channelTopic.getTopic(), roomMessage);
    }
}
