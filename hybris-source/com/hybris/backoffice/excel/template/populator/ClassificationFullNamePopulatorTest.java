package com.hybris.backoffice.excel.template.populator;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.template.AttributeNameFormatter;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClassificationFullNamePopulatorTest
{
    @Mock
    AttributeNameFormatter<ExcelClassificationAttribute> mockedAttributeNameFormatter;
    @InjectMocks
    ClassificationFullNamePopulator populator;


    @Test
    public void shouldGetClassificationFullName()
    {
        ExcelClassificationAttribute attribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        BDDMockito.given(this.mockedAttributeNameFormatter.format((ExcelAttributeContext)Matchers.any())).willReturn("fullName");
        String result = this.populator.apply(DefaultExcelAttributeContext.ofExcelAttribute((ExcelAttribute)attribute));
        Assertions.assertThat(result).isEqualTo("fullName");
    }
}
