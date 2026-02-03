package com.spring.jwt.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import com.corundumstudio.socketio.protocol.JacksonJsonSupport;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.spring.jwt.jwt.JwtService;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SocketIOConfig {

    private final JwtService jwtService;

    @Value("${socketio.host:0.0.0.0}")
    private String host;

    @Value("${socketio.port:8082}")
    private Integer port;

    private SocketIOServer server;

    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        // Use 0.0.0.0 to accept connections from all network interfaces
        config.setHostname("0.0.0.0");
        config.setPort(port);

        log.info("🚀 Configuring Socket.IO server on 0.0.0.0:{}", port);

        // Allow all origins for development (restrict in production)
        config.setOrigin("*");
        config.setAllowCustomRequests(true);
        config.setTransports(com.corundumstudio.socketio.Transport.WEBSOCKET, com.corundumstudio.socketio.Transport.POLLING);
        
        // Enable CORS for Socket.IO
        config.setBossThreads(1);
        config.setWorkerThreads(100);

        // Support Java 8 date/time serialization
        JacksonJsonSupport jsonSupport = new JacksonJsonSupport(new JavaTimeModule());
        config.setJsonSupport(jsonSupport);

        config.setAuthorizationListener(data -> {
            String token = null;
            
            // Try to get token from query parameter first (Socket.IO client query)
            token = data.getSingleUrlParam("token");
            
            // If not in query, try Authorization header
            if (token == null || token.isEmpty()) {
                String authHeader = data.getHttpHeaders().get("Authorization");
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    token = authHeader.substring(7);
                }
            }

            log.info("🔗 Socket.IO connection attempt from: {}", data.getAddress());
            log.info("🔑 Token received: {}", token != null ? (token.length() > 20 ? token.substring(0, 20) + "..." : "empty") : "null");

            // If token exists, validate it
            if (token != null && !token.isEmpty()) {
                try {
                    if (jwtService.isValidToken(token)) {
                        var claims = jwtService.extractClaims(token);
                        String username = claims.getSubject();
                        log.info("✅ Socket.IO authenticated user: {}", username);
                        return true;
                    } else {
                        log.warn("⚠️ Invalid token provided");
                    }
                } catch (Exception e) {
                    log.error("❌ Socket.IO authentication failed: {}", e.getMessage());
                    // For development, allow connection even if token validation fails
                    // In production, return false here
                    return true;
                }
            } else {
                log.warn("⚠️ No token provided in connection request");
            }

            // Allow connection for testing (change to false in production)
            log.info("🔓 Allowing connection without authentication (development mode)");
            return true;
        });

        server = new SocketIOServer(config);

        // Start the server
        log.info("🟢 Starting Socket.IO server...");
        try {
            server.start();
            log.info("✅ Socket.IO server started successfully on port: {}", port);
        } catch (Exception e) {
            log.error("❌ Failed to start Socket.IO server: {}", e.getMessage());
            throw new RuntimeException("Failed to start Socket.IO server", e);
        }

        // Add connection listeners
        server.addConnectListener(client -> {
            log.info("🎉 Client connected: {}", client.getSessionId());
        });

        server.addDisconnectListener(client -> {
            log.info("🔌 Client disconnected: {}", client.getSessionId());
        });

        return server;
    }

    @PreDestroy
    public void stopSocketIOServer() {
        if (server != null) {
            log.info("🛑 Stopping Socket.IO server...");
            server.stop();
            log.info("✅ Socket.IO server stopped");
        }
    }

    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
        return new SpringAnnotationScanner(socketServer);
    }
}