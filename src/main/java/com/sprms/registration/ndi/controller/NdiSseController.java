package com.sprms.registration.ndi.controller;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.sprms.registration.frmDTO.NdiSseRegistry;

@RestController
@RequestMapping("/ndi")
public class NdiSseController {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(NdiSseController.class);

	@Autowired
	private NdiSseRegistry registry;

	// Shared scheduler (IMPORTANT: avoid thread per request leak)
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);

	@GetMapping("/events/{thid}")
	public SseEmitter stream(@PathVariable String thid,
			@RequestHeader(value = "Last-Event-ID", required = false) String lastEventId) {

		logger.info("SSE connected thid={}, lastEventId={}", thid, lastEventId);

		SseEmitter emitter = new SseEmitter(0L);

		registry.add(thid, emitter);

		AtomicBoolean closed = new AtomicBoolean(false);

		ScheduledFuture<?>[] futureHolder = new ScheduledFuture<?>[1];

		// ================= CLEANUP =================
		Runnable cleanup = () -> {
			if (closed.compareAndSet(false, true)) {

				ScheduledFuture<?> f = futureHolder[0];
				if (f != null) {
					f.cancel(true);
				}

				registry.removeExecutor(thid);
				registry.remove(thid);

				try {
					emitter.complete();
				} catch (Exception ignored) {
				}
			}
		};

		emitter.onCompletion(cleanup);
		emitter.onTimeout(cleanup);
		emitter.onError(e -> cleanup.run());

		// ================= HEARTBEAT =================
		futureHolder[0] = scheduler.scheduleAtFixedRate(() -> {

			if (closed.get())
				return;

			try {
				emitter.send(SseEmitter.event().id(String.valueOf(System.currentTimeMillis())).name("heartbeat")
						.data("ping").reconnectTime(5000));

			} catch (Exception ex) {

				logger.info("SSE disconnected thid {}: {}", thid, ex.getMessage());

				cleanup.run();
			}

		}, 0, 15, TimeUnit.SECONDS);

		registry.addFuture(thid, futureHolder[0]);

		// ================= RECONNECT SUPPORT =================
		if (lastEventId != null) {
			logger.info("Reconnected thid={} from lastEventId={}", thid, lastEventId);

			// OPTIONAL: replay missed events
			// replayService.replay(thid, lastEventId, emitter);
		}

		return emitter;
	}

}
