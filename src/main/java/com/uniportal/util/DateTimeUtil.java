package com.uniportal.util;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateTimeUtil {
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
    
    public static Date parseDate(String dateStr) {
        try {
            java.util.Date parsed = DATE_FORMAT.parse(dateStr);
            return new Date(parsed.getTime());
        } catch (ParseException e) {
            return null;
        }
    }
    
    public static String formatDate(Date date) {
        if (date == null) return "";
        return DATE_FORMAT.format(date);
    }
    
    public static Time parseTime(String timeStr) {
        try {
            // Append seconds if not provided
            if (timeStr.length() == 5) {
                timeStr += ":00";
            }
            java.util.Date parsed = TIME_FORMAT.parse(timeStr);
            return new Time(parsed.getTime());
        } catch (ParseException e) {
            return null;
        }
    }
    
    public static String formatTime(Time time) {
        if (time == null) return "";
        return TIME_FORMAT.format(time);
    }
}
