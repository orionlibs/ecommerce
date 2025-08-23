package com.hybris.backoffice.excel.util;

import de.hybris.platform.servicelayer.i18n.I18NService;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultExcelDateUtilsTest
{
    @Mock
    private I18NService i18NService;
    @InjectMocks
    private DefaultExcelDateUtils extractDateUtils;


    @Test
    public void testTestExtractRange()
    {
        setTimeZone("UTC");
        Pair<String, String> range = this.extractDateUtils.extractDateRange("123 to 200");
        Assertions.assertThat((String)range.getLeft()).isEqualTo("123");
        Assertions.assertThat((String)range.getRight()).isEqualTo("200");
    }


    @Test
    public void testExtractRangeWhitespaces()
    {
        setTimeZone("UTC");
        Pair<String, String> range = this.extractDateUtils.extractDateRange("123    to\t 200");
        Assertions.assertThat((String)range.getLeft()).isEqualTo("123");
        Assertions.assertThat((String)range.getRight()).isEqualTo("200");
    }


    @Test
    public void testExportDateWhenServerIsInUTC()
    {
        setTimeZone("UTC");
        ZonedDateTime zonedDateTime = ZonedDateTime.of(2017, 10, 23, 10, 46, 0, 0, ZoneId.of("UTC"));
        Date dateFrom = Date.from(zonedDateTime.toInstant());
        Assertions.assertThat(this.extractDateUtils.exportDate(dateFrom)).isEqualTo("23.10.2017 10:46:00");
    }


    @Test
    public void testExportDateWhenServerInCET()
    {
        setTimeZone("CET");
        ZonedDateTime zonedDateTime = ZonedDateTime.of(2017, 10, 23, 10, 46, 0, 0, ZoneId.of("CET"));
        Date dateFrom = Date.from(zonedDateTime.toInstant());
        Assertions.assertThat(this.extractDateUtils.exportDate(dateFrom)).isEqualTo("23.10.2017 08:46:00");
    }


    @Test
    public void testImportDateWhenServerIsInUTC()
    {
        setTimeZone("UTC");
        Assertions.assertThat(this.extractDateUtils.importDate("23.10.2017 10:46:00")).isEqualTo("23.10.2017 10:46:00");
    }


    @Test
    public void testImportDateWhenServerInCET()
    {
        setTimeZone("CET");
        Assertions.assertThat(this.extractDateUtils.importDate("23.10.2017 10:46:00")).isEqualTo("23.10.2017 12:46:00");
    }


    private void setTimeZone(String timeZone)
    {
        BDDMockito.given(this.i18NService.getCurrentTimeZone()).willReturn(TimeZone.getTimeZone(timeZone));
    }
}
