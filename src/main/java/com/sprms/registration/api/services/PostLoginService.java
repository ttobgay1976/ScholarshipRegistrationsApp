package com.sprms.registration.api.services;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class PostLoginService {

    private final Map<String, Boolean> readyMap = new ConcurrentHashMap<>();

    // ✅ WRITE (THIS WAS MISSING)
    public void markReady(String thid) {
    	
    	System.out.println("SERVICE INSTANCE: " + System.identityHashCode(this));
        readyMap.put(thid, true);
    }

    // ✅ READ   
    public boolean isReady(String thid) {
    	
        System.out.println("READING READY FOR: " + thid + " → " + readyMap.get(thid));
                
        return readyMap.getOrDefault(thid, false);
    }
    
}
