package com.sprms.registration.frmbean;

import java.io.IOException;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;

@Component
public class NdiSseRegistry {

	private static final Logger logger = LoggerFactory.getLogger(NdiSseRegistry.class);

	private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

	private final Map<String, ScheduledFuture<?>> futures = new ConcurrentHashMap<>();

	// ✅ PUT IT HERE (class level, not inside method)
	private final Set<String> cleaned = ConcurrentHashMap.newKeySet();

	private final Map<String, AtomicInteger> failCounts = new ConcurrentHashMap<>();

	/**
	 * Register emitter
	 */
	public void add(String thid, SseEmitter emitter) {

		emitters.put(thid, emitter);

		logger.info("SSE emitter registered. thid={}", thid);

		emitter.onCompletion(() -> {

			logger.info("SSE completed. thid={}", thid);

			cleanup(thid);
		});

		emitter.onTimeout(() -> {

			logger.info("SSE timeout. thid={}", thid);

			cleanup(thid);

			try {
				emitter.complete();
			} catch (Exception ignored) {
			}
		});

		emitter.onError(ex -> {

			logger.warn("SSE error. thid={}, message={}", thid, ex.getMessage());

			cleanup(thid);
		});
	}

	/**
	 * Retrieve emitter
	 */
	public SseEmitter get(String thid) {
		return emitters.get(thid);
	}

	/**
	 * Register polling future
	 */
	public void addFuture(String thid, ScheduledFuture<?> future) {

		futures.put(thid, future);
	}

	/**
	 * Send SSE event
	 */
	public void send(String thid, String eventName, Object data) {

		SseEmitter emitter = emitters.get(thid);

		if (emitter == null) {

			logger.debug("No active SSE emitter found for thid={}", thid);

			return;
		}

		try {

			emitter.send(SseEmitter.event().id(String.valueOf(System.currentTimeMillis())).name(eventName).data(data));

		} catch (IOException ex) {

			logger.info("Client disconnected. thid={}", thid);

			cleanup(thid);

		} catch (Exception ex) {

			logger.error("SSE send failed. thid={}", thid, ex);

			cleanup(thid);
		}
	}

	/**
	 * Remove emitter only
	 */
	public void remove(String thid) {

		emitters.remove(thid);
	}

	/**
	 * Remove future only
	 */
	public void removeExecutor(String thid) {

		ScheduledFuture<?> future = futures.remove(thid);

		if (future != null) {
			future.cancel(true);
		}
	}

	/**
	 * Complete cleanup
	 */
	public void cleanup(String thid) {

		SseEmitter emitter = emitters.remove(thid);

		// prevent duplicate cleanup
		if (emitter == null) {
			return;
		}

		try {
			emitter.complete();
			
		} catch (Exception ex) {
			logger.debug("Ignoring SSE cleanup exception. thid={}, msg={}", thid, ex.getMessage());
		}

		// 🔥 IMPORTANT: cancel heartbeat task (IF YOU HAVE IT)
		ScheduledFuture<?> future = futures.remove(thid);
		if (future != null) {
			future.cancel(true);
		}

		logger.info("@@@SSE cleaned safely thid={}", thid);
	}

	public void register(String thid, SseEmitter emitter) {
		emitters.put(thid, emitter);
	}

	public boolean exists(String thid) {
		return emitters.containsKey(thid);
	}

	public void removeOnly(String thid) {
		emitters.remove(thid);
		failCounts.remove(thid);
		logger.info("@@@SSE removed from registry thid={}", thid);
	}
}