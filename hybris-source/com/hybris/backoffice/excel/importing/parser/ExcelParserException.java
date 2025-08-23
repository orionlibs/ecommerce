package com.hybris.backoffice.excel.importing.parser;

public class ExcelParserException extends RuntimeException
{
    private final String cellValue;
    private final String expectedFormat;


    public ExcelParserException(String cellValue, String expectedFormat)
    {
        this.cellValue = cellValue;
        this.expectedFormat = expectedFormat;
    }


    public String getCellValue()
    {
        return this.cellValue;
    }


    public String getExpectedFormat()
    {
        return this.expectedFormat;
    }
}
