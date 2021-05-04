package com.mongdok.websocket.config;

/**
 * author: pinest94
 * since: 2021-05-03
 */

//@Slf4j
//@RequiredArgsConstructor
//@Component
//public class WebSockChatHandler extends TextWebSocketHandler {
//
//    private final ObjectMapper objectMapper;
//    private final ChatRepository chatRepository;
//
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String payload = message.getPayload();
//        log.info("payload {}", payload);
//
//        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
//        ChatRoom room = chatRepository.findRoomById(chatMessage.getSessionId());
//
//        room.handleActions(session, chatMessage, chatRepository);
//    }
//}
