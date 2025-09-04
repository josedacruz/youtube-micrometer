package com.josedacruz.observability.micrometer.rest;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.observation.annotation.Observed;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Random;

@RestController
public class WorkRestController {

    private final Counter workRequestsCounter;
    private final Timer workDurationTimer;
    private final Random random = new Random();

    public WorkRestController(MeterRegistry meterRegistry) {
        this.workRequestsCounter = Counter.builder("work_requests_total")
                .description("Total number of work requests")
                .register(meterRegistry);

        this.workDurationTimer = Timer.builder("work_duration_seconds")
                .description("Duration of work endpoint execution")
                .publishPercentiles(0.5, 0.95, 0.99)
                .minimumExpectedValue(Duration.ofMillis(10))
                .maximumExpectedValue(Duration.ofSeconds(5))
                .register(meterRegistry);
    }

    @GetMapping("/api/work")
    public String doWork() {
        workRequestsCounter.increment();

        return workDurationTimer.record(() -> {
            try {
                Thread.sleep(100 + random.nextInt(900));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "Work completed!";
        });
    }

    @GetMapping("/api/observed-task")
    @Observed(name = "observed_task", contextualName = "Automatically observed task with @Observed")
    public String observedTask() throws InterruptedException {
        Thread.sleep(200 + random.nextInt(400)); // Simulate work
        return "Observed task completed!";
    }
}
