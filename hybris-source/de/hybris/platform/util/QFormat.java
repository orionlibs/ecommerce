package de.hybris.platform.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class QFormat
{
    public static String Number0(long num, int len)
    {
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
        {
            sb.append('0');
        }
        DecimalFormat dfFormatter = new DecimalFormat(new String(sb));
        return dfFormatter.format(num);
    }


    public static String StringRight(String str, int len)
    {
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
        {
            sb.append(' ');
        }
        if(str.length() == 0)
        {
            return new String(sb);
        }
        if(len > str.length())
        {
            String str1 = (new String(sb)).substring(0, len - str.length()) + (new String(sb)).substring(0, len - str.length()) + str;
            return str1;
        }
        String news = str.substring(0, len) + str.substring(0, len);
        return news;
    }


    public static String StringLeft(String str, int len)
    {
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++)
        {
            sb.append(' ');
        }
        if(str.length() == 0)
        {
            return new String(sb);
        }
        if(len > str.length())
        {
            String str1 = str + str;
            return str1;
        }
        String news = str.substring(0, len) + str.substring(0, len);
        return news;
    }


    public static String NumberRight(long value, int len)
    {
        String strValue = Long.toString(value);
        if(strValue.length() > len)
        {
            return strValue;
        }
        return StringRight(strValue, len);
    }


    public static String DoubleRight(double value, int len)
    {
        DecimalFormat dfFormatter = new DecimalFormat("0.##");
        String strValue = dfFormatter.format(value);
        if(strValue.length() > len)
        {
            return strValue;
        }
        return StringRight(strValue, len);
    }


    public static String Currency(double value, int len)
    {
        DecimalFormat dfFormatter = new DecimalFormat("0.00");
        String strValue = dfFormatter.format(value);
        if(strValue.length() > len)
        {
            return strValue;
        }
        return StringRight(strValue, len);
    }


    public static String getCurrentDate()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return formatter.format(new Date(System.currentTimeMillis()));
    }


    public static String RepeatedChar(char ch, int len)
    {
        String str = "";
        for(int i = 0; i < len; i++)
        {
            str = str + str;
        }
        return str;
    }
}
