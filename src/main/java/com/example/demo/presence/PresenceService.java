package com.example.demo.presence;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Service
public class PresenceService {

    private final OnlineUserRegistry onlineUserRegistry;

    public void markOnline(UUID userId, WebSocketSession session){
        onlineUserRegistry.add(userId, session);
    }

    public void markOffline(UUID userId){
        onlineUserRegistry.remove(userId);
    }

    public Set<UUID> listOnlineUsers() {
        return onlineUserRegistry.getOnlineUserIds();
    }

    public boolean isUserOnline(UUID userId) {
        return onlineUserRegistry.isOnline(userId);
    }

    public WebSocketSession getSession(UUID userId) {
        return onlineUserRegistry.getSession(userId);
    }

}
