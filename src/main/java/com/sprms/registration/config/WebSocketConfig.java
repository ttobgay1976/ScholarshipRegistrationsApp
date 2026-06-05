package com.sprms.registration.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	
	 public WebSocketConfig() {
	        System.out.println("🔥 WebSocketConfig LOADED");
	    }

	    @Override
	    public void registerStompEndpoints(StompEndpointRegistry registry) {

	        System.out.println("🔥 Registering /ws endpoint");

	        registry.addEndpoint("/ws")
	                .setAllowedOriginPatterns("*")
	                .withSockJS();
	    }

	    @Override
	    public void configureMessageBroker(MessageBrokerRegistry registry) {

	        System.out.println("🔥 Enabling broker");

	        registry.enableSimpleBroker("/topic");
	        registry.setApplicationDestinationPrefixes("/app");
	    }

	
    
}
