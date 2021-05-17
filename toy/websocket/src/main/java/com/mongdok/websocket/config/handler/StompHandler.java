package com.mongdok.websocket.config.handler;

import com.mongdok.websocket.model.RoomMessage;
import com.mongdok.websocket.model.Seat;
import com.mongdok.websocket.model.SeatInfo;
import com.mongdok.websocket.model.enums.MessageType;
import com.mongdok.websocket.repository.RoomRepository;
import com.mongdok.websocket.repository.SeatRepository;
import com.mongdok.websocket.service.RoomService;
import com.mongdok.websocket.service.StudyLogService;
import com.mongdok.websocket.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * author: pinest94
 * since: 2021-05-06
 */

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

    private final StudyLogService studyLogService;
    private final SeatRepository seatRepository;
    private final RoomRepository roomRepository;
    private final RoomService roomService;
    private final JWTUtil jwtUtil;

    // WebSocket을 통해 들어온 요청이 처리 되기 전 실행된다.
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if(StompCommand.CONNECT == accessor.getCommand()) {
            String jwtToken = accessor.getFirstNativeHeader("token");
            roomRepository.setTokenInfo((String) message.getHeaders().get("simpSessionId"), jwtToken);
        }
        else if(StompCommand.SUBSCRIBE == accessor.getCommand()) {
            // 헤더정보에서 구독 destination정보를 얻고 sessionId를 추출한다.
            String roomId = roomService.getRoomId((String) Optional.ofNullable(message.getHeaders().get("simpDestination")).orElse("InvalidSessionId"));

            // 열람실에 들어온 클라이언트 userId를 sessionId와 맵핑해 놓는다. (추후 특정 클라이언트가 어떤 열람실에 있는지 알기 위함)
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            roomRepository.setRoomEnterInfo(sessionId, roomId);

            // 열람실의 인원 수를 +1한다.
            roomRepository.plusUserCount(roomId);

            // token값을 꺼내온다.
            String token = roomRepository.getTokenBySessionId(sessionId);

            String userName = jwtUtil.getUserName(token);
            String userId = jwtUtil.getUserId(token);

            // 클라이언트 입장 메시지를 채팅방에 발송한다.
            roomService.sendMessage(RoomMessage.builder().type(MessageType.ENTER)
                    .roomId(roomId)
                    .sender(userName)
                    .userId(userId)
                    .build());

        } else if(StompCommand.DISCONNECT == accessor.getCommand()) { // WebSocket 연결 종료
            // 연결이 종료된 클라이언트 userId로 열람실 sessionId를 얻는다.
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            String roomId = roomRepository.getRoomEnterSessionId(sessionId);

            log.info("[DISCONNECT] sessionId : {}", sessionId);
            // token값을 꺼내온다.
            String token = roomRepository.getTokenBySessionId(sessionId);

            String userName = "";
            String userId = "";
            int seatNo = 0;

            if(token != null) {
                userName = jwtUtil.getUserName(token);
                userId = jwtUtil.getUserId(token);

                Seat seat = seatRepository.findSeatByUserId(roomId, userId);

                // 좌석정보가 있는 경우 ----> 시간 정보 저장
                if (seat != null) {
                    // TODO: 현재 토큰에 담긴 userId가 실제 id가 아니므로 DB오류 발생시킨다.
                    seatNo = seat.getSeatNo();
                    studyLogService.saveLog(userId, seat.getTimestampList(), seat.getAllocateTime());
                    log.info("[DISCONNECT] 공부기록 저장 ");
                    seatRepository.minusSeatCount(roomId);
                    log.info("[DISCONNECT] 현재 착석 유저 수 : {} ", seatRepository.getSeatCount(roomId));
                }
            }

            // 열람실의 인원 수를 -1한다.
            roomRepository.minusUserCount(roomId);

            // 퇴장한 클라이언트의 sessionId 매핑 정보를 삭제한다.
            roomRepository.removeRoomEnterInfo(sessionId);

            roomRepository.removeToken(sessionId);

            // TODO: 좌석정보 삭제
            seatRepository.removeSeatInfo(roomId, userId);

            SeatInfo seatInfo = SeatInfo.builder().seatNo(seatNo).build();

            // 해당 유저의 퇴장 메시지를 열람실에 발송한다.
            roomService.sendMessage(RoomMessage.builder()
                    .type(MessageType.QUIT)
                    .roomId(roomId)
                    .sender(userName)
                    .userId(userId)
                    .seatInfo(seatInfo)
                    .build());
            log.info("[DISCONNECT] {} ----- {}", sessionId, roomId);
        }
        return message;
    }


}
