package com.hybris.backoffice.excel.template.populator;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

public class ClassificationAttributeLocalizedPopulatorTest
{
    ExcelClassificationCellPopulator populator = (ExcelClassificationCellPopulator)new ClassificationAttributeLocalizedPopulator();


    @Test
    public void shouldGetFalsyLocalizedFromAssignment()
    {
        ExcelClassificationAttribute attribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        BDDMockito.given(Boolean.valueOf(attribute.isLocalized())).willReturn(Boolean.valueOf(false));
        String result = this.populator.apply(DefaultExcelAttributeContext.ofExcelAttribute((ExcelAttribute)attribute));
        Assertions.assertThat(result).isEqualTo("false");
    }


    @Test
    public void shouldGetTruthyLocalizedFromAssignment()
    {
        ExcelClassificationAttribute attribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        BDDMockito.given(Boolean.valueOf(attribute.isLocalized())).willReturn(Boolean.valueOf(true));
        String result = this.populator.apply(DefaultExcelAttributeContext.ofExcelAttribute((ExcelAttribute)attribute));
        Assertions.assertThat(result).isEqualTo("true");
    }
}
