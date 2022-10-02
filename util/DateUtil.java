package util;

import java.util.Calendar;
import java.util.Date;

/**
 * 有关日期的操作
 */
public class DateUtil {
    //获取年份，返回int类型的年份
    public static int getYear(Date date){
        Calendar calendar= Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }
    //获取月份，返回int类型的月份
    public static int getMonth(Date date){
        Calendar calendar= Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONDAY);
    }

}
