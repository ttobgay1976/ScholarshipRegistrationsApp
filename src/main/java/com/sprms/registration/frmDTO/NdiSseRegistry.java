package com.sprms.registration.frmDTO;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.sprms.registration.ndi.controller.NdiSseController;

@Component
public class NdiSseRegistry {
	
	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(NdiSseRegistry.class);

	private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
	private final Map<String, ScheduledFuture<?>> futures = new ConcurrentHashMap<>();

	public void add(String thid, SseEmitter emitter) {
		emitters.put(thid, emitter);
	}

	public SseEmitter get(String thid) {
		return emitters.get(thid);
	}

	public void remove(String thid) {
		emitters.remove(thid);
	}

	public void addFuture(String thid, ScheduledFuture<?> future) {
		futures.put(thid, future);
	}

	public void removeExecutor(String thid) {
		ScheduledFuture<?> future = futures.remove(thid);
		if (future != null) {
			future.cancel(true);
		}
	}


	 // ================= 🔥 THIS METHOD GOES HERE =================
    public void send(String thid, String eventName, Object data) {

        SseEmitter emitter = emitters.get(thid);

        if (emitter == null) {
            logger.debug("No SSE emitter found for thid {}", thid);
            return;
        }

        try {
            emitter.send(SseEmitter.event()
                    .name(eventName)
                    .data(data)
                    .id(String.valueOf(System.currentTimeMillis())));

        } catch (Exception ex) {

            logger.info("SSE send failed for thid {}: {}", thid, ex.getMessage());

            // cleanup immediately
            emitters.remove(thid);

            ScheduledFuture<?> future = futures.remove(thid);
            if (future != null) {
                future.cancel(true);
            }

            try {
                emitter.complete();
            } catch (Exception ignored) {}
        }
    }
}
