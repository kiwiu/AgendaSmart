package com.example.agendasmart;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
public class DateUtils {
    public static long calculateTimeDiff(String taskDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date taskDateTime = sdf.parse(taskDate);

            Calendar taskCalendar = Calendar.getInstance();
            taskCalendar.setTime(taskDateTime);

            Calendar currentCalendar = Calendar.getInstance();

            return taskCalendar.getTimeInMillis() - currentCalendar.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
