package com.byc.common.utils;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeFormatter {

    public enum Format{
        SECOND("yyyyMMddHHmmss"),
        SIMPLE("yyyy-MM-dd");
        private String value;
        private SimpleDateFormat format;

        public SimpleDateFormat getFormat() {
            return format;
        }

        Format(String value) {
            this.value = value;
            this.format = new SimpleDateFormat(value);
        }
    }
    /**判断当前时间是否在时间范围内**/
    public static boolean decideDateTime(Date start, Date end) {
        long now = second();
        return start.getTime() <= now && end.getTime() >= now;
    }
    /**当前时间与传入时间的比较，时分10:40:00-12:30:00*/
    public static boolean decideTime(String startTime, String endTime) {
        Calendar nowTime = Calendar.getInstance();
        Calendar sTime = Calendar.getInstance();
        Calendar eTime = Calendar.getInstance();
        nowTime.set(Calendar.SECOND, 0);
        String[] sa = startTime.split(":");
        String[] ea = endTime.split(":");
        if (sa.length > 1 && ea.length > 1) {
            sTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(sa[0]));
            sTime.set(Calendar.MINUTE, Integer.valueOf(sa[1]));
            eTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(ea[0]));
            eTime.set(Calendar.MINUTE, Integer.valueOf(ea[1]));
            return nowTime.compareTo(sTime) > 0 && nowTime.compareTo(eTime) < 0;
        }
        return true;
    }

    //当前时间s
    public static long second() {
        return millis() / 1000;
    }

    public static long second(Date date) {
        return millis(date) / 1000;
    }
    //当前时间ms
    public static long millis() {
        return System.currentTimeMillis();
    }

    public static long millis(Date date) {
        return date.getTime();
    }

    //格式化时间
    public static String formatDate(Format format, Date date) {
        return format.getFormat().format(date);
    }

    public static Long formatStr(Format format, String date) {
        return second(parseStr(format,date));
    }

    public static String formatDate(Format format) {
        return format.getFormat().format(new Date());
    }

    public static Date parseStr(Format format, String dateStr){
        try {
            return format.getFormat().parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }
}
