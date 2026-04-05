package com.studyplanner.actuator;

import com.studyplanner.repository.StudySessionRepository;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class StudyLoadHealthIndicator implements HealthIndicator {
    private final StudySessionRepository repository;

    public StudyLoadHealthIndicator(StudySessionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Health health() {
        long total = repository.count();

        if (total > 50) {
            // surcharge critique = Health.down()
            return Health.down()
                    .withDetail("totalSessions", total)
                    .withDetail("status", "Surcharge critique")
                    .build();
        } else if (total > 30) {
            // warning = Health.up() + message
            return Health.up()
                    .withDetail("totalSessions", total)
                    .withDetail("status", "Attention, charge élevée")
                    .build();
        } else {
            // tout est ok = Health.up()
            return Health.up()
                    .withDetail("totalSessions", total)
                    .withDetail("status", "OK")
                    .build();
        }
    }
}
