package tech.xigam.onelineofcode.utils;

import org.apache.commons.io.IOUtils;

import java.net.URL;
import java.time.Duration;
import java.util.Base64;

public final class EncodingUtil {
    public static String base64Encode(String encode) {
        return Base64.getUrlEncoder().encodeToString(encode.getBytes());
    }

    public static String base64Decode(String decode) {
        return new String(Base64.getUrlDecoder().decode(decode));
    }
    
    public static String base64EncodeImage(String imageUrl) {
        try {
            byte[] imageBytes = IOUtils.toByteArray(new URL(imageUrl));
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);
        } catch (Exception ignored) {
            return EncodingUtil.base64Encode(imageUrl);
        }
    }

    public static String formatDuration(long ms) {
        final Duration duration = Duration.ofMillis(ms);
        final int hours = duration.toHoursPart();
        final int minutes = duration.toMinutesPart();
        final int seconds = duration.toSecondsPart();
        if (hours > 0) {
            return String.format("%d hours, %d minutes, and %d seconds", hours, minutes, seconds);
        }
        if (minutes > 0) {
            return String.format("%d minutes and %d seconds", minutes, seconds);
        }
        return String.format("%d seconds", seconds);
    }

    public static String formatPeriod(long sec) {
        final Duration duration = Duration.ofSeconds(sec);
        int days = (int) duration.toDaysPart(); // This will be subtracted from.
        final int years = (int) Math.floor(days / 365f);
        days -= years * 365;
        final int months = (int) Math.floor(days / 30f);
        days -= months * 30;
        final int weeks = (int) Math.floor(days / 7f);
        days -= weeks * 7;
        final int hours = duration.toHoursPart();
        final int minutes = duration.toMinutesPart();
        final int seconds = duration.toSecondsPart();
        if (years > 0) {
            return String.format("%d years, %d months, %d weeks, %d days, %d hours, %d minutes and %d seconds", years, months, weeks, days, hours, minutes, seconds);
        }
        if (months > 0) {
            return String.format("%d months, %d weeks, %d days, %d hours, %d minutes and %d seconds", months, weeks, days, hours, minutes, seconds);
        }
        if (weeks > 0) {
            return String.format("%d weeks, %d days, %d hours, %d minutes and %d seconds", weeks, days, hours, minutes, seconds);
        }
        if (days > 0) {
            return String.format("%d days, %d hours, %d minutes and %d seconds", days, hours, minutes, seconds);
        }
        if (hours > 0) {
            return String.format("%d hours, %d minutes, and %d seconds", hours, minutes, seconds);
        }
        if (minutes > 0) {
            return String.format("%d minutes and %d seconds", minutes, seconds);
        }
        return String.format("%d seconds", seconds);
    }
    
    public static String consolidate(String string) {
        string = string.toLowerCase();
        
        var parts = string.split(" ");
        var builder = new StringBuilder();
        for (var part : parts)
            builder.append(part).append("_");
        return builder.toString();
    }
}
