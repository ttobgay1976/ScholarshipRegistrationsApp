package com.sprms.registration.api.sessionStore;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpSession;

@Component
public class NdiUserSessionStore {

	private final Map<String, HttpSession> sessionMap = new ConcurrentHashMap<>();

    public void register(String thid, HttpSession session) {
        sessionMap.put(thid, session);
    }

    public HttpSession getSessionByThid(String thid) {
        return sessionMap.get(thid);
    }

    public void remove(String thid) {
        sessionMap.remove(thid);
    }
    
}
