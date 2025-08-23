package com.hybris.backoffice.excel.validators;

import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import java.io.Serializable;
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
public class ExcelNumberFieldValidatorTest
{
    @Mock
    private TypeService typeService;
    @InjectMocks
    private ExcelNumberValidator excelNumberFieldValidator;


    @Test
    public void shouldHandleWhenCellValueIsNotBlankAndAttributeIsNumber()
    {
        ImportParameters importParameters = new ImportParameters("Product", null, Double.valueOf(3.14D), null, new ArrayList());
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        TypeModel typeModel = (TypeModel)Mockito.mock(TypeModel.class);
        Mockito.when(attributeDescriptor.getAttributeType()).thenReturn(typeModel);
        Mockito.when(typeModel.getCode()).thenReturn(Double.class.getCanonicalName());
        Mockito.when(Boolean.valueOf(this.typeService.isAssignableFrom(Number.class.getCanonicalName(), typeModel.getCode()))).thenReturn(Boolean.valueOf(true));
        boolean canHandle = this.excelNumberFieldValidator.canHandle(importParameters, attributeDescriptor);
        Assertions.assertThat(canHandle).isTrue();
    }


    @Test
    public void shouldNotHandleWhenCellIsEmpty()
    {
        ImportParameters importParameters = new ImportParameters("Product", null, "", null, new ArrayList());
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        TypeModel typeModel = (TypeModel)Mockito.mock(TypeModel.class);
        Mockito.lenient().when(attributeDescriptor.getAttributeType()).thenReturn(typeModel);
        Mockito.lenient().when(typeModel.getCode()).thenReturn(Integer.class.getCanonicalName());
        boolean canHandle = this.excelNumberFieldValidator.canHandle(importParameters, attributeDescriptor);
        Assertions.assertThat(canHandle).isFalse();
    }


    @Test
    public void shouldNotHandleWhenAttributeIsNotNumber()
    {
        ImportParameters importParameters = new ImportParameters("Product", null, "any value", null, new ArrayList());
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        TypeModel typeModel = (TypeModel)Mockito.mock(TypeModel.class);
        Mockito.when(attributeDescriptor.getAttributeType()).thenReturn(typeModel);
        Mockito.when(typeModel.getCode()).thenReturn(Boolean.class.getCanonicalName());
        boolean canHandle = this.excelNumberFieldValidator.canHandle(importParameters, attributeDescriptor);
        Assertions.assertThat(canHandle).isFalse();
    }


    @Test
    public void shouldNotReturnValidationErrorWhenCellHasNumberValue()
    {
        ImportParameters importParameters = new ImportParameters("Product", null, "3.14", null, new ArrayList());
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        ExcelValidationResult validationResult = this.excelNumberFieldValidator.validate(importParameters, attributeDescriptor, new HashMap<>());
        Assertions.assertThat(validationResult.hasErrors()).isFalse();
        Assertions.assertThat(validationResult.getValidationErrors()).isEmpty();
    }


    @Test
    public void shouldReturnValidationErrorWhenCellDoesntHaveNumberValue()
    {
        ImportParameters importParameters = new ImportParameters("Product", null, Boolean.TRUE, null, new ArrayList());
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        ExcelValidationResult validationResult = this.excelNumberFieldValidator.validate(importParameters, attributeDescriptor, new HashMap<>());
        Assertions.assertThat(validationResult.hasErrors()).isTrue();
        Assertions.assertThat(validationResult.getValidationErrors()).hasSize(1);
        Assertions.assertThat(((ValidationMessage)validationResult.getValidationErrors().get(0)).getMessageKey()).isEqualTo("excel.import.validation.incorrecttype.number");
        Assertions.assertThat((Object[])((ValidationMessage)validationResult.getValidationErrors().get(0)).getParams()).containsExactly((Object[])new Serializable[] {importParameters.getCellValue()});
    }
}
