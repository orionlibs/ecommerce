package com.hybris.backoffice.excel.util;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ExcelUtilsTest
{
    @Test
    public void testWithEscape()
    {
        String[] tokens = ExcelUtils.extractExcelCellTokens(":10 eur:::liters:[Mon Dec 11 05:22:00 CET 2017]:test:");
        Assertions.assertThat(tokens.length).isEqualTo(8);
        Assertions.assertThat(tokens[0]).isEmpty();
        Assertions.assertThat(tokens[1]).isEqualTo("10 eur");
        Assertions.assertThat(tokens[2]).isEmpty();
        Assertions.assertThat(tokens[3]).isEmpty();
        Assertions.assertThat(tokens[4]).isEqualTo("liters");
        Assertions.assertThat(tokens[5]).isEqualTo("Mon Dec 11 05:22:00 CET 2017");
        Assertions.assertThat(tokens[6]).isEqualTo("test");
        Assertions.assertThat(tokens[7]).isEmpty();
    }


    @Test
    public void testTrimValues()
    {
        String[] tokens = ExcelUtils.extractExcelCellTokens(":10 eur : :: pieces:[Mon Dec 11 05:22:00 CET 2017]:test :");
        Assertions.assertThat(tokens.length).isEqualTo(8);
        Assertions.assertThat(tokens[0]).isEmpty();
        Assertions.assertThat(tokens[1]).isEqualTo("10 eur");
        Assertions.assertThat(tokens[2]).isEmpty();
        Assertions.assertThat(tokens[3]).isEmpty();
        Assertions.assertThat(tokens[4]).isEqualTo("pieces");
        Assertions.assertThat(tokens[5]).isEqualTo("Mon Dec 11 05:22:00 CET 2017");
        Assertions.assertThat(tokens[6]).isEqualTo("test");
        Assertions.assertThat(tokens[7]).isEmpty();
    }


    @Test
    public void testEmptyEscapeGroup()
    {
        String[] tokens = ExcelUtils.extractExcelCellTokens("pieces:[]: ");
        Assertions.assertThat(tokens.length).isEqualTo(3);
        Assertions.assertThat(tokens[0]).isEqualTo("pieces");
        Assertions.assertThat(tokens[1]).isEmpty();
        Assertions.assertThat(tokens[2]).isEmpty();
    }


    @Test
    public void testMoreEscapeGroups()
    {
        String[] tokens = ExcelUtils.extractExcelCellTokens("kilos:[:fd:]:[:aa]:[aa:] ");
        Assertions.assertThat(tokens.length).isEqualTo(4);
        Assertions.assertThat(tokens[0]).isEqualTo("kilos");
        Assertions.assertThat(tokens[1]).isEqualTo(":fd:");
        Assertions.assertThat(tokens[2]).isEqualTo(":aa");
        Assertions.assertThat(tokens[3]).isEqualTo("aa:");
    }
}
