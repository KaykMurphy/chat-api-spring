package com.example.demo.presence;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OnlineUserRegistry {

    private final Map<UUID, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public void add(UUID userId, WebSocketSession session) {
        sessions.put(userId, session);
    }

    public void remove (UUID userId){
        sessions.remove(userId);
    }

    public boolean isOnline(UUID userId) {
        return sessions.containsKey(userId);
    }

    public Set<UUID> getOnlineUserIds() {
        return sessions.keySet();
    }

    public WebSocketSession getSession(UUID userId) {
        return sessions.get(userId);
    }
}
