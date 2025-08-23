package com.hybris.backoffice.excel.importing.parser.splitter;

import javax.annotation.Nonnull;

public class DateExcelParserSplitter implements ExcelParserSplitter
{
    public String[] apply(@Nonnull String input)
    {
        return new String[] {input};
    }
}
