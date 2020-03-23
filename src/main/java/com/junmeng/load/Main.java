package com.junmeng.load;

import java.util.Calendar;
import java.util.Date;

/**
 * @author james
 * @date 2020/3/17
 */
public class Main {

    public static void main(String[] args) {
        Date dateByAddDay = getDateByAddDay(new Date(), 3);
        System.out.println(dateByAddDay);
    }

    public static Date getDateByAddDay(Date date, Integer numberDay) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, numberDay);
        return cal.getTime();
    }

}
