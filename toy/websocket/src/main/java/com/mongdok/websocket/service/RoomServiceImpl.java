package com.mongdok.websocket.service;

import com.mongdok.websocket.model.RoomMessage;
import com.mongdok.websocket.repository.RoomRepository;
import com.mongdok.websocket.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

/**
 * author: pinest94
 * since: 2021-05-06
 */

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService{

    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private SeatRepository seatRepository;

    /**
     * destination정보에서 sessionId추출
     * @param dest
     * @return
     */
    @Override
    public String getSessionId(String dest) {
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
        roomMessage.setUserCount(roomRepository.getUserCount(roomMessage.getSessionId()));

        switch (roomMessage.getType()) {
            case ENTER:
                roomMessage.setMessage(roomMessage.getSender() + "님이 열람실에 입장했습니다. ");
                break;
            case QUIT:
                // TODO: MariaDB에 공부기록 저장

                // TODO: 좌석정보 삭제
                seatRepository.removeSeatInfo(roomMessage.getSessionId(), roomMessage.getUserId());

                roomMessage.setMessage(roomMessage.getSender() + "님이 열람실에 퇴장했습니다. ");
                break;
            case SEAT_ALLOCATED:
                // TODO: 좌석착석의 경우
                seatRepository.setSeatInfo(roomMessage.getSessionId(),
                                           roomMessage.getUserId(),
                                           roomMessage.getSender(),
                                           roomMessage.getSeatInfo());
                break;
            case SEAT_STATUS:
                // TODO: 공부시작, 휴식시작시간 저장 호출
                seatRepository.updateSeatInfo(roomMessage.getSessionId(),
                                              roomMessage.getUserId(),
                                              roomMessage.getSeatInfo());
                break;
            case CLEAR:
                seatRepository.removeAll(roomMessage.getSessionId());
        }

        redisTemplate.convertAndSend(channelTopic.getTopic(), roomMessage);
    }
}
