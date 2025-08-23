package com.hybris.backoffice.excel.importing.parser.splitter;

import com.hybris.backoffice.excel.util.ExcelUtils;
import javax.annotation.Nonnull;

public class DefaultExcelParserSplitter implements ExcelParserSplitter
{
    public String[] apply(@Nonnull String input)
    {
        return ExcelUtils.extractExcelCellTokens(input);
    }
}
