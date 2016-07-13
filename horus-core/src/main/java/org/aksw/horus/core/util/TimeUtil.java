package org.aksw.horus.core.util;

import java.util.concurrent.TimeUnit;

/**
 * Created by Diego on 7/12/2016.
 */

public class TimeUtil {

    public static String formatTime(long millis) {

        return String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(millis), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }
}
