package com.example.lzc.daliylife.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lzc on 2017/3/15.
 */

public class DateTimeFormat {
    public static String formatDateTime(String dateTime) {
        String finalDateTime = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat finalSdf=new SimpleDateFormat("MM-dd HH:mm");
        try {
            Date date = sdf.parse(dateTime);
            finalDateTime = finalSdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            finalDateTime = dateTime;
        }
        return finalDateTime;
    }

    ;
}
