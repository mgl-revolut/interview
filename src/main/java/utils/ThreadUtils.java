package utils;

import java.util.Random;

public final class ThreadUtils {

    /**
     * Makes the current thread to yield its current time slot and sleep for a random number of milliseconds that
     * satisfies the condition: minMilliseconds <= SLEEP_TIME < maxMilliseconds.
     * @param minMilliseconds The minimum number of milliseconds
     * @param maxMilliseconds The maximum number of milliseconds
     */
    public static void sleepRandomInterval(long minMilliseconds, long maxMilliseconds) {

        long millisToSleep = minMilliseconds + (Math.abs(sRandom.nextLong()) % (maxMilliseconds - minMilliseconds));

        try {
            Thread.sleep(millisToSleep);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static final Random sRandom = new Random(System.currentTimeMillis());
}
