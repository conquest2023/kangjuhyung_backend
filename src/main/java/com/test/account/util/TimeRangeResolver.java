package com.test.account.util;

import com.test.account.controller.dto.time.TimePreset;
import com.test.account.controller.dto.time.TimeRange;

import java.time.LocalDateTime;

public final class TimeRangeResolver {

    private TimeRangeResolver() {

    }

    public static TimeRange resolve(TimePreset preset, LocalDateTime now) {
        return switch (preset) {
            case TODAY -> new TimeRange(
                    now.toLocalDate().atStartOfDay(),
                    now
            );

            case LAST_1_HOUR -> new TimeRange(
                    now.minusHours(1),
                    now
            );

            case LAST_6_HOURS -> new TimeRange(
                    now.minusHours(6),
                    now
            );

            case LAST_7_DAYS -> new TimeRange(
                    now.minusDays(7),
                    now
            );

            case LAST_1_MONTH -> new TimeRange(
                    now.minusMonths(1),
                    now
            );

            case LAST_3_MONTHS -> new TimeRange(
                    now.minusMonths(3),
                    now
            );
        };
    }
}
