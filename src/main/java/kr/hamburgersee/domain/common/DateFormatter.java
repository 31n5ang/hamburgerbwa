package kr.hamburgersee.domain.common;

import java.time.Duration;
import java.time.LocalDateTime;

public final class DateFormatter {
    private static final String MINS = "분";
    private static final String HOURS = "시간";
    private static final String DAYS = "일";
    private static final String MONS = "달";
    private static final String YEARS = "년";
    private static final String AGO = " 전";

    public static String getAgoFormatted(LocalDateTime target, LocalDateTime now) {
        Duration ago = Duration.between(target, now);

        if (ago.toHours() < 1) {
            return ago.toMinutes() + MINS + AGO;
        }

        if (ago.toDays() < 1) {
            return ago.toHours() + HOURS + AGO;
        }

        if (ago.toDays() < 31) {
            return ago.toDays() + DAYS + AGO;
        }

        if (ago.toDays() < 365) {
            return ago.toDays() / 30 + MONS + AGO;
        }

        return ago.toDays() / 365 + YEARS + AGO;
    }
}
