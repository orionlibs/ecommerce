package com.hybris.backoffice.excel.importing.parser.matcher;

import com.hybris.backoffice.excel.util.ExcelDateUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DateExcelParserMatcherTest
{
    @Mock
    private ExcelDateUtils excelDateUtils;
    @InjectMocks
    private DateExcelParserMatcher matcher = new DateExcelParserMatcher();


    @Test
    public void shouldInputMatchThePattern()
    {
        String pattern = "dd.MM.yyyy HH:mm:ss";
        BDDMockito.given(this.excelDateUtils.getDateTimeFormat()).willReturn("dd.MM.yyyy HH:mm:ss");
        Assert.assertTrue(this.matcher.test("dd.MM.yyyy HH:mm:ss"));
    }
}
