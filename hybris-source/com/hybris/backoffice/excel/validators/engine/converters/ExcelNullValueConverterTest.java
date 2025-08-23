package com.hybris.backoffice.excel.validators.engine.converters;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import java.util.Collections;
import java.util.Date;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExcelNullValueConverterTest
{
    private final ExcelValueConverter converter = (ExcelValueConverter)new ExcelNullValueConverter();
    final ExcelAttribute excelAttribute = (ExcelAttribute)Mockito.mock(ExcelAttribute.class);


    @Test
    public void shouldBeEligibleToConvertingWhenCellIsEmpty()
    {
        boolean canConvert = this.converter.canConvert(this.excelAttribute, prepareImportParameters(""));
        Assertions.assertThat(canConvert).isTrue();
    }


    @Test
    public void shouldNotBeEligibleToConvertingWhenCellIsNotEmpty()
    {
        boolean canConvert = this.converter.canConvert(this.excelAttribute, prepareImportParameters("Abc"));
        Assertions.assertThat(canConvert).isFalse();
    }


    @Test
    public void shouldConvertValueWhenCellIsEmptyAndTypeIsNotString()
    {
        BDDMockito.given(this.excelAttribute.getType()).willReturn(Date.class.getName());
        ImportParameters importParameters = prepareImportParameters("");
        Object convertedValue = this.converter.convert(this.excelAttribute, importParameters);
        Assertions.assertThat(convertedValue).isNull();
    }


    @Test
    public void shouldNotConvertValueWhenCellIsEmptyAndTypeIsString()
    {
        BDDMockito.given(this.excelAttribute.getType()).willReturn(String.class.getName());
        ImportParameters importParameters = prepareImportParameters("");
        Object convertedValue = this.converter.convert(this.excelAttribute, importParameters);
        Assertions.assertThat(convertedValue).isInstanceOf(String.class);
        Assertions.assertThat(convertedValue).isEqualTo("");
    }


    private ImportParameters prepareImportParameters(String cellValue)
    {
        return new ImportParameters(null, null, cellValue, null, Collections.emptyList());
    }
}
