package com.hybris.backoffice.excel.template.populator;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

public class ClassificationAttributeLocLangPopulatorTest
{
    ClassificationAttributeLocLangPopulator populator = new ClassificationAttributeLocLangPopulator();


    @Test
    public void shouldGetEnglishLocLang()
    {
        ExcelClassificationAttribute attribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        BDDMockito.given(Boolean.valueOf(attribute.isLocalized())).willReturn(Boolean.valueOf(true));
        BDDMockito.given(attribute.getIsoCode()).willReturn("en");
        String result = this.populator.apply(DefaultExcelAttributeContext.ofExcelAttribute((ExcelAttribute)attribute));
        Assertions.assertThat(result).isEqualTo("en");
    }


    @Test
    public void shouldGetFrenchLocLang()
    {
        ExcelClassificationAttribute attribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        BDDMockito.given(Boolean.valueOf(attribute.isLocalized())).willReturn(Boolean.valueOf(true));
        BDDMockito.given(attribute.getIsoCode()).willReturn("fr");
        String result = this.populator.apply(DefaultExcelAttributeContext.ofExcelAttribute((ExcelAttribute)attribute));
        Assertions.assertThat(result).isEqualTo("fr");
    }
}
