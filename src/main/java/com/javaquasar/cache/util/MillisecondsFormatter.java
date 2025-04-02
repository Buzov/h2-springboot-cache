package com.javaquasar.cache.util;

public class MillisecondsFormatter {

    private static final String PATTERN_dd_hh_mm_ss_ms = "%d day(s) %02d:%02d:%02d.%03d";
    private static final String PATTERN_hh_mm_ss_ms = "%02d:%02d:%02d.%03d";
    private static final String PATTERN_mm_ss_ms = "%02d:%02d.%03d";
    private static final String PATTERN_ss_ms = "%02d.%03d";

    private MillisecondsFormatter() {
    }

    public static String format(long aMilliseconds) {
        int milliseconds = (int) aMilliseconds % 1000;
        int seconds = (int) aMilliseconds / 1000 % 60;
        int minutes = (int) aMilliseconds / (1000 * 60) % 60;
        int hours = (int) aMilliseconds / (1000 * 60 * 60) % 24;
        int days = (int) aMilliseconds / (1000 * 60 * 60 * 24);

        String lResult;

        if (days != 0) {
            lResult = String.format(PATTERN_dd_hh_mm_ss_ms, days, hours, minutes, seconds, milliseconds);
        } else if (hours != 0) {
            lResult = String.format(PATTERN_hh_mm_ss_ms, hours, minutes, seconds, milliseconds);
        } else if (minutes != 0) {
            lResult = String.format(PATTERN_mm_ss_ms, minutes, seconds, milliseconds);
        } else {
            lResult = String.format(PATTERN_ss_ms, seconds, milliseconds);
        }

        return lResult;
    }

}
