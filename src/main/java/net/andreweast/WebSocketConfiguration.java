package net.andreweast;

import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configure WebSockets with the STOMP protocol
 */
@Component
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
    // Route prefix: Every message route will have this prefix
    public static final String MESSAGE_PREFIX = "/topic";
    // Backend endpoint:
    private static final String ENDPOINT = "/geneticalgorithm";
    // Message broker: acts and a go-between to relay between server and clients
    private static final String MESSAGE_BROKER = "/app";

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Backend clients will link up over this endpoint
        registry.addEndpoint(ENDPOINT).withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker(MESSAGE_PREFIX);
        registry.setApplicationDestinationPrefixes(MESSAGE_BROKER);
    }
}
