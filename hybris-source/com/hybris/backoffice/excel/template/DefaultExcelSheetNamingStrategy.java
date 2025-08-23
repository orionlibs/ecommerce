package com.hybris.backoffice.excel.template;

import org.apache.poi.ss.usermodel.Workbook;

public class DefaultExcelSheetNamingStrategy implements ExcelSheetNamingStrategy
{
    protected static final int MAX_LENGTH_SHEET_NAME = 31;
    protected static final String SHEET_NUMBER_SEPARATOR = "_";


    public String generateName(Workbook workbook, String typeCode)
    {
        if(typeCode.length() <= 31 && !hasSheetForName(workbook, typeCode))
        {
            return typeCode;
        }
        return generateSheetName(workbook, typeCode);
    }


    protected String generateSheetName(Workbook workbook, String typeCode)
    {
        int counter = 1;
        while(true)
        {
            String suffix = "_".concat(String.valueOf(counter));
            String truncatedName = typeCode.substring(0, getEndOfOriginalName(typeCode, suffix)).concat(suffix);
            if(!hasSheetForName(workbook, truncatedName))
            {
                return truncatedName;
            }
            counter++;
        }
    }


    protected int getEndOfOriginalName(String typeCode, String suffix)
    {
        int nameWithSuffix = typeCode.length() + suffix.length();
        if(nameWithSuffix <= 31)
        {
            return typeCode.length();
        }
        int overflowSize = nameWithSuffix - 31;
        return nameWithSuffix - overflowSize - suffix.length();
    }


    protected boolean hasSheetForName(Workbook workbook, String typeCode)
    {
        return (workbook.getSheet(typeCode) != null);
    }
}
