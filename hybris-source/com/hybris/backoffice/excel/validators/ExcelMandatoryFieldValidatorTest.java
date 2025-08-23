package com.hybris.backoffice.excel.validators;

import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import java.util.ArrayList;
import java.util.HashMap;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExcelMandatoryFieldValidatorTest
{
    @Mock
    private CommonI18NService commonI18NService;
    @InjectMocks
    private ExcelMandatoryFieldValidator excelMandatoryFieldValidator;


    @Test
    public void shouldHandleWhenAttributeIsNotOptional()
    {
        ImportParameters importParameters = new ImportParameters("Product", null, null, null, new ArrayList());
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        Mockito.when(attributeDescriptor.getOptional()).thenReturn(Boolean.valueOf(false));
        boolean canHandle = this.excelMandatoryFieldValidator.canHandle(importParameters, attributeDescriptor);
        Assertions.assertThat(canHandle).isTrue();
    }


    @Test
    public void shouldHandleWhenAttributeIsNotOptionalAndItIsLocalizedForCurrentLanguage()
    {
        ImportParameters importParameters = new ImportParameters("Product", "en", null, null, new ArrayList());
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        LanguageModel languageModel = (LanguageModel)Mockito.mock(LanguageModel.class);
        Mockito.when(attributeDescriptor.getOptional()).thenReturn(Boolean.valueOf(false));
        Mockito.when(attributeDescriptor.getLocalized()).thenReturn(Boolean.valueOf(true));
        Mockito.when(this.commonI18NService.getCurrentLanguage()).thenReturn(languageModel);
        Mockito.when(languageModel.getIsocode()).thenReturn("en");
        boolean canHandle = this.excelMandatoryFieldValidator.canHandle(importParameters, attributeDescriptor);
        Assertions.assertThat(canHandle).isTrue();
    }


    @Test
    public void shouldNotHandleWhenAttributeIsOptional()
    {
        ImportParameters importParameters = new ImportParameters("Product", null, null, null, new ArrayList());
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        Mockito.when(attributeDescriptor.getOptional()).thenReturn(Boolean.valueOf(true));
        boolean canHandle = this.excelMandatoryFieldValidator.canHandle(importParameters, attributeDescriptor);
        Assertions.assertThat(canHandle).isFalse();
    }


    @Test
    public void shouldNotHandleWhenAttributeIsNotOptionalAndIsNotLocalizedForCurrentLanguage()
    {
        ImportParameters importParameters = new ImportParameters("Product", "en", null, null, new ArrayList());
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        LanguageModel languageModel = (LanguageModel)Mockito.mock(LanguageModel.class);
        Mockito.when(attributeDescriptor.getOptional()).thenReturn(Boolean.valueOf(false));
        Mockito.when(attributeDescriptor.getLocalized()).thenReturn(Boolean.valueOf(true));
        Mockito.when(this.commonI18NService.getCurrentLanguage()).thenReturn(languageModel);
        Mockito.when(languageModel.getIsocode()).thenReturn("fr");
        boolean canHandle = this.excelMandatoryFieldValidator.canHandle(importParameters, attributeDescriptor);
        Assertions.assertThat(canHandle).isFalse();
    }


    @Test
    public void shouldNotReturnValidationErrorWhenCellIsNotBlank()
    {
        ImportParameters importParameters = new ImportParameters("Product", null, "notEmptyCell", null, new ArrayList());
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        ExcelValidationResult validationResult = this.excelMandatoryFieldValidator.validate(importParameters, attributeDescriptor, new HashMap<>());
        Assertions.assertThat(validationResult.hasErrors()).isFalse();
        Assertions.assertThat(validationResult.getValidationErrors()).isEmpty();
    }


    @Test
    public void shouldReturnValidationErrorWhenCellIsBlank()
    {
        ImportParameters importParameters = new ImportParameters("Product", null, "", null, new ArrayList());
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        ExcelValidationResult validationResult = this.excelMandatoryFieldValidator.validate(importParameters, attributeDescriptor, new HashMap<>());
        Assertions.assertThat(validationResult.hasErrors()).isTrue();
        Assertions.assertThat(validationResult.getValidationErrors()).hasSize(1);
        Assertions.assertThat(((ValidationMessage)validationResult.getValidationErrors().get(0)).getMessageKey()).isEqualTo("excel.import.validation.mandatory.field");
        Assertions.assertThat((Object[])((ValidationMessage)validationResult.getValidationErrors().get(0)).getParams()).isEmpty();
    }
}
