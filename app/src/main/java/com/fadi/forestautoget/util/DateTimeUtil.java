package com.fadi.forestautoget.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {

    /**
     * Gets the current date of the system (2012-12-28)
     *
     * @return
     */
    public static String getSysTime() {
        return getDate(new Date(System.currentTimeMillis()), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * Gets the current date of the system (2012-12-28)
     *
     * @return
     */
    public static String getSysDateYMD() {
        return getDate(new Date(System.currentTimeMillis()), "yyyy-MM-dd");
    }

    /**
     * Acquisition system current time (format 13:58:00)
     *
     * @return
     */
    public static String getSysTimeHMS() {
        return getDate(new Date(System.currentTimeMillis()), "HH:mm:ss");
    }

    public static int getHourOfDay() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute() {
        return Calendar.getInstance().get(Calendar.MINUTE);

    }

    /**
     * date to get the string
     *
     * @param date
     * @param format ("hh:mm:ss yyyy-MM-dd")
     * @return
     */
    public static String getDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * Format time, if not two digit in front of 0
     *
     * @param x
     * @return
     */
    public static String format(int x) {
        String s = "" + x;
        if (s.length() == 1) {
            s = "0" + s;
        }
        return s;
    }

    /**
     * Time difference, return second
     *
     * @param start
     * @param end
     * @return
     */
    public static long diffTime(String start, String end) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dt_start = formatter.parse(start);
            Date dt_end = formatter.parse(end);
            return (dt_end.getTime() - dt_start.getTime()) / 1000;

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * 获取系统当前日期（格式2012-12-28 13:58:00）
     *
     * @return
     */
    public static String getSysDate() {
        return getDate(new Date(System.currentTimeMillis()), "yyyy-MM-dd HH:mm:ss");
    }

    public static String getSysDate(long time) {
        return getDate(time, "yyyy-MM-dd HH:mm:ss");
    }

    public static String getDate(long time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(time));
    }

    /**
     * 获取天数
     *
     * @return
     */
    public static int getDayOfYear() {
        return Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 获取小时
     *
     * @return
     */
    public static int getHourOfDay(long time) {
        Date date = new Date(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取分钟
     *
     * @return
     */
    public static int getMinute(long time) {
        Date date = new Date(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 毫秒时间
     * Long类型时间转换成时长
     */
    public static String longFormatSting(long time) {
        long hour = time / (60 * 60 * 1000);
        long minute = (time - hour * 60 * 60 * 1000) / (60 * 1000);
        long second = (time - hour * 60 * 60 * 1000 - minute * 60 * 1000) / 1000;
        return (hour == 0 ? "00" : (hour >= 10 ? hour : ("0" + hour))) + ":" + (minute == 0 ? "00" : (minute >= 10 ? minute : ("0" + minute))) + ":" + (second == 0 ? "00" : (second >= 10 ? second : ("0" + second)));
    }

    /**
     * 取出时分秒，并转化为long类型
     * 2017-12-13 15:47:43 -> 15:47:43 -> 56863000
     */
    public static long filterHms(long curTime) {
        String str = getDate(curTime, "HH:mm:ss");
        String time[] = str.split(":");
        int hour = Integer.parseInt(time[0]);
        int minute = Integer.parseInt(time[1]);
        int second = Integer.parseInt(time[2]);
        return hour * 60 * 60 * 1000 + minute * 60 * 1000 + second * 1000;
    }
}
