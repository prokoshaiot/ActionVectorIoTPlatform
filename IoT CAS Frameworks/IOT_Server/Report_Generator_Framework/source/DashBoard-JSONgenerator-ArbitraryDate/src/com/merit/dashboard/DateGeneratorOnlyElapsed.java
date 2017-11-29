/**
 * *********************************************************************
 * Software Developed by Merit Systems Pvt. Ltd., No. 42/1, 55/c, Nandi Mansion,
 * 40th Cross, Jayanagar 8th Block Bangalore - 560 070, India Work Created for
 * Merit Systems Private Limited All rights reserved
 *
 * THIS WORK IS SUBJECT TO INDIAN AND INTERNATIONAL COPYRIGHT LAWS AND TREATIES
 * NO PART OF THIS WORK MAY BE USED, PRACTICED, PERFORMED, COPIED, DISTRIBUTED,
 * REVISED, MODIFIED, TRANSLATED, ABRIDGED, CONDENSED, EXPANDED, COLLECTED,
 * COMPILED, LINKED, RECAST, TRANSFORMED OR ADAPTED WITHOUT THE PRIOR WRITTEN
 * CONSENT ANY USE OR EXPLOITATION OF THIS WORK WITHOUT AUTHORIZATION COULD
 * SUBJECT THE PERPETRATOR TO CRIMINAL AND CIVIL LIABILITY.
 * *********************************************************************
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.merit.dashboard;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.merit.dashboard.DBUtil.DBUtilHelper;

/**
 * **************************************************************************************************
 * This Class is used to get Start Date as current Date minus selected time
 * period and current Date
 *
 * @author satyaJay
 * **************************************************************************************************
 */
public class DateGeneratorOnlyElapsed {

    @SuppressWarnings("deprecation")
    public static String[] dateGenerator(String interval) {
        String strDate[] = new String[2];
        Calendar now = Calendar.getInstance();
        Date result = null;
        SimpleDateFormat formatter1 = null;
        try {
            String dateFormate1 = DBUtilHelper.getMetrics_mapping_properties().getProperty("ReferenceTime");
            formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (dateFormate1 != null) {
                Date dt = formatter1.parse(dateFormate1);
                now.set((dt.getYear() + 1900), dt.getMonth(), dt.getDate(), dt.getHours(), dt.getMinutes(), dt.getSeconds());
            } else {
                now = Calendar.getInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Date nowDate = now.getTime();
        String pformatted = formatter1.format(nowDate);
        System.out.println("presentDate= " + pformatted);

        if (interval.equalsIgnoreCase("hour")) {
            now.add(Calendar.HOUR, -1);

        } else if (interval.equalsIgnoreCase("day")) {
            now.add(Calendar.DATE, -1);

        } else if (interval.equalsIgnoreCase("week")) {
            now.add(Calendar.DATE, -7);

        } else if (interval.equalsIgnoreCase("month")) {
            now.add(Calendar.MONTH, -1);

        } else if (interval.equalsIgnoreCase("year")) {
            now.add(Calendar.YEAR, -1);
        }
        result = now.getTime();
        //System.out.println("result :"+result);
        //dFormat.setTimeZone(TimeZone.getTimeZone("UTC +5:30"));
        String formatted = formatter1.format(result);
        System.out.println("Date " + "Before " + interval + " = " + formatted);
        strDate[0] = pformatted;
        strDate[1] = formatted;
        return strDate;

    }

    public static void main(String... a) {
        dateGenerator("week");
    }
}