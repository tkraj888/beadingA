package com.spring.jwt.config;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
// @Component  // COMMENT THIS OUT
@RequiredArgsConstructor
public class SocketIOServerRunner implements CommandLineRunner {

    private final SocketIOServer socketIOServer;

    @Override
    public void run(String... args) throws Exception {
        // socketIOServer.start();  // REMOVE THIS - server is already started
        // log.info("✅ Socket.IO server started on port {}", socketIOServer.getConfiguration().getPort());
    }
}