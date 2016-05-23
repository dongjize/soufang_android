package com.dong.soufang.util;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    private static Calendar c = Calendar.getInstance();
    public static long DAY = 24 * 60 * 60 * 1000;

    /**
     * 获取当前日期时间
     *
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getNowDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        return formatter.format(curDate);
    }

    /**
     * 获取time以前的日期时间
     *
     * @param time
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getLastDateTime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis() - time);// 获取当前时间
        return formatter.format(curDate);
    }

    /**
     * 获取小时
     *
     * @return 小时
     */
    public static int getHour() {
        return c.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取分钟
     *
     * @return 分钟
     */
    public static int getMinute() {
        return c.get(Calendar.MINUTE);
    }

    /**
     * 获取年
     *
     * @return 年
     */
    public static int getYear() {
        return c.get(Calendar.YEAR);
    }

    /**
     * 获取月
     *
     * @return 月
     */
    public static int getMonth(Date date) {
        c.setTime(date);
        return c.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取月
     *
     * @return 月
     */
    public static int getMonth() {
        return c.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取月
     *
     * @return 月
     */
    public static String getMonthS() {
        int month = c.get(Calendar.MONTH) + 1;
        return month < 10 ? "0" + month : "" + month;
    }

    /**
     * 获取日
     *
     * @return 日
     */
    public static int getDay() {
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取日
     *
     * @return 日
     */
    public static int getDay(Date date) {
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 日期变量转成对应的星期字符串
     *
     * @param date
     * @return
     */
    public static String getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        String dayOfWeek = "";
        calendar.setTime(date);
        int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayIndex < 1 || dayIndex > 7) {
            return null;
        }
        switch (dayIndex) {
            case 1:
                dayOfWeek = "星期天";
                break;
            case 2:
                dayOfWeek = "星期一";
                break;
            case 3:
                dayOfWeek = "星期二";
                break;
            case 4:
                dayOfWeek = "星期三";
                break;
            case 5:
                dayOfWeek = "星期四";
                break;
            case 6:
                dayOfWeek = "星期五";
                break;
            case 7:
                dayOfWeek = "星期六";
                break;
            default:
                break;
        }
        return dayOfWeek;
    }

    /**
     * 获取星期
     *
     * @return 星期
     */
    public static String getDayOfWeek() {
        String dayOfWeek = "";
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                dayOfWeek = "星期天";
                break;
            case 2:
                dayOfWeek = "星期一";
                break;
            case 3:
                dayOfWeek = "星期二";
                break;
            case 4:
                dayOfWeek = "星期三";
                break;
            case 5:
                dayOfWeek = "星期四";
                break;
            case 6:
                dayOfWeek = "星期五";
                break;
            case 7:
                dayOfWeek = "星期六";
                break;
            default:
                break;
        }
        return dayOfWeek;
    }

    /**
     * 获取星期
     *
     * @return 星期
     */
    public static String getDayOfWeek(int day) {
        String dayOfWeek = "";
        switch (day) {
            case 0:
                dayOfWeek = "星期天";
                break;
            case 1:
                dayOfWeek = "星期一";
                break;
            case 2:
                dayOfWeek = "星期二";
                break;
            case 3:
                dayOfWeek = "星期三";
                break;
            case 4:
                dayOfWeek = "星期四";
                break;
            case 5:
                dayOfWeek = "星期五";
                break;
            case 6:
                dayOfWeek = "星期六";
                break;
            default:
                break;
        }
        return dayOfWeek;
    }

    public static Date strToDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = null;
        try {
            dt = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dt;
    }

    /**
     * YYYYMMDDhhmmss转为 format格式时间
     *
     * @param time
     * @return
     * @throws Exception
     */
    @SuppressLint("SimpleDateFormat")
    public static String changeTime(String time, String format) throws Exception {
        String newDate = "";
        if (TextUtils.isEmpty(time)) {
            return "";
        }
        try {
            SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat newFormat = new SimpleDateFormat(format);
            Date date = oldFormat.parse(time);
            newDate = newFormat.format(date);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return newDate;
    }

    /**
     * 格式化时间（输出类似于 刚刚, 4分钟前, 一小时前, 昨天这样的时间）
     *
     * @param time    需要格式化的时间 如"2014-07-14 19:01:45"
     * @param pattern 输入参数time的时间格式 如:"yyyy-MM-dd HH:mm:ss"
     *                <p/>如果为空则默认使用"yyyy-MM-dd HH:mm:ss"格式
     * @return time为null，或者时间格式不匹配，输出空字符""
     */
    public static String formatDisplayTime(String time, String pattern) {
        String display = "";
        int tMin = 60 * 1000;
        int tHour = 60 * tMin;
        int tDay = 24 * tHour;

        if (time != null) {
            try {
                Date tDate = new SimpleDateFormat(pattern).parse(time);
                Date today = new Date();
                SimpleDateFormat thisYearDf = new SimpleDateFormat("yyyy");
                SimpleDateFormat todayDf = new SimpleDateFormat("yyyy-MM-dd");
                Date thisYear = new Date(thisYearDf.parse(thisYearDf.format(today)).getTime());
                Date yesterday = new Date(todayDf.parse(todayDf.format(today)).getTime());
                Date beforeYes = new Date(yesterday.getTime() - tDay);
                if (tDate != null) {
                    SimpleDateFormat halfDf = new SimpleDateFormat("MM月dd日");
                    long dTime = today.getTime() - tDate.getTime();
                    if (tDate.before(thisYear)) {
                        display = new SimpleDateFormat("yyyy年MM月dd日").format(tDate);
                    } else {

                        if (dTime < tMin) {
                            display = "刚刚";
                        } else if (dTime < tHour) {
                            display = (int) Math.ceil(dTime / tMin) + "分钟前";
                        } else if (dTime < tDay && tDate.after(yesterday)) {
                            display = (int) Math.ceil(dTime / tHour) + "小时前";
                        } else if (tDate.after(beforeYes) && tDate.before(yesterday)) {
                            display = "昨天" + new SimpleDateFormat("HH:mm").format(tDate);
                        } else {
                            display = halfDf.format(tDate);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return display;
    }

    public static int compareDate(String date1, String date2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date dt1 = df.parse(date1);
            Date dt2 = df.parse(date2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
}
