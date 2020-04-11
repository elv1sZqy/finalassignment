package xzy.lovelybj.finalassignment.util;

import java.util.Calendar;

/**
 * @ClassName DateUtil
 * @Author Elv1s
 * @Date 2020/4/11 15:28
 * @Description:
 */
public class DateUtil {
    private static Calendar calendar = Calendar.getInstance();

    public static int getCurrentWeakInThisYear(){
        //设置星期一为一周开始的第一天
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        //获得当前日期属于今年的第几周
        int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        return weekOfYear;
    }

    public static int getCurrentMonthInThisYear(){
        //获得当前的年
        return calendar.get(Calendar.MONTH);
    }
    public static int getCurrentYear(){
        //获得当前的年
        return calendar.get(Calendar.YEAR);
    }
}
