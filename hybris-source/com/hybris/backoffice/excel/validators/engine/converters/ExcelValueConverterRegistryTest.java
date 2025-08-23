package com.hybris.backoffice.excel.validators.engine.converters;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

public class ExcelValueConverterRegistryTest
{
    @Test
    public void shouldFindAppropriateConverted()
    {
        ExcelNullValueConverter excelNullValueConverter = (ExcelNullValueConverter)Mockito.mock(ExcelNullValueConverter.class);
        ExcelNumberValueConverter excelNumberValueConverter = (ExcelNumberValueConverter)Mockito.mock(ExcelNumberValueConverter.class);
        ExcelValueConverterRegistry registry = new ExcelValueConverterRegistry();
        registry.setConverters(Arrays.asList(new ExcelValueConverter[] {(ExcelValueConverter)excelNullValueConverter, (ExcelValueConverter)excelNumberValueConverter}));
        ExcelAttribute excelAttribute = (ExcelAttribute)Mockito.mock(ExcelAttribute.class);
        ImportParameters importParameters = new ImportParameters("Product", "en", "3.14", null, new ArrayList());
        BDDMockito.given(Boolean.valueOf(excelNullValueConverter.canConvert(excelAttribute, importParameters))).willReturn(Boolean.valueOf(true));
        BDDMockito.given(Boolean.valueOf(excelNumberValueConverter.canConvert(excelAttribute, importParameters))).willReturn(Boolean.valueOf(true));
        Optional<ExcelValueConverter> foundConverted = registry.getConverter(excelAttribute, importParameters, new Class[] {excelNullValueConverter
                        .getClass()});
        Assertions.assertThat(foundConverted).isPresent();
        Assertions.assertThat(foundConverted.get()).isEqualTo(excelNumberValueConverter);
    }
}
