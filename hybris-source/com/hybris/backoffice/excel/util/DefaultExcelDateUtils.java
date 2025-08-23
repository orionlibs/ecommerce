package com.hybris.backoffice.excel.util;

import de.hybris.platform.servicelayer.i18n.I18NService;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Required;

public class DefaultExcelDateUtils implements ExcelDateUtils
{
    private static final Pattern PATTERN_DATE_RANGE = Pattern.compile("(.*)\\s+to\\s+(.*)");
    private static final String FORMAT_DATE_RANGE_PARAM_KEY = "%s to %s";
    private static final String FORMAT_DATE_RANGE_EXPORT_VALUE = "[%s to %s]";
    private String exportTimeZone = "UTC";
    private String dateTimeFormat = "dd.MM.yyyy HH:mm:ss";
    private I18NService i18NService;


    public Pair<String, String> extractDateRange(String cellValue)
    {
        Matcher matcher = PATTERN_DATE_RANGE.matcher(cellValue);
        if(matcher.matches() && matcher.groupCount() == 2)
        {
            return (Pair<String, String>)new ImmutablePair(matcher.group(1).trim(), matcher.group(2).trim());
        }
        return null;
    }


    public String getDateRangePattern()
    {
        return String.format("[%s to %s]", new Object[] {getDateTimeFormat(), getDateTimeFormat()});
    }


    public String getDateRangeParamKey()
    {
        return String.format("%s to %s", new Object[] {getDateTimeFormat(), getDateTimeFormat()});
    }


    public String exportDateRange(Date start, Date end)
    {
        return String.format("[%s to %s]", new Object[] {exportDate(start), exportDate(end)});
    }


    public String getDateTimeFormat()
    {
        return this.dateTimeFormat;
    }


    public String exportDate(Date date)
    {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(this.dateTimeFormat);
        ZonedDateTime exportDateTimeInUTC = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.of(this.exportTimeZone));
        return format.format(exportDateTimeInUTC);
    }


    public String importDate(String date)
    {
        if(StringUtils.isNotBlank(date))
        {
            DateTimeFormatter format = DateTimeFormatter.ofPattern(this.dateTimeFormat);
            LocalDateTime importedDateTime = LocalDateTime.parse(StringUtils.trim(date), format);
            ZonedDateTime dateFromExcelInUtc = adjustToSessionTimeZone(importedDateTime);
            return format.format(dateFromExcelInUtc);
        }
        return "";
    }


    public Date convertToImportedDate(String date)
    {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(this.dateTimeFormat);
        LocalDateTime importedDateTime = LocalDateTime.parse(StringUtils.trim(date), format);
        ZonedDateTime dateFromExcelInUtc = adjustToSessionTimeZone(importedDateTime);
        return Date.from(dateFromExcelInUtc.toInstant());
    }


    private ZonedDateTime adjustToSessionTimeZone(LocalDateTime importedDateTime)
    {
        return ZonedDateTime.ofInstant(importedDateTime.atZone(ZoneId.of(this.exportTimeZone)).toInstant(), getSessionTimeZone());
    }


    private ZoneId getSessionTimeZone()
    {
        TimeZone currentTimeZone = this.i18NService.getCurrentTimeZone();
        return (currentTimeZone != null) ? currentTimeZone.toZoneId() : ZoneId.systemDefault();
    }


    public String getExportTimeZone()
    {
        return this.exportTimeZone;
    }


    public void setExportTimeZone(String exportTimeZone)
    {
        this.exportTimeZone = exportTimeZone;
    }


    public void setDateTimeFormat(String dateTimeFormat)
    {
        this.dateTimeFormat = dateTimeFormat;
    }


    @Required
    public void setI18NService(I18NService i18NService)
    {
        this.i18NService = i18NService;
    }
}
