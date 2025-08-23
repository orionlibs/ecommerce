package com.hybris.backoffice.excel.validators;

import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.enumeration.EnumerationMetaTypeModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExcelEnumFieldValidatorTest
{
    public static final String CHECK = "check";
    @Mock
    private EnumerationService enumerationService;
    @InjectMocks
    private ExcelEnumValidator excelEnumFieldValidator;


    @Test
    public void shouldHandleWhenCellValueIsNotBlankAndAttributeIsEnum()
    {
        ImportParameters importParameters = new ImportParameters("Product", null, "notBlank", null, new ArrayList());
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        EnumerationMetaTypeModel typeModel = (EnumerationMetaTypeModel)Mockito.mock(EnumerationMetaTypeModel.class);
        Mockito.when(attributeDescriptor.getAttributeType()).thenReturn(typeModel);
        boolean canHandle = this.excelEnumFieldValidator.canHandle(importParameters, attributeDescriptor);
        Assertions.assertThat(canHandle).isTrue();
    }


    @Test
    public void shouldNotHandleWhenCellIsEmpty()
    {
        ImportParameters importParameters = new ImportParameters("Product", null, "", null, new ArrayList());
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        EnumerationMetaTypeModel typeModel = (EnumerationMetaTypeModel)Mockito.mock(EnumerationMetaTypeModel.class);
        Mockito.lenient().when(attributeDescriptor.getAttributeType()).thenReturn(typeModel);
        boolean canHandle = this.excelEnumFieldValidator.canHandle(importParameters, attributeDescriptor);
        Assertions.assertThat(canHandle).isFalse();
    }


    @Test
    public void shouldNotHandleWhenAttributeIsNotEnum()
    {
        ImportParameters importParameters = new ImportParameters("Product", null, "check", null, new ArrayList());
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        TypeModel typeModel = (TypeModel)Mockito.mock(TypeModel.class);
        Mockito.when(attributeDescriptor.getAttributeType()).thenReturn(typeModel);
        Mockito.lenient().when(typeModel.getCode()).thenReturn(Integer.class.getCanonicalName());
        boolean canHandle = this.excelEnumFieldValidator.canHandle(importParameters, attributeDescriptor);
        Assertions.assertThat(canHandle).isFalse();
    }


    @Test
    public void shouldNotReturnValidationErrorWhenCellHasEnumValue()
    {
        ImportParameters importParameters = new ImportParameters("Product", null, "check", null, new ArrayList());
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        TypeModel typeModel = (TypeModel)Mockito.mock(TypeModel.class);
        Mockito.when(attributeDescriptor.getAttributeType()).thenReturn(typeModel);
        Mockito.when(typeModel.getCode()).thenReturn("ArticleApprovalStatus");
        List<HybrisEnumValue> enumValues = new ArrayList<>();
        enumValues.add(ArticleApprovalStatus.CHECK);
        enumValues.add(ArticleApprovalStatus.APPROVED);
        enumValues.add(ArticleApprovalStatus.UNAPPROVED);
        Mockito.when(this.enumerationService.getEnumerationValues("ArticleApprovalStatus")).thenReturn(enumValues);
        ExcelValidationResult validationResult = this.excelEnumFieldValidator.validate(importParameters, attributeDescriptor, new HashMap<>());
        Assertions.assertThat(validationResult.hasErrors()).isFalse();
        Assertions.assertThat(validationResult.getValidationErrors()).isEmpty();
    }


    @Test
    public void shouldReturnValidationErrorWhenCellDoesntHaveEnumValue()
    {
        ImportParameters importParameters = new ImportParameters("Product", null, "abc", null, new ArrayList());
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        TypeModel typeModel = (TypeModel)Mockito.mock(TypeModel.class);
        Mockito.when(attributeDescriptor.getAttributeType()).thenReturn(typeModel);
        Mockito.when(typeModel.getCode()).thenReturn("ArticleApprovalStatus");
        List<HybrisEnumValue> enumValues = new ArrayList<>();
        enumValues.add(ArticleApprovalStatus.CHECK);
        enumValues.add(ArticleApprovalStatus.APPROVED);
        enumValues.add(ArticleApprovalStatus.UNAPPROVED);
        Mockito.when(this.enumerationService.getEnumerationValues("ArticleApprovalStatus")).thenReturn(enumValues);
        ExcelValidationResult validationResult = this.excelEnumFieldValidator.validate(importParameters, attributeDescriptor, new HashMap<>());
        Assertions.assertThat(validationResult.hasErrors()).isTrue();
        Assertions.assertThat(validationResult.getValidationErrors()).hasSize(1);
        Assertions.assertThat(((ValidationMessage)validationResult.getValidationErrors().get(0)).getMessageKey()).isEqualTo("excel.import.validation.incorrecttype.enumvalue");
        Assertions.assertThat((Object[])((ValidationMessage)validationResult.getValidationErrors().get(0)).getParams()).containsExactly((Object[])new Serializable[] {importParameters.getCellValue(), "ArticleApprovalStatus"});
    }


    @Test
    public void shouldReturnValidationErrorWhenEnumTypeDoesntExist()
    {
        ImportParameters importParameters = new ImportParameters("Product", null, "check", null, new ArrayList());
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        TypeModel typeModel = (TypeModel)Mockito.mock(TypeModel.class);
        Mockito.when(attributeDescriptor.getAttributeType()).thenReturn(typeModel);
        Mockito.when(typeModel.getCode()).thenReturn("ArticleApprovalStatus");
        ((EnumerationService)Mockito.doThrow(UnknownIdentifierException.class).when(this.enumerationService)).getEnumerationValues("ArticleApprovalStatus");
        ExcelValidationResult validationResult = this.excelEnumFieldValidator.validate(importParameters, attributeDescriptor, new HashMap<>());
        Assertions.assertThat(validationResult.hasErrors()).isTrue();
        Assertions.assertThat(validationResult.getValidationErrors()).hasSize(1);
        Assertions.assertThat(((ValidationMessage)validationResult.getValidationErrors().get(0)).getMessageKey()).isEqualTo("excel.import.validation.incorrecttype.enum");
        Assertions.assertThat((Object[])((ValidationMessage)validationResult.getValidationErrors().get(0)).getParams()).containsExactly((Object[])new Serializable[] {importParameters.getCellValue(), "ArticleApprovalStatus"});
    }
}
