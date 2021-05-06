package com.mongdok.websocket.service;

import com.mongdok.websocket.model.RoomMessage;
import com.mongdok.websocket.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
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
    private final RoomRepository roomRepository;

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
        if(RoomMessage.MessageType.ENTER.equals(roomMessage.getType())) {
            roomMessage.setMessage(roomMessage.getSender() + "님이 열람실에 입장했습니다. ");
        } else if(RoomMessage.MessageType.QUIT.equals(roomMessage.getType())) {
            roomMessage.setMessage(roomMessage.getSender() + "님이 열람실에 퇴장했습니다. ");
        }
        redisTemplate.convertAndSend(channelTopic.getTopic(), roomMessage);
    }
}
