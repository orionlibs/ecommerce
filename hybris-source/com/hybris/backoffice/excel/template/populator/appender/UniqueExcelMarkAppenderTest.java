package com.hybris.backoffice.excel.template.populator.appender;

import com.hybris.backoffice.excel.data.ExcelAttributeDescriptorAttribute;
import com.hybris.backoffice.excel.template.ExcelTemplateConstants;
import com.hybris.backoffice.excel.template.filter.ExcelFilter;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import org.assertj.core.api.AbstractCharSequenceAssert;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UniqueExcelMarkAppenderTest
{
    @Mock
    ExcelFilter<AttributeDescriptorModel> uniqueFilter;
    @InjectMocks
    UniqueExcelMarkAppender appender = new UniqueExcelMarkAppender();


    @Test
    public void shouldMarkBeAppendedWhenExcelFilterReturnsTrue()
    {
        String input = "Article Number";
        BDDMockito.given(Boolean.valueOf(this.uniqueFilter.test(Matchers.any()))).willReturn(Boolean.valueOf(true));
        String output = this.appender.apply("Article Number", (ExcelAttributeDescriptorAttribute)Mockito.mock(ExcelAttributeDescriptorAttribute.class));
        Assertions.assertThat(output).contains(new CharSequence[] {String.valueOf(ExcelTemplateConstants.SpecialMark.UNIQUE.getMark())});
    }


    @Test
    public void shouldMarkNotBeAppendedWhenExcelFilterReturnsFalse()
    {
        String input = "Article Number";
        BDDMockito.given(Boolean.valueOf(this.uniqueFilter.test(Matchers.any()))).willReturn(Boolean.valueOf(false));
        String output = this.appender.apply("Article Number", (ExcelAttributeDescriptorAttribute)Mockito.mock(ExcelAttributeDescriptorAttribute.class));
        ((AbstractCharSequenceAssert)Assertions.assertThat(output).isEqualTo("Article Number")).doesNotContain(String.valueOf(ExcelTemplateConstants.SpecialMark.UNIQUE.getMark()));
    }
}
