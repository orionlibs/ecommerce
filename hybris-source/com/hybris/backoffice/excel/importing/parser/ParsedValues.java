package com.hybris.backoffice.excel.importing.parser;

import java.util.List;
import java.util.Map;

public class ParsedValues
{
    private final List<Map<String, String>> parameters;
    private final String cellValue;


    public ParsedValues(String cellValue, List<Map<String, String>> parameters)
    {
        this.cellValue = cellValue;
        this.parameters = parameters;
    }


    public String getCellValue()
    {
        return this.cellValue;
    }


    public List<Map<String, String>> getParameters()
    {
        return this.parameters;
    }
}
