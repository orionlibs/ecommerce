package com.hybris.backoffice.excel.validators;

import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.util.DefaultExcelDateUtils;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.i18n.I18NService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExcelDateFieldValidatorTest
{
    @InjectMocks
    private ExcelDateValidator excelDateFieldValidator;
    @Spy
    private final DefaultExcelDateUtils excelDateUtils = new DefaultExcelDateUtils();


    @Before
    public void setUp() throws Exception
    {
        I18NService i18NService = (I18NService)Mockito.mock(I18NService.class);
        Mockito.when(i18NService.getCurrentTimeZone()).thenReturn(TimeZone.getDefault());
        this.excelDateUtils.setI18NService(i18NService);
    }


    @Test
    public void shouldHandleWhenCellValueIsNotBlankAndAttributeIsDate()
    {
        ImportParameters importParameters = new ImportParameters("Product", null, "10/18/17 9:44 AM", null, new ArrayList());
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        TypeModel typeModel = (TypeModel)Mockito.mock(TypeModel.class);
        Mockito.when(attributeDescriptor.getAttributeType()).thenReturn(typeModel);
        Mockito.when(typeModel.getCode()).thenReturn(Date.class.getCanonicalName());
        boolean canHandle = this.excelDateFieldValidator.canHandle(importParameters, attributeDescriptor);
        Assertions.assertThat(canHandle).isTrue();
    }


    @Test
    public void shouldNotHandleWhenCellIsEmpty()
    {
        ImportParameters importParameters = new ImportParameters("Product", null, "", null, new ArrayList());
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        TypeModel typeModel = (TypeModel)Mockito.mock(TypeModel.class);
        Mockito.lenient().when(attributeDescriptor.getAttributeType()).thenReturn(typeModel);
        Mockito.lenient().when(typeModel.getCode()).thenReturn(Date.class.getCanonicalName());
        boolean canHandle = this.excelDateFieldValidator.canHandle(importParameters, attributeDescriptor);
        Assertions.assertThat(canHandle).isFalse();
    }


    @Test
    public void shouldNotHandleWhenAttributeIsNotDate()
    {
        ImportParameters importParameters = new ImportParameters("Product", null, "10/18/17 9:44 AM", null, new ArrayList());
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        TypeModel typeModel = (TypeModel)Mockito.mock(TypeModel.class);
        Mockito.when(attributeDescriptor.getAttributeType()).thenReturn(typeModel);
        Mockito.when(typeModel.getCode()).thenReturn(Integer.class.getCanonicalName());
        boolean canHandle = this.excelDateFieldValidator.canHandle(importParameters, attributeDescriptor);
        Assertions.assertThat(canHandle).isFalse();
    }


    @Test
    public void shouldNotReturnValidationErrorWhenCellHasDateValue()
    {
        ImportParameters importParameters = new ImportParameters("Product", null, this.excelDateUtils.exportDate(new Date()), null, new ArrayList());
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        ExcelValidationResult validationResult = this.excelDateFieldValidator.validate(importParameters, attributeDescriptor, new HashMap<>());
        Assertions.assertThat(validationResult.hasErrors()).isFalse();
        Assertions.assertThat(validationResult.getValidationErrors()).isEmpty();
    }


    @Test
    public void shouldReturnValidationErrorWhenCellDoesntHaveDateValue()
    {
        ImportParameters importParameters = new ImportParameters("Product", null, "abc", null, new ArrayList());
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        ExcelValidationResult validationResult = this.excelDateFieldValidator.validate(importParameters, attributeDescriptor, new HashMap<>());
        Assertions.assertThat(validationResult.hasErrors()).isTrue();
        Assertions.assertThat(validationResult.getValidationErrors()).hasSize(1);
        Assertions.assertThat((Object[])((ValidationMessage)validationResult.getValidationErrors().get(0)).getParams()).containsExactly((Object[])new Serializable[] {importParameters.getCellValue()});
        Assertions.assertThat(((ValidationMessage)validationResult.getValidationErrors().get(0)).getMessageKey())
                        .isEqualTo("excel.import.validation.incorrecttype.date");
    }
}
