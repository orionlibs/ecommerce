package com.hybris.backoffice.excel.importing.parser.splitter;

import javax.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;

public class UnitExcelParserSplitter implements ExcelParserSplitter
{
    public String[] apply(@Nonnull String s)
    {
        if(StringUtils.isBlank(s))
        {
            return new String[0];
        }
        int unitSeparatorIndex = s.lastIndexOf(":");
        String left = s.substring(0, unitSeparatorIndex);
        String right = s.substring(unitSeparatorIndex + 1);
        return new String[] {left, right};
    }
}
