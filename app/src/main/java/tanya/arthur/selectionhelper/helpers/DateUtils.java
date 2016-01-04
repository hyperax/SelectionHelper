package tanya.arthur.selectionhelper.helpers;

import android.support.annotation.NonNull;

import org.joda.time.DateTimeConstants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    private DateUtils() {
    }

    public static long getCurrentSeconds() {
        return getCurrentMillis() / DateTimeConstants.MILLIS_PER_SECOND;
    }

    public static long getCurrentMillis() {
        return System.currentTimeMillis();
    }

    public static long getCurrentNanos() {
        return System.nanoTime();
    }

    @NonNull
    public static String format(long date, String formatPattern) {
        Date dateTime = new Date(date);
        SimpleDateFormat sdf = new SimpleDateFormat(formatPattern, new Locale("ru"));
        return sdf.format(dateTime);
    }
}
