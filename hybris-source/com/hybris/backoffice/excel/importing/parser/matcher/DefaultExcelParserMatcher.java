package com.hybris.backoffice.excel.importing.parser.matcher;

import javax.annotation.Nonnull;

public class DefaultExcelParserMatcher implements ExcelParserMatcher
{
    public boolean test(@Nonnull String ignored)
    {
        return true;
    }
}
