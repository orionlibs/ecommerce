package com.hybris.backoffice.excel.validators.engine.converters;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import java.util.Collections;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExcelBooleanValueConverterTest
{
    private final ExcelValueConverter converter = (ExcelValueConverter)new ExcelBooleanValueConverter();


    @Test
    public void shouldBeEligibleToConvertingWhenTypeIsBoolean()
    {
        ExcelAttribute excelAttribute = (ExcelAttribute)Mockito.mock(ExcelAttribute.class);
        BDDMockito.given(excelAttribute.getType()).willReturn(Boolean.class.getName());
        boolean canConvert = this.converter.canConvert(excelAttribute, prepareImportParameters("true"));
        Assertions.assertThat(canConvert).isTrue();
    }


    @Test
    public void shouldNotBeEligibleToConvertingWhenTypeIsNotBoolean()
    {
        ExcelAttribute excelAttribute = (ExcelAttribute)Mockito.mock(ExcelAttribute.class);
        Mockito.lenient().when(excelAttribute.getType()).thenReturn("");
        boolean canConvert = this.converter.canConvert(excelAttribute, prepareImportParameters(""));
        Assertions.assertThat(canConvert).isFalse();
    }


    @Test
    public void shouldConvertCorrectTrueValueToTrue()
    {
        ExcelAttribute excelAttribute = (ExcelAttribute)Mockito.mock(ExcelAttribute.class);
        ImportParameters importParameters = prepareImportParameters("true");
        Object convertedValue = this.converter.convert(excelAttribute, importParameters);
        Assertions.assertThat(convertedValue).isInstanceOf(Boolean.class);
        Assertions.assertThat((Boolean)convertedValue).isTrue();
    }


    @Test
    public void shouldConvertCorrectFalseValueToFalse()
    {
        ExcelAttribute excelAttribute = (ExcelAttribute)Mockito.mock(ExcelAttribute.class);
        ImportParameters importParameters = prepareImportParameters("false");
        Object convertedValue = this.converter.convert(excelAttribute, importParameters);
        Assertions.assertThat(convertedValue).isInstanceOf(Boolean.class);
        Assertions.assertThat((Boolean)convertedValue).isFalse();
    }


    @Test
    public void shouldConvertIncorrectValueToFalse()
    {
        ExcelAttribute excelAttribute = (ExcelAttribute)Mockito.mock(ExcelAttribute.class);
        ImportParameters importParameters = prepareImportParameters("abc");
        Object convertedValue = this.converter.convert(excelAttribute, importParameters);
        Assertions.assertThat(convertedValue).isInstanceOf(Boolean.class);
        Assertions.assertThat((Boolean)convertedValue).isFalse();
    }


    private ImportParameters prepareImportParameters(String cellValue)
    {
        return new ImportParameters(null, null, cellValue, null, Collections.emptyList());
    }
}
