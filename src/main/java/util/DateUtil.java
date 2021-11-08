package util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    /**
     * Method increase or decrease date
     * @param date - base date
     * @param days - amount of days to increase/decrease
     * @return result date
     */
    public Date increaseDate(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    /**
     * Method removes time form date object
     * @param date - base date
     * @return - date with 00:00:00.000
     */
    public Date removeTimeFromDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
