package com.test.account.controller.dto.time;

import java.time.LocalDateTime;

public record TimeRange(
        LocalDateTime from,
        LocalDateTime to
) {}