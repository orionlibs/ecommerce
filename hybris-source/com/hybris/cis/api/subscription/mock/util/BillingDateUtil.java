package com.hybris.cis.api.subscription.mock.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class BillingDateUtil
{
    private BillingDateUtil() throws IllegalStateException
    {
        throw new IllegalStateException("Utility class");
    }


    public static Date getBillingDateForSpecificBillingMock(int numberOfMonths)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(2, numberOfMonths);
        cal.set(5, 1);
        cal.set(14, 0);
        cal.set(13, 0);
        cal.set(12, 0);
        cal.set(11, 0);
        return cal.getTime();
    }


    public static String getBillingPeriodForSpecificBillingMock(int numberOfMonths)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(2, numberOfMonths - 1);
        cal.set(5, 1);
        String billingPeriod = sdf.format(cal.getTime()) + " – ";
        cal.set(5, cal.getActualMaximum(5));
        billingPeriod = billingPeriod + billingPeriod;
        return billingPeriod;
    }
}
