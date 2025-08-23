package com.hybris.backoffice.excel.util;

import java.util.Date;
import org.apache.commons.lang3.tuple.Pair;

public interface ExcelDateUtils
{
    Pair<String, String> extractDateRange(String paramString);


    String getDateRangePattern();


    String getDateRangeParamKey();


    String exportDateRange(Date paramDate1, Date paramDate2);


    String getDateTimeFormat();


    String exportDate(Date paramDate);


    String importDate(String paramString);


    Date convertToImportedDate(String paramString);


    String getExportTimeZone();
}
