package com.hybris.backoffice.solrsearch.resolvers;

import java.text.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class CaseFormatTest
{
    private static final String TEST_STRING = "Test";


    @Test
    public void testLowerCase()
    {
        Assertions.assertThat((new CaseFormat(CaseFormat.Case.TO_LOWER)).format("Test")).isEqualTo("Test".toLowerCase());
    }


    @Test
    public void testUpperCase()
    {
        Assertions.assertThat((new CaseFormat(CaseFormat.Case.TO_UPPER)).format("Test")).isEqualTo("Test".toUpperCase());
    }


    @Test
    public void testSwapCase()
    {
        Assertions.assertThat((new CaseFormat(CaseFormat.Case.SWAP)).format("Test"))
                        .isEqualTo(StringUtils.swapCase("Test"));
    }


    @Test
    public void testNull()
    {
        Assertions.assertThat((new CaseFormat(CaseFormat.Case.SWAP)).format(null)).isEmpty();
    }


    @Test
    public void testParse() throws ParseException
    {
        Assertions.assertThat((new CaseFormat(CaseFormat.Case.SWAP)).parseObject("Test")).isEqualTo("Test");
    }


    @Test
    public void testParseNull() throws ParseException
    {
        Assertions.assertThat((new CaseFormat(CaseFormat.Case.SWAP)).parseObject(null)).isEqualTo(null);
    }
}
