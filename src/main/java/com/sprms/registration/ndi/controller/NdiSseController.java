package com.sprms.registration.ndi.controller;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.sprms.registration.frmbean.NdiSseRegistry;

@RestController
@RequestMapping("/ndi")
public class NdiSseController {

	private static final Logger logger = LoggerFactory.getLogger(NdiSseController.class);

	// CALL REPO
	private final NdiSseRegistry registry;

	// constructor
	public NdiSseController(NdiSseRegistry ndiSseRegistry) {
		this.registry = ndiSseRegistry;
	}

	// Shared scheduler (good approach)
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);

	// new code
	@GetMapping("/events_OOOLLLDDD/{thid}")
	public SseEmitter stream_Ooolldd(@PathVariable String thid,
			@RequestHeader(value = "Last-Event-ID", required = false) String lastEventId) {

		logger.info("SSE connected thid={}", thid);

		SseEmitter emitter = new SseEmitter(0L);

		registry.register(thid, emitter);

		AtomicInteger failCount = new AtomicInteger(0);

		ScheduledFuture<?> heartbeat = scheduler.scheduleWithFixedDelay(() -> {

			// prevent zombie execution after cleanup
			if (!registry.exists(thid)) {
				return;
			}

			try {
				emitter.send(SseEmitter.event().id(String.valueOf(System.currentTimeMillis())).name("heartbeat")
						.data("ping").reconnectTime(5000));

				failCount.set(0);

			} catch (Exception ex) {

				int count = failCount.incrementAndGet();

				logger.warn("Heartbeat failed thid={}, count={}", thid, count);

				if (count >= 3) {
					logger.warn("SSE unstable → cleanup {}", thid);
					registry.cleanup(thid); // 🔥 FIXED
				}
			}

		}, 15, 15, TimeUnit.SECONDS);

		registry.addFuture(thid, heartbeat);

		// ================= LIFECYCLE HANDLERS =================

		emitter.onCompletion(() -> {
			logger.info("SSE completed thid={}", thid);
			registry.cleanup(thid); // 🔥 FIXED
		});

		emitter.onTimeout(() -> {
			logger.info("SSE timeout thid={}", thid);
			registry.cleanup(thid);
		});

		emitter.onError(ex -> {
			logger.warn("SSE error thid={}, msg={}", thid, ex.getMessage());
			registry.cleanup(thid); // 🔥 FIXED
		});

		if (lastEventId != null) {
			logger.info("Reconnected thid={} lastEventId={}", thid, lastEventId);
		}

		return emitter;
	}

	@GetMapping(value = "/events/{thid}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter stream(@PathVariable String thid,
			@RequestHeader(value = "Last-Event-ID", required = false) String lastEventId) {

		logger.info("SSE connected thid={}", thid);

		SseEmitter emitter = new SseEmitter(0L);

		registry.register(thid, emitter);

		// =====================================================
		// LIFECYCLE HANDLERS
		// =====================================================

		emitter.onCompletion(() -> {
			logger.info("SSE completed thid={}", thid);
			registry.cleanup(thid);
		});

		emitter.onTimeout(() -> {
			logger.info("SSE timeout thid={}", thid);
			registry.cleanup(thid);
		});

		emitter.onError(ex -> {
			logger.warn("SSE error thid={}, msg={}", thid, ex.getMessage());

			registry.cleanup(thid);
		});

		// =====================================================
		// INITIAL EVENT
		// =====================================================

		try {

			emitter.send(SseEmitter.event().name("connected").data("connected").reconnectTime(5000));

		} catch (Exception ex) {

			logger.warn("Initial SSE send failed thid={}, msg={}", thid, ex.getMessage());

			registry.cleanup(thid);

			return emitter;
		}

		// =====================================================
		// HEARTBEAT
		// =====================================================

		ScheduledFuture<?> heartbeat = scheduler.scheduleWithFixedDelay(() -> {

			if (!registry.exists(thid)) {
				return;
			}

			try {

				emitter.send(SseEmitter.event().id(String.valueOf(System.currentTimeMillis())).name("heartbeat")
						.data("ping").reconnectTime(5000));

			} catch (Exception ex) {

				logger.warn("SSE heartbeat failed thid={}, msg={}", thid, ex.getMessage());

				// Connection is already dead.
				registry.cleanup(thid);
			}

		}, 15, 15, TimeUnit.SECONDS);

		registry.addFuture(thid, heartbeat);

		// =====================================================
		// RECONNECTION
		// =====================================================

		if (lastEventId != null) {

			logger.info("SSE reconnected thid={}, lastEventId={}", thid, lastEventId);
		}

		return emitter;
	}
}