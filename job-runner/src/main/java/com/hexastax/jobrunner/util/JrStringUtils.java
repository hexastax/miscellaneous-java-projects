package com.hexastax.jobrunner.util;

/**
 * Contains string utilities.
 * 
 * @author dgoldenberg
 */
public class JrStringUtils {

  private JrStringUtils() {
  }

  /**
   * Converts time in milliseconds to a user-friendly String that lists hours, minutes, seconds, and
   * milliseconds.
   * 
   * @param time
   *          the time in milliseconds.
   * @return the user-friendly string
   */
  public static String millisecondsToString(long time) {
    if (time == 0L) {
      return "0 ms.";
    } else {
      final StringBuilder result = new StringBuilder();
      if (time < 0L) {
        result.append('-');
        time = -time;
      }

      final long milliseconds = time % 1000;
      final long seconds = (time / 1000L) % 60;
      final long minutes = (time / 60000L) % 60;
      final long hours = (time / 3600000L) % 24;
      final long days = time / 86400000L;

      String sep = timeToString(result, days, "day", "");
      sep = timeToString(result, hours, "hour", sep);
      sep = timeToString(result, minutes, "minute", sep);
      sep = timeToString(result, seconds, "second", sep);
      if (milliseconds > 0) {
        result.append(sep).append(milliseconds).append(" ms.");
      }
      return result.toString();
    }
  }

  private static final String timeToString(final StringBuilder buffer, final long value, final String label, final String sep) {
    if (value == 1L) {
      buffer.append(sep).append(value).append(' ').append(label);
      return "; ";
    } else if (value > 0L) {
      buffer.append(sep).append(value).append(' ').append(label).append('s');
      return "; ";
    } else {
      return sep;
    }
  }

}
