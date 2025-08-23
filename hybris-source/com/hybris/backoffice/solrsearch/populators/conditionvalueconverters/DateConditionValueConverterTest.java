package com.hybris.backoffice.solrsearch.populators.conditionvalueconverters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class DateConditionValueConverterTest
{
    private final DateConditionValueConverter converter = new DateConditionValueConverter();


    @Test
    public void shouldConvertDateToSolrDateFormat()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, 2, 2, 10, 11, 12);
        for(String timeZoneID : TimeZone.getAvailableIDs())
        {
            calendar.setTimeZone(TimeZone.getTimeZone(timeZoneID));
            Date dateToTest = calendar.getTime();
            String converted = this.converter.apply(dateToTest);
            Assertions.assertThat(converted).isEqualTo(getExpectedDate(dateToTest, timeZoneID));
        }
    }


    private String getExpectedDate(Date dateToTest, String timeZoneID)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(timeZoneID));
        String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(dateToTest);
    }
}
