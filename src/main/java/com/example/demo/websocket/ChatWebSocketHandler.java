package com.example.demo.websocket;

import com.example.demo.message.Message;
import com.example.demo.message.MessageService;
import com.example.demo.presence.PresenceService;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final PresenceService presenceService;
    private final MessageService messageService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    record MessagePayload(UUID receiverId, String content) {}

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        UUID userId = extractUserId(session);

        if (userId != null) {
            presenceService.markOnline(userId, session);
            System.out.println("Conexão estabelecida: " + userId);
        } else {
            session.close(CloseStatus.BAD_DATA);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        UUID senderId = extractUserId(session);

        MessagePayload payload = objectMapper.readValue(message.getPayload(), MessagePayload.class);

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Remetente não encontrado"));

        User receiver = userRepository.findById(payload.receiverId)
                .orElseThrow(() -> new RuntimeException("Destinatário não encontrado"));

        Message savedMessage = messageService.sendMessage(sender, receiver, payload.content());

        WebSocketSession receiverSession = presenceService.getSession(receiver.getId());

        if (receiverSession != null && receiverSession.isOpen()) {
            String jsonResponse = objectMapper.writeValueAsString(savedMessage);
            receiverSession.sendMessage(new TextMessage(jsonResponse));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        UUID userId = extractUserId(session);
        if (userId != null) {
            presenceService.markOffline(userId);
            System.out.println("❌ Conexão encerrada: " + userId);
        }
    }

    private UUID extractUserId(WebSocketSession session) {
        try {
            URI uri = session.getUri();
            String query = uri.getQuery();
            if (query != null && query.contains("userId=")) {
                String idStr = query.split("userId=")[1];
                return UUID.fromString(idStr);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}