package com.sprms.registration.frmbean;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class NdiSseRegistry {

	private static final Logger logger = LoggerFactory.getLogger(NdiSseRegistry.class);

	private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

	private final Map<String, ScheduledFuture<?>> futures = new ConcurrentHashMap<>();

	// =====================================================
	// REGISTER
	// =====================================================

	public void register(String thid, SseEmitter emitter) {

		SseEmitter oldEmitter = emitters.put(thid, emitter);

		if (oldEmitter != null && oldEmitter != emitter) {
			logger.debug("Replacing existing SSE emitter. thid={}", thid);
		}

		logger.info("SSE emitter registered. thid={}", thid);
	}

	// =====================================================
	// GET
	// =====================================================

	public SseEmitter get(String thid) {

		return emitters.get(thid);
	}

	// =====================================================
	// EXISTS
	// =====================================================

	public boolean exists(String thid) {

		return emitters.containsKey(thid);
	}

	// =====================================================
	// HEARTBEAT FUTURE
	// =====================================================

	public void addFuture(String thid, ScheduledFuture<?> future) {

		ScheduledFuture<?> oldFuture = futures.put(thid, future);

		if (oldFuture != null) {
			oldFuture.cancel(false);
		}
	}

	// =====================================================
	// SEND EVENT
	// =====================================================

	public void send_OLD(String thid, String eventName, Object data) {

		SseEmitter emitter = emitters.get(thid);

		if (emitter == null) {

			logger.debug("No active SSE emitter. thid={}", thid);

			return;
		}

		try {

			emitter.send(SseEmitter.event().id(String.valueOf(System.currentTimeMillis())).name(eventName).data(data));

		} catch (IOException ex) {

			logger.info("SSE client disconnected. thid={}", thid);

			cleanup(thid);

		} catch (Exception ex) {

			logger.warn("SSE send failed. thid={}, msg={}", thid, ex.getMessage());

			cleanup(thid);
		}
	}

	// NEW CODE
	public void send(String thid, String eventName, Object data) {

		SseEmitter emitter = emitters.get(thid);

		if (emitter == null) {

			logger.info("No active SSE connection. thid={}, event={}", thid, eventName);

			return;
		}

		try {

			emitter.send(SseEmitter.event().id(String.valueOf(System.currentTimeMillis())).name(eventName).data(data));

			logger.debug("SSE event sent. thid={}, event={}", thid, eventName);

		} catch (IOException ex) {

			/*
			 * Normal situation when: - browser closes - page changes - EventSource
			 * reconnects - proxy terminates connection
			 */

			logger.warn("SSE client disconnected. thid={}, event={}, msg={}", thid, eventName, ex.getMessage());

			cleanup(thid);

		} catch (IllegalStateException ex) {

			logger.warn("SSE emitter already completed. thid={}, event={}, msg={}", thid, eventName, ex.getMessage());

			cleanup(thid);

		} catch (Exception ex) {

			logger.warn("Unexpected SSE send failure. thid={}, event={}, msg={}", thid, eventName, ex.getMessage());

			cleanup(thid);
		}
	}

	// =====================================================
	// CLEANUP
	// =====================================================

	public void cleanup(String thid) {

		SseEmitter removed = emitters.remove(thid);

		ScheduledFuture<?> future = futures.remove(thid);

		if (future != null) {
			future.cancel(false);
		}

		if (removed != null) {

			logger.info("@@@SSE cleaned safely thid={}", thid);
		}
	}

	// =====================================================
	// NORMAL COMPLETION
	// =====================================================

	public void complete(String thid) {

		SseEmitter emitter = emitters.remove(thid);

		ScheduledFuture<?> future = futures.remove(thid);

		if (future != null) {
			future.cancel(false);
		}

		if (emitter == null) {
			return;
		}

		try {

			emitter.complete();

		} catch (Exception ex) {

			logger.debug("Ignoring SSE completion error. thid={}, msg={}", thid, ex.getMessage());
		}
	}
}