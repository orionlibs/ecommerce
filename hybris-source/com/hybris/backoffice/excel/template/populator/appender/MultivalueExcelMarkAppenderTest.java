package com.hybris.backoffice.excel.template.populator.appender;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelAttributeDescriptorAttribute;
import com.hybris.backoffice.excel.template.ExcelTemplateConstants;
import org.assertj.core.api.AbstractCharSequenceAssert;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MultivalueExcelMarkAppenderTest
{
    private MultivalueExcelMarkAppender appender = new MultivalueExcelMarkAppender();


    @Test
    public void shouldMultivalueMarkBeAppendedWhenAttributeIsMultivalue()
    {
        ExcelAttributeDescriptorAttribute excelAttributeDescriptorAttribute = (ExcelAttributeDescriptorAttribute)Mockito.mock(ExcelAttributeDescriptorAttribute.class);
        BDDMockito.given(Boolean.valueOf(excelAttributeDescriptorAttribute.isMultiValue())).willReturn(Boolean.valueOf(true));
        String input = "Article Number";
        String output = this.appender.apply("Article Number", (ExcelAttribute)excelAttributeDescriptorAttribute);
        Assertions.assertThat(output).isEqualTo("Article Number" + ExcelTemplateConstants.SpecialMark.MULTIVALUE.getMark());
    }


    @Test
    public void shouldMultivalueMarkNotBeAppendedWhenAttributeIsNotMultivalue()
    {
        ExcelAttributeDescriptorAttribute excelAttributeDescriptorAttribute = (ExcelAttributeDescriptorAttribute)Mockito.mock(ExcelAttributeDescriptorAttribute.class);
        BDDMockito.given(Boolean.valueOf(excelAttributeDescriptorAttribute.isMultiValue())).willReturn(Boolean.valueOf(false));
        String input = "Article Number";
        String output = this.appender.apply("Article Number", (ExcelAttribute)excelAttributeDescriptorAttribute);
        ((AbstractCharSequenceAssert)Assertions.assertThat(output).isEqualTo("Article Number")).doesNotContain(String.valueOf(ExcelTemplateConstants.SpecialMark.MULTIVALUE.getMark()));
    }
}
