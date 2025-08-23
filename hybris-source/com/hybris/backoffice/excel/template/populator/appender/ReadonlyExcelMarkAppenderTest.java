package com.hybris.backoffice.excel.template.populator.appender;

import com.hybris.backoffice.excel.data.ExcelAttributeDescriptorAttribute;
import com.hybris.backoffice.excel.template.ExcelTemplateConstants;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import org.assertj.core.api.AbstractCharSequenceAssert;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ReadonlyExcelMarkAppenderTest
{
    private ReadonlyExcelMarkAppender appender = new ReadonlyExcelMarkAppender();


    @Test
    public void shouldReadonlyMarkBeAppendedToInputWhenAttributeIsReadonly()
    {
        ExcelAttributeDescriptorAttribute excelAttributeDescriptorAttribute = (ExcelAttributeDescriptorAttribute)Mockito.mock(ExcelAttributeDescriptorAttribute.class);
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(attributeDescriptor.getReadable()).willReturn(Boolean.valueOf(true));
        BDDMockito.given(attributeDescriptor.getWritable()).willReturn(Boolean.valueOf(false));
        BDDMockito.given(excelAttributeDescriptorAttribute.getAttributeDescriptorModel()).willReturn(attributeDescriptor);
        String input = "Article Number";
        String output = this.appender.apply("Article Number", excelAttributeDescriptorAttribute);
        Assertions.assertThat(output).isEqualTo("Article Number" + ExcelTemplateConstants.SpecialMark.READONLY.getMark());
    }


    @Test
    public void shouldReadonlyMarkNotBeAppendedToInputWhenAttributeIsNotReadonly()
    {
        ExcelAttributeDescriptorAttribute excelAttributeDescriptorAttribute = (ExcelAttributeDescriptorAttribute)Mockito.mock(ExcelAttributeDescriptorAttribute.class);
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(attributeDescriptor.getReadable()).willReturn(Boolean.valueOf(true));
        BDDMockito.given(attributeDescriptor.getWritable()).willReturn(Boolean.valueOf(true));
        BDDMockito.given(excelAttributeDescriptorAttribute.getAttributeDescriptorModel()).willReturn(attributeDescriptor);
        String input = "Article Number";
        String output = this.appender.apply("Article Number", excelAttributeDescriptorAttribute);
        ((AbstractCharSequenceAssert)Assertions.assertThat(output).isEqualTo("Article Number")).doesNotContain(String.valueOf(ExcelTemplateConstants.SpecialMark.READONLY.getMark()));
    }
}
