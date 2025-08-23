package com.hybris.backoffice.excel.template;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.template.populator.DefaultExcelAttributeContext;
import com.hybris.backoffice.excel.template.populator.appender.ExcelMarkAppender;
import com.hybris.backoffice.excel.template.populator.extractor.ClassificationFullNameExtractor;
import java.util.Arrays;
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
public class ClassificationAttributeNameFormatterTest
{
    @Mock
    ClassificationFullNameExtractor mockedClassificationFullNameExtractor;
    @InjectMocks
    ClassificationAttributeNameFormatter classificationAttributeNameFormatter;


    @Test
    public void shouldFormatAttributeNameWithExtractor()
    {
        ExcelClassificationAttribute excelClassificationAttribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        BDDMockito.given(this.mockedClassificationFullNameExtractor.extract(excelClassificationAttribute)).willReturn("extractedFullName");
        String result = this.classificationAttributeNameFormatter.format(DefaultExcelAttributeContext.ofExcelAttribute((ExcelAttribute)excelClassificationAttribute));
        Assertions.assertThat(result).isEqualTo("extractedFullName");
    }


    @Test
    public void shouldApplyAppenders()
    {
        ExcelClassificationAttribute excelAttribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        BDDMockito.given(this.mockedClassificationFullNameExtractor.extract(excelAttribute)).willReturn("fullName");
        ExcelMarkAppender<ExcelClassificationAttribute> firstAppender = (ExcelMarkAppender<ExcelClassificationAttribute>)Mockito.mock(ExcelMarkAppender.class);
        ExcelMarkAppender<ExcelClassificationAttribute> secondAppender = (ExcelMarkAppender<ExcelClassificationAttribute>)Mockito.mock(ExcelMarkAppender.class);
        BDDMockito.given(firstAppender.apply(Matchers.any(), Matchers.any())).willReturn("fullNameFirstAppender");
        BDDMockito.given(secondAppender.apply(Matchers.any(), Matchers.any())).willReturn("fullNameFirstAppenderSecondAppender");
        this.classificationAttributeNameFormatter.setAppenders(Arrays.asList(new ExcelMarkAppender[] {firstAppender, secondAppender}));
        String result = this.classificationAttributeNameFormatter.format(DefaultExcelAttributeContext.ofExcelAttribute((ExcelAttribute)excelAttribute));
        Assertions.assertThat(result).isEqualTo("fullNameFirstAppenderSecondAppender");
        ((ExcelMarkAppender)BDDMockito.then(firstAppender).should()).apply("fullName", excelAttribute);
        ((ExcelMarkAppender)BDDMockito.then(secondAppender).should()).apply("fullNameFirstAppender", excelAttribute);
    }
}
