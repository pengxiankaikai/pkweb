package com.pk.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by simba on 2017/1/14 0014.
 */
public class DateUtils {
    private static final String date_format = "yyyy-MM-dd HH:mm:ss";

    private static ThreadLocal threadlocal = new ThreadLocal() {
        protected synchronized Object initialValue() {
            return new SimpleDateFormat(date_format);
        }
    };

    /**
     * 获取时间格式：yyyy-MM-dd HH:mm:ss
     * @return DateFormat
     */
    public static DateFormat getdateformat() {
        return (DateFormat) threadlocal.get();
    }

    /**
     * 将时间格式的字符串转化为时间
     * @param textdate 时间格式的字符串(转化格式为：yyyy-MM-dd HH:mm:ss)
     * @return
     * @throws ParseException
     */
    public static Date parse(String textdate) throws ParseException {
        return getdateformat().parse(textdate);
    }

    /**
     * 将时间转换为时间格式的字符串
     * @param date 待转化时间 (转化格式为：yyyy-MM-dd HH:mm:ss)
     * @return
     * @throws ParseException
     */
    public static String format(Date date) throws ParseException {
        return getdateformat().format(date);
    }


    private static final String sdf = "yyyyMMdd";

    private static ThreadLocal threadlocalSdf = new ThreadLocal() {
        protected synchronized Object initialValue() {
            return new SimpleDateFormat(sdf);
        }
    };

    public static DateFormat getdateformatSdf() {
        return (DateFormat) threadlocalSdf.get();
    }

    public static Date parseSdf(String textdate) throws ParseException {
        return getdateformatSdf().parse(textdate);
    }

    public static String formatSdf(Date date) throws ParseException {
        return getdateformatSdf().format(date);
    }

    private static final String sdf2 = "yyyy-MM-dd";

    private static ThreadLocal threadlocalSdf2 = new ThreadLocal() {
        protected synchronized Object initialValue() {
            return new SimpleDateFormat(sdf2);
        }
    };

    public static DateFormat getdateformatSdf2() {
        return (DateFormat) threadlocalSdf2.get();
    }

    public static Date parseSdf2(String textdate) throws ParseException {
        return getdateformatSdf2().parse(textdate);
    }

    public static String formatSdf2(Date date) throws ParseException {
        return getdateformatSdf2().format(date);
    }



}
