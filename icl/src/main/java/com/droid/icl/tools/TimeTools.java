package com.droid.icl.tools;

import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2016/3/14.
 */
public class TimeTools {

    private static final long Mills = 1000;
    private static final long Minutes = Mills * 60;
    private static final long Hours = Minutes * 60;
    private static final long Day = Hours * 24;

    public static String convertTime(String timeString) {
        try {
            timeString = timeString.replaceAll("  ", " ");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long curretTimeMills = System.currentTimeMillis();
            long timeMills = sdf.parse(timeString).getTime();
            long timeToNow = curretTimeMills - timeMills;

            if (timeToNow < Minutes)
                return "刚刚";
            if (timeToNow < Hours) {
                int min = (int) (timeToNow / Minutes);
                return min + "分钟前";
            }
            if (timeToNow < Day) {
                int h = (int) (timeToNow / Hours);
                return h + "小时前";
            }
            if (timeToNow < Day * 10) {
                int d = (int) (timeToNow / Day);
                return d + "天前";
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
