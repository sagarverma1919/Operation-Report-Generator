/*
 * Copyright 2018 Expedia, Inc. All rights reserved.
 * EXPEDIA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.expedia.risk.report.generator.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    public static String getPreviousDate(final LocalDateTime localDateTime, final DateTimeFormatter format, final
    String timeZone) {
        final LocalDateTime then = localDateTime.minusDays(7);
        ZoneId newYokZoneId = ZoneId.of(timeZone);
        return then.format(format.withZone(newYokZoneId));
    }
}
