package com.spring.jwt.service;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocketIOService {

    private final SocketIOServer socketIOServer;

    /**
     * Send event to all connected clients
     */
    public void sendToAll(String eventName, Object data) {
        socketIOServer.getBroadcastOperations().sendEvent(eventName, data);
    }

    /**
     * Send event to specific room
     */
    public void sendToRoom(String room, String eventName, Object data) {
        socketIOServer.getRoomOperations(room).sendEvent(eventName, data);
    }

    /**
     * Send event to specific client
     */
    public void sendToClient(String clientId, String eventName, Object data) {
        socketIOServer.getClient(java.util.UUID.fromString(clientId)).sendEvent(eventName, data);
    }
}
