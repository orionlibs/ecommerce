package com.hybris.backoffice.excel.validators.engine.converters;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.importing.parser.DefaultImportParameterParser;
import com.hybris.backoffice.excel.importing.parser.ParsedValues;
import com.hybris.backoffice.excel.importing.parser.matcher.DefaultExcelParserMatcher;
import com.hybris.backoffice.excel.importing.parser.matcher.ExcelParserMatcher;
import com.hybris.backoffice.excel.importing.parser.splitter.DefaultExcelParserSplitter;
import com.hybris.backoffice.excel.importing.parser.splitter.ExcelParserSplitter;
import java.util.Collection;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExcelMultiValueConverterTest
{
    private final ExcelValueConverter converter = (ExcelValueConverter)new ExcelMultiValueConverter();
    final ExcelAttribute excelAttribute = (ExcelAttribute)Mockito.mock(ExcelAttribute.class);


    @Test
    public void shouldBeEligibleToConvertingWhenTypeIsMultiValue()
    {
        BDDMockito.given(Boolean.valueOf(this.excelAttribute.isMultiValue())).willReturn(Boolean.valueOf(true));
        boolean canConvert = this.converter.canConvert(this.excelAttribute, prepareImportParameters(""));
        Assertions.assertThat(canConvert).isTrue();
    }


    @Test
    public void shouldNotBeEligibleToConvertingWhenTypeIsSingleValue()
    {
        BDDMockito.given(Boolean.valueOf(this.excelAttribute.isMultiValue())).willReturn(Boolean.valueOf(false));
        boolean canConvert = this.converter.canConvert(this.excelAttribute, prepareImportParameters(""));
        Assertions.assertThat(canConvert).isFalse();
    }


    @Test
    public void shouldConvertCorrectMultiValue()
    {
        ExcelAttribute excelAttribute = (ExcelAttribute)Mockito.mock(ExcelAttribute.class);
        ImportParameters importParameters = prepareImportParameters("firstValue,secondValue,thirdValue");
        Object convertedValue = this.converter.convert(excelAttribute, importParameters);
        Assertions.assertThat(convertedValue).isInstanceOf(Collection.class);
        Assertions.assertThat((Collection)convertedValue).hasSize(3);
    }


    private ImportParameters prepareImportParameters(String cellValue)
    {
        DefaultImportParameterParser parameterParser = new DefaultImportParameterParser();
        parameterParser.setSplitter((ExcelParserSplitter)new DefaultExcelParserSplitter());
        parameterParser.setMatcher((ExcelParserMatcher)new DefaultExcelParserMatcher());
        ParsedValues parsedValues = parameterParser.parseValue("", "", cellValue);
        return new ImportParameters("", "", parsedValues.getCellValue(), null, parsedValues
                        .getParameters());
    }
}
