package de.hybris.platform.util;

import java.text.DateFormat;

public class DateFormatUtilImpl implements StandardDateRange.DateFormatUtil
{
    public DateFormat getDateTimeInstance()
    {
        return Utilities.getDateTimeInstance();
    }
}
