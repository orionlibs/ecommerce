package com.hybris.backoffice.excel.importing.parser.matcher;

import com.hybris.backoffice.excel.util.ExcelDateUtils;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DateExcelParserMatcher implements ExcelParserMatcher
{
    private ExcelDateUtils excelDateUtils;


    public boolean test(@Nonnull String input)
    {
        return StringUtils.equals(input, this.excelDateUtils.getDateTimeFormat());
    }


    @Required
    public void setExcelDateUtils(ExcelDateUtils excelDateUtils)
    {
        this.excelDateUtils = excelDateUtils;
    }
}
