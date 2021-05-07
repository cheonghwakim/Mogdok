package com.mongdok.websocket.config.handler;

import com.mongdok.websocket.model.RoomMessage;
import com.mongdok.websocket.repository.RoomRepository;
import com.mongdok.websocket.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Optional;

/**
 * author: pinest94
 * since: 2021-05-06
 */

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

    private final RoomRepository roomRepository;
    private final RoomService roomService;

    // WebSocket을 통해 들어온 요청이 처리 되기 전 실행된다.
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // 열람실 구독요청
        if(StompCommand.SUBSCRIBE == accessor.getCommand()) {
            // 헤더정보에서 구독 destination정보를 얻고 sessionId를 추출한다.
            String sessionId = roomService.getSessionId((String) Optional.ofNullable(message.getHeaders().get("simpDestination")).orElse("InvalidSessionId"));
            log.info("SUB - sessionId : {}", sessionId);

            // 열람실에 들어온 클라이언트 userId를 sessionId와 맵핑해 놓는다. (추후 특정 클라이언트가 어떤 열람실에 있는지 알기 위함)
            String userId = (String) message.getHeaders().get("simpSessionId");
            log.info("SUB - userId : {}", userId);
            roomRepository.setUserEnterInfo(userId, sessionId);

            // 열람실의 인원 수를 +1한다.
            roomRepository.plusUserCount(sessionId);

            // 클라이언트 입장 메시지를 채팅방에 발송한다.
            String userName = Optional.ofNullable((Principal)message.getHeaders().get("simpUser")).map(Principal::getName).orElse("UnknownUser");

            for(String key : message.getHeaders().keySet()) {
                log.info("{} - {}", key, message.getHeaders().get(key));
            }

            log.info("SUB - userName : {}", userName);

            roomService.sendMessage(RoomMessage.builder().type(RoomMessage.MessageType.ENTER).sessionId(sessionId).sender(userName).build());
            log.info("SUBSCRIBED {} ----- {}", userId, sessionId);

        } else if(StompCommand.DISCONNECT == accessor.getCommand()) { // WebSocket 연결 종료
            // 연결이 종료된 클라이언트 userId로 열람실 sessionId를 얻는다.
            String userId = (String) message.getHeaders().get("simpSessionId");
            String sessionId = roomRepository.getSessionEnterUserId(userId);
            log.info("dis : {} --- {}", userId, sessionId);

            // 열람실의 인원 수를 -1한다.
            roomRepository.minusUserCount(sessionId);

            // 해당 유저의 퇴장 메시지를 열람실에 발송한다.
            String userName = Optional.ofNullable((Principal)message.getHeaders().get("simpUser")).map(Principal::getName).orElse("UnknownUser");
            roomService.sendMessage(RoomMessage.builder().type(RoomMessage.MessageType.QUIT).sessionId(sessionId).sender(userName).build());

            // 퇴장한 클라이언트의 sessionId 매핑 정보를 삭제한다.
            roomRepository.removeUserEnterInfo(userId);
            log.info("DISCONNECT {} ----- {}", userId, sessionId);
        }
        return message;
    }


}
