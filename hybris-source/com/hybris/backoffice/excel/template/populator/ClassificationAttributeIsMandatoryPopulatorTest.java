package com.hybris.backoffice.excel.template.populator;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

public class ClassificationAttributeIsMandatoryPopulatorTest
{
    ExcelClassificationCellPopulator populator = (ExcelClassificationCellPopulator)new ClassificationAttributeIsMandatoryPopulator();


    @Test
    public void shouldGetFalsyMandatory()
    {
        ExcelClassificationAttribute attribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        BDDMockito.given(Boolean.valueOf(attribute.isMandatory())).willReturn(Boolean.FALSE);
        String result = this.populator.apply(DefaultExcelAttributeContext.ofExcelAttribute((ExcelAttribute)attribute));
        Assertions.assertThat(result).isEqualTo("false");
    }


    @Test
    public void shouldGetTruthyMandatory()
    {
        ExcelClassificationAttribute attribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        BDDMockito.given(Boolean.valueOf(attribute.isMandatory())).willReturn(Boolean.TRUE);
        String result = this.populator.apply(DefaultExcelAttributeContext.ofExcelAttribute((ExcelAttribute)attribute));
        Assertions.assertThat(result).isEqualTo("true");
    }
}
