package com.spring.jwt.config;

import com.spring.jwt.config.filter.JwtTokenAuthenticationFilter;
import com.spring.jwt.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

import java.util.List;
import java.util.stream.Collectors;

// DISABLED - Replaced by Socket.IO implementation
// @Configuration
// @EnableWebSocketMessageBroker
// @RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // Field made non-final since class is disabled and @RequiredArgsConstructor is commented
    @SuppressWarnings("unused")
    private JwtService jwtService;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/Aucbidding")
                .setAllowedOrigins("http://localhost:5173","http://localhost:63342", "https://caryanamindia.com","http://localhost:8081")
                .withSockJS();

        registry.addEndpoint("/Aucbidding/websocket")
                .setAllowedOriginPatterns("*")
                .setHandshakeHandler(new CustomHandshakeHandler());
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setMessageSizeLimit(8192);
        registration.setSendTimeLimit(20 * 10000);
        registration.setSendBufferSizeLimit(3 * 512 * 1024);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String token = extractToken(accessor);
                    if (token != null && jwtService.isValidToken(token)) {
                        try {
                            var claims = jwtService.extractClaims(token);
                            String username = claims.getSubject();
                            List<String> authorities = claims.get("authorities", List.class);

                            UsernamePasswordAuthenticationToken auth =
                                    new UsernamePasswordAuthenticationToken(
                                            username,
                                            null,
                                            authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
                                    );
                            accessor.setUser(auth);
                            SecurityContextHolder.getContext().setAuthentication(auth);

                            System.out.println("✅ WebSocket authenticated user: " + username);
                        } catch (Exception e) {
                            System.err.println("❌ WebSocket authentication failed: " + e.getMessage());
                        }
                    } else {
                        System.err.println("❌ No valid token provided for WebSocket connection");
                    }
                }
                return message;
            }

            private String extractToken(StompHeaderAccessor accessor) {
                // Try Authorization header first
                String authHeader = accessor.getFirstNativeHeader("Authorization");
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    return authHeader.substring(7);
                }

                // Try token header
                String tokenHeader = accessor.getFirstNativeHeader("token");
                if (tokenHeader != null) {
                    return tokenHeader;
                }

                return null;
            }
        });
    }
}