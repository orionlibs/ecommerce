package com.hybris.backoffice.excel.template.populator.appender;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelAttributeDescriptorAttribute;
import com.hybris.backoffice.excel.template.ExcelTemplateConstants;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import org.assertj.core.api.AbstractCharSequenceAssert;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MandatoryExcelMarkAppenderTest
{
    @Mock
    CommonI18NService commonI18NService;
    @InjectMocks
    MandatoryExcelMarkAppender appender = new MandatoryExcelMarkAppender();


    @Test
    public void shouldMarkNotBeAppendedWhenAttributeIsNotMandatory()
    {
        String input = "Article Number";
        ExcelAttribute excelAttribute = (ExcelAttribute)Mockito.mock(ExcelAttribute.class);
        BDDMockito.given(Boolean.valueOf(excelAttribute.isMandatory())).willReturn(Boolean.valueOf(false));
        String output = this.appender.apply("Article Number", excelAttribute);
        ((AbstractCharSequenceAssert)AssertionsForClassTypes.assertThat(output).isEqualTo("Article Number")).doesNotContain(String.valueOf(ExcelTemplateConstants.SpecialMark.MANDATORY.getMark()));
    }


    @Test
    public void shouldMarkBeAppendedWhenAttributeIsMandatory()
    {
        String input = "Article Number";
        ExcelAttribute excelAttribute = (ExcelAttribute)Mockito.mock(ExcelAttribute.class);
        BDDMockito.given(Boolean.valueOf(excelAttribute.isMandatory())).willReturn(Boolean.valueOf(true));
        String output = this.appender.apply("Article Number", excelAttribute);
        AssertionsForClassTypes.assertThat(output).isEqualTo("Article Number" + ExcelTemplateConstants.SpecialMark.MANDATORY.getMark());
    }


    @Test
    public void shouldMarkNotBeAppendedWhenLocaleIsDifferentFromCurrentLocale()
    {
        String input = "Article Number";
        String currentIsoCode = "en";
        String attrIsoCode = "de";
        ExcelAttribute excelAttribute = (ExcelAttribute)Mockito.mock(ExcelAttribute.class);
        BDDMockito.given(Boolean.valueOf(excelAttribute.isMandatory())).willReturn(Boolean.valueOf(true));
        BDDMockito.given(Boolean.valueOf(excelAttribute.isLocalized())).willReturn(Boolean.valueOf(true));
        BDDMockito.given(excelAttribute.getIsoCode()).willReturn("de");
        LanguageModel languageModel = (LanguageModel)Mockito.mock(LanguageModel.class);
        BDDMockito.given(languageModel.getIsocode()).willReturn("en");
        BDDMockito.given(this.commonI18NService.getCurrentLanguage()).willReturn(languageModel);
        String output = this.appender.apply("Article Number", excelAttribute);
        ((AbstractCharSequenceAssert)AssertionsForClassTypes.assertThat(output).isEqualTo("Article Number")).doesNotContain(String.valueOf(ExcelTemplateConstants.SpecialMark.MANDATORY.getMark()));
    }


    @Test
    public void shouldMarkBeAppendedWhenAttributeIsMandatoryAndLocalesAreTheSame()
    {
        String input = "Article Number";
        String currentIsoCode = "en";
        String attrIsoCode = "en";
        ExcelAttribute excelAttribute = (ExcelAttribute)Mockito.mock(ExcelAttribute.class);
        BDDMockito.given(Boolean.valueOf(excelAttribute.isMandatory())).willReturn(Boolean.valueOf(true));
        BDDMockito.given(Boolean.valueOf(excelAttribute.isLocalized())).willReturn(Boolean.valueOf(true));
        BDDMockito.given(excelAttribute.getIsoCode()).willReturn("en");
        LanguageModel languageModel = (LanguageModel)Mockito.mock(LanguageModel.class);
        BDDMockito.given(languageModel.getIsocode()).willReturn("en");
        BDDMockito.given(this.commonI18NService.getCurrentLanguage()).willReturn(languageModel);
        String output = this.appender.apply("Article Number", excelAttribute);
        AssertionsForClassTypes.assertThat(output).contains(new CharSequence[] {String.valueOf(ExcelTemplateConstants.SpecialMark.MANDATORY.getMark())});
    }


    @Test
    public void shouldMarkBeNotAppendedWhenAttributeIsMandatoryAndDoesntHaveDefaultValue()
    {
        String input = "Article Number";
        ExcelAttributeDescriptorAttribute excelAttribute = (ExcelAttributeDescriptorAttribute)Mockito.mock(ExcelAttributeDescriptorAttribute.class);
        AttributeDescriptorModel attributeDescriptorModel = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(Boolean.valueOf(excelAttribute.isMandatory())).willReturn(Boolean.valueOf(true));
        BDDMockito.given(excelAttribute.getAttributeDescriptorModel()).willReturn(attributeDescriptorModel);
        BDDMockito.given(attributeDescriptorModel.getDefaultValue()).willReturn(null);
        String output = this.appender.apply("Article Number", (ExcelAttribute)excelAttribute);
        AssertionsForClassTypes.assertThat(output).contains(new CharSequence[] {"*"});
    }
}
