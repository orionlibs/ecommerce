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
public class ExcelNumberValueConverterTest
{
    private final ExcelValueConverter converter = (ExcelValueConverter)new ExcelNumberValueConverter();
    final ExcelAttribute excelAttribute = (ExcelAttribute)Mockito.mock(ExcelAttribute.class);


    @Test
    public void shouldBeEligibleToConvertingWhenTypeIsByte()
    {
        BDDMockito.given(this.excelAttribute.getType()).willReturn(Byte.class.getName());
        boolean canConvert = this.converter.canConvert(this.excelAttribute, prepareImportParameters("1"));
        Assertions.assertThat(canConvert).isTrue();
    }


    @Test
    public void shouldBeEligibleToConvertingWhenTypeIsShort()
    {
        BDDMockito.given(this.excelAttribute.getType()).willReturn(Short.class.getName());
        boolean canConvert = this.converter.canConvert(this.excelAttribute, prepareImportParameters("1"));
        Assertions.assertThat(canConvert).isTrue();
    }


    @Test
    public void shouldBeEligibleToConvertingWhenTypeIsInteger()
    {
        BDDMockito.given(this.excelAttribute.getType()).willReturn(Integer.class.getName());
        boolean canConvert = this.converter.canConvert(this.excelAttribute, prepareImportParameters("1"));
        Assertions.assertThat(canConvert).isTrue();
    }


    @Test
    public void shouldBeEligibleToConvertingWhenTypeIsLong()
    {
        BDDMockito.given(this.excelAttribute.getType()).willReturn(Long.class.getName());
        boolean canConvert = this.converter.canConvert(this.excelAttribute, prepareImportParameters("1"));
        Assertions.assertThat(canConvert).isTrue();
    }


    @Test
    public void shouldBeEligibleToConvertingWhenTypeIsFloat()
    {
        BDDMockito.given(this.excelAttribute.getType()).willReturn(Float.class.getName());
        boolean canConvert = this.converter.canConvert(this.excelAttribute, prepareImportParameters("1"));
        Assertions.assertThat(canConvert).isTrue();
    }


    @Test
    public void shouldBeEligibleToConvertingWhenTypeIsDouble()
    {
        BDDMockito.given(this.excelAttribute.getType()).willReturn(Double.class.getName());
        boolean canConvert = this.converter.canConvert(this.excelAttribute, prepareImportParameters("1"));
        Assertions.assertThat(canConvert).isTrue();
    }


    @Test
    public void shouldNotBeEligibleToConvertingWhenTypeIsNotANumber()
    {
        Mockito.lenient().when(this.excelAttribute.getType()).thenReturn(String.class.getName());
        boolean canConvert = this.converter.canConvert(this.excelAttribute, prepareImportParameters(""));
        Assertions.assertThat(canConvert).isFalse();
    }


    @Test
    public void shouldConvertValueForByteType()
    {
        BDDMockito.given(this.excelAttribute.getType()).willReturn(Byte.class.getName());
        ImportParameters importParameters = prepareImportParameters("1");
        Object convertedValue = this.converter.convert(this.excelAttribute, importParameters);
        Assertions.assertThat(convertedValue).isInstanceOf(Byte.class);
        Assertions.assertThat(convertedValue).isEqualTo(Byte.valueOf((byte)1));
    }


    @Test
    public void shouldConvertValueForShortType()
    {
        BDDMockito.given(this.excelAttribute.getType()).willReturn(Short.class.getName());
        ImportParameters importParameters = prepareImportParameters("15");
        Object convertedValue = this.converter.convert(this.excelAttribute, importParameters);
        Assertions.assertThat(convertedValue).isInstanceOf(Short.class);
        Assertions.assertThat(convertedValue).isEqualTo(Short.valueOf((short)15));
    }


    @Test
    public void shouldConvertValueForIntegerType()
    {
        BDDMockito.given(this.excelAttribute.getType()).willReturn(Integer.class.getName());
        ImportParameters importParameters = prepareImportParameters("123456");
        Object convertedValue = this.converter.convert(this.excelAttribute, importParameters);
        Assertions.assertThat(convertedValue).isInstanceOf(Integer.class);
        Assertions.assertThat(convertedValue).isEqualTo(Integer.valueOf(123456));
    }


    @Test
    public void shouldConvertValueForLongType()
    {
        BDDMockito.given(this.excelAttribute.getType()).willReturn(Long.class.getName());
        ImportParameters importParameters = prepareImportParameters("1234567898");
        Object convertedValue = this.converter.convert(this.excelAttribute, importParameters);
        Assertions.assertThat(convertedValue).isInstanceOf(Long.class);
        Assertions.assertThat(convertedValue).isEqualTo(Long.valueOf(1234567898L));
    }


    @Test
    public void shouldConvertValueForFloatType()
    {
        BDDMockito.given(this.excelAttribute.getType()).willReturn(Float.class.getName());
        ImportParameters importParameters = prepareImportParameters("12345.67898");
        Object convertedValue = this.converter.convert(this.excelAttribute, importParameters);
        Assertions.assertThat(convertedValue).isInstanceOf(Float.class);
        Assertions.assertThat(convertedValue).isEqualTo(Float.valueOf(12345.679F));
    }


    @Test
    public void shouldConvertValueForDoubleType()
    {
        BDDMockito.given(this.excelAttribute.getType()).willReturn(Double.class.getName());
        ImportParameters importParameters = prepareImportParameters("12345.678987654321");
        Object convertedValue = this.converter.convert(this.excelAttribute, importParameters);
        Assertions.assertThat(convertedValue).isInstanceOf(Double.class);
        Assertions.assertThat(convertedValue).isEqualTo(Double.valueOf(12345.67898765432D));
    }


    private ImportParameters prepareImportParameters(String cellValue)
    {
        return new ImportParameters(null, null, cellValue, null, Collections.emptyList());
    }
}
