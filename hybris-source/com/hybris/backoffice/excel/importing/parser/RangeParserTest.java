package com.hybris.backoffice.excel.importing.parser;

import com.hybris.backoffice.excel.importing.parser.splitter.DefaultExcelParserSplitter;
import com.hybris.backoffice.excel.importing.parser.splitter.ExcelParserSplitter;
import java.util.LinkedHashMap;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RangeParserTest
{
    @Mock
    private ParserRegistry registry;
    private RangeParser rangeParser = new RangeParser();
    private static final String rangeFormat = "RANGE[%s;%s]";


    @Before
    public void setUp()
    {
        DefaultImportParameterParser parameterParser = new DefaultImportParameterParser();
        parameterParser.setSplitter((ExcelParserSplitter)new DefaultExcelParserSplitter());
        BDDMockito.given(this.registry.getParser((String)Matchers.any())).willReturn(parameterParser);
        this.rangeParser.setParserRegistry(this.registry);
    }


    @Test
    public void shouldRetrieveDefaultValues()
    {
        String referenceFormatLeft = "from";
        String referenceFormatRight = "to";
        String referenceFormat = String.format("RANGE[%s;%s]", new Object[] {"from", "to"});
        String defaultLeftValue = "someValue";
        String defaultValues = String.format("RANGE[%s;%s]", new Object[] {"someValue", ""});
        DefaultValues output = this.rangeParser.parseDefaultValues(referenceFormat, defaultValues);
        Assertions.assertThat(output.getDefaultValues()).isEqualTo("someValue;");
        Assertions.assertThat(output.getReferenceFormat())
                        .isEqualTo(RangeParserUtils.prependFromPrefix("from") + ";" + RangeParserUtils.prependFromPrefix("from"));
        Assertions.assertThat(output.getKeys()).containsExactly((Object[])new String[] {RangeParserUtils.prependFromPrefix("from"), RangeParserUtils.prependToPrefix("to")});
        Assertions.assertThat(output.getValues()).containsExactly((Object[])new String[] {"someValue", null});
    }


    @Test
    public void shouldParseValue()
    {
        String referenceFormatLeft = "someValue1";
        String referenceFormatRight = "someValue2";
        String prependedReferenceFormatLeft = RangeParserUtils.prependFromPrefix("someValue1");
        String prependedReferenceFormatRight = RangeParserUtils.prependToPrefix("someValue2");
        String referenceFormat = prependedReferenceFormatLeft + ";" + prependedReferenceFormatLeft;
        String defaultValue = "defaultValue";
        String defaultValues = "defaultValue;";
        Map<String, String> params = new LinkedHashMap<>();
        params.put(prependedReferenceFormatLeft, "defaultValue");
        params.put(prependedReferenceFormatRight, "");
        DefaultValues defValues = new DefaultValues("defaultValue;", referenceFormat, params);
        String cellLeft = "";
        String cellRight = "value";
        String cellValue = String.format("RANGE[%s;%s]", new Object[] {"", "value"});
        ParsedValues parsedValues = this.rangeParser.parseValue(cellValue, defValues);
        Assertions.assertThat(parsedValues.getParameters().size()).isEqualTo(2);
        Assertions.assertThat(((Map)parsedValues.getParameters().get(0)).values()).containsExactly((Object[])new String[] {"defaultValue", "defaultValue"});
        Assertions.assertThat(((Map)parsedValues.getParameters().get(1)).values()).containsExactly((Object[])new String[] {"value", "value"});
        Assertions.assertThat(((Map)parsedValues.getParameters().get(0)).keySet()).filteredOn(key -> key.contains("from$")).hasSize(2);
        Assertions.assertThat(((Map)parsedValues.getParameters().get(1)).keySet()).filteredOn(key -> key.contains("to$")).hasSize(2);
    }


    @Test
    public void shouldDefaultValueBeUsedInCaseOfEmptyValueCell()
    {
        String defaultValue = "RANGE[5:kg;6:kg]";
        String cellValue = "";
        String referenceFormat = "RANGE[value:unit[kg];value:unit[kg]]";
        ParsedValues parsedValues = this.rangeParser.parseValue("RANGE[value:unit[kg];value:unit[kg]]", "RANGE[5:kg;6:kg]", "");
        Assertions.assertThat(parsedValues.getCellValue()).isEqualTo("RANGE[5:kg;6:kg]");
    }
}
