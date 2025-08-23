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
public class ExcelStringValueConverterTest
{
    private final ExcelValueConverter converter = (ExcelValueConverter)new ExcelStringValueConverter();
    final ExcelAttribute excelAttribute = (ExcelAttribute)Mockito.mock(ExcelAttribute.class);


    @Test
    public void shouldBeEligibleToConvertingWhenTypeIsString()
    {
        BDDMockito.given(this.excelAttribute.getType()).willReturn(String.class.getName());
        boolean canConvert = this.converter.canConvert(this.excelAttribute, prepareImportParameters(""));
        Assertions.assertThat(canConvert).isTrue();
    }


    @Test
    public void shouldNotBeEligibleToConvertingWhenTypeIsNotString()
    {
        BDDMockito.given(this.excelAttribute.getType()).willReturn(Number.class.getName());
        boolean canConvert = this.converter.canConvert(this.excelAttribute, prepareImportParameters(""));
        Assertions.assertThat(canConvert).isFalse();
    }


    @Test
    public void shouldConvertValueForStringType()
    {
        Mockito.lenient().when(this.excelAttribute.getType()).thenReturn(String.class.getName());
        ImportParameters importParameters = prepareImportParameters("123");
        Object convertedValue = this.converter.convert(this.excelAttribute, importParameters);
        Assertions.assertThat(convertedValue).isInstanceOf(String.class);
        Assertions.assertThat(convertedValue).isEqualTo("123");
    }


    private ImportParameters prepareImportParameters(String cellValue)
    {
        return new ImportParameters(null, null, cellValue, null, Collections.emptyList());
    }
}
