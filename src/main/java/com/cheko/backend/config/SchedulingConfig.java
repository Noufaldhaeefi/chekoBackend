package com.cheko.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SchedulingConfig {
    // This configuration enables Spring's scheduled task execution capability
    // Used for the best seller recalculation scheduled task in ItemService
}

