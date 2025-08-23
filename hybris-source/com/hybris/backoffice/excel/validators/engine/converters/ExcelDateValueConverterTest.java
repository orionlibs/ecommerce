package com.hybris.backoffice.excel.validators.engine.converters;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.util.ExcelDateUtils;
import java.util.Collections;
import java.util.Date;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExcelDateValueConverterTest
{
    @Mock
    private ExcelDateUtils excelDateUtils;
    @Mock
    ExcelAttribute excelAttribute;
    @InjectMocks
    private ExcelDateValueConverter converter;


    @Test
    public void shouldBeEligibleToConvertingWhenTypeIsDate()
    {
        BDDMockito.given(this.excelAttribute.getType()).willReturn(Date.class.getName());
        boolean canConvert = this.converter.canConvert(this.excelAttribute, prepareImportParameters("09.04.2018 14:51:21"));
        Assertions.assertThat(canConvert).isTrue();
    }


    @Test
    public void shouldNotBeEligibleToConvertingWhenTypeIsNotDate()
    {
        Mockito.lenient().when(this.excelAttribute.getType()).thenReturn(String.class.getName());
        boolean canConvert = this.converter.canConvert(this.excelAttribute, prepareImportParameters(""));
        Assertions.assertThat(canConvert).isFalse();
    }


    @Test
    public void shouldConvertCorrectDateValue()
    {
        ExcelAttribute excelAttribute = (ExcelAttribute)Mockito.mock(ExcelAttribute.class);
        String cellValue = "09.04.2018 14:51:21";
        ImportParameters importParameters = prepareImportParameters("09.04.2018 14:51:21");
        this.converter.convert(excelAttribute, importParameters);
        ((ExcelDateUtils)Mockito.verify(this.excelDateUtils)).convertToImportedDate("09.04.2018 14:51:21");
    }


    @Test
    public void shouldThrowDateTimeParseExceptionWhenCellValueIsNull()
    {
        ExcelAttribute excelAttribute = (ExcelAttribute)Mockito.mock(ExcelAttribute.class);
        ImportParameters importParameters = prepareImportParameters(null);
        this.converter.convert(excelAttribute, importParameters);
    }


    private ImportParameters prepareImportParameters(String cellValue)
    {
        return new ImportParameters(null, null, cellValue, null, Collections.emptyList());
    }
}
