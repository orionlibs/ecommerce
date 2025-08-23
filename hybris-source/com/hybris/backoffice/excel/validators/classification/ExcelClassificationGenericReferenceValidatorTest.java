package com.hybris.backoffice.excel.validators.classification;

import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.translators.generic.RequiredAttribute;
import com.hybris.backoffice.excel.translators.generic.factory.RequiredAttributesFactory;
import com.hybris.backoffice.excel.validators.ExcelGenericReferenceValidator;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExcelClassificationGenericReferenceValidatorTest
{
    private static final HashMap<String, Object> ANY_CONTEXT = new HashMap<>();
    @Mock
    ExcelGenericReferenceValidator mockedExcelGenericReferenceValidator;
    @Mock
    RequiredAttributesFactory mockedRequiredAttributesFactory;
    @Mock
    TypeService typeService;
    @InjectMocks
    ExcelClassificationGenericReferenceValidator excelClassificationGenericReferenceValidator;


    @Before
    public void setUp()
    {
        this.excelClassificationGenericReferenceValidator.setBlacklistedTypes(new ArrayList());
    }


    @Test
    public void shouldHandleReferenceAttributesAndNonBlankCellValues()
    {
        ImportParameters importParameters = (ImportParameters)Mockito.mock(ImportParameters.class);
        ExcelClassificationAttribute excelClassificationAttribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        ClassAttributeAssignmentModel classAttributeAssignmentModel = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        BDDMockito.given(Boolean.valueOf(importParameters.isCellValueNotBlank())).willReturn(Boolean.valueOf(true));
        BDDMockito.given(excelClassificationAttribute.getAttributeAssignment()).willReturn(classAttributeAssignmentModel);
        BDDMockito.given(classAttributeAssignmentModel.getAttributeType()).willReturn(ClassificationAttributeTypeEnum.REFERENCE);
        boolean result = this.excelClassificationGenericReferenceValidator.canHandle(excelClassificationAttribute, importParameters);
        Assertions.assertThat(result).isTrue();
    }


    @Test
    public void shouldNotHandleWhenTypeIsOnBlacklist()
    {
        ImportParameters importParameters = (ImportParameters)Mockito.mock(ImportParameters.class);
        ExcelClassificationAttribute excelClassificationAttribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        ClassAttributeAssignmentModel classAttributeAssignmentModel = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ComposedTypeModel mediaComposedType = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        List<String> blacklistedTypes = Collections.singletonList("Media");
        this.excelClassificationGenericReferenceValidator.setBlacklistedTypes(blacklistedTypes);
        BDDMockito.given(Boolean.valueOf(importParameters.isCellValueNotBlank())).willReturn(Boolean.valueOf(true));
        BDDMockito.given(excelClassificationAttribute.getAttributeAssignment()).willReturn(classAttributeAssignmentModel);
        BDDMockito.given(classAttributeAssignmentModel.getAttributeType()).willReturn(ClassificationAttributeTypeEnum.REFERENCE);
        BDDMockito.given(classAttributeAssignmentModel.getReferenceType()).willReturn(mediaComposedType);
        BDDMockito.given(mediaComposedType.getCode()).willReturn("Media");
        BDDMockito.given(Boolean.valueOf(this.typeService.isAssignableFrom("Media", "Media"))).willReturn(Boolean.valueOf(true));
        boolean result = this.excelClassificationGenericReferenceValidator.canHandle(excelClassificationAttribute, importParameters);
        Assertions.assertThat(result).isFalse();
    }


    @Test
    public void shouldHandleWhenTypeIsNotOnBlacklist()
    {
        ImportParameters importParameters = (ImportParameters)Mockito.mock(ImportParameters.class);
        ExcelClassificationAttribute excelClassificationAttribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        ClassAttributeAssignmentModel classAttributeAssignmentModel = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ComposedTypeModel productComposedType = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        List<String> blacklistedTypes = Collections.singletonList("Product");
        this.excelClassificationGenericReferenceValidator.setBlacklistedTypes(blacklistedTypes);
        BDDMockito.given(Boolean.valueOf(importParameters.isCellValueNotBlank())).willReturn(Boolean.valueOf(true));
        BDDMockito.given(excelClassificationAttribute.getAttributeAssignment()).willReturn(classAttributeAssignmentModel);
        BDDMockito.given(classAttributeAssignmentModel.getAttributeType()).willReturn(ClassificationAttributeTypeEnum.REFERENCE);
        BDDMockito.given(classAttributeAssignmentModel.getReferenceType()).willReturn(productComposedType);
        BDDMockito.given(productComposedType.getCode()).willReturn("Media");
        Mockito.lenient().when(Boolean.valueOf(this.typeService.isAssignableFrom("Media", "Media"))).thenReturn(Boolean.valueOf(true));
        boolean result = this.excelClassificationGenericReferenceValidator.canHandle(excelClassificationAttribute, importParameters);
        Assertions.assertThat(result).isTrue();
    }


    @Test
    public void shouldNotHandleBlankCellValues()
    {
        ImportParameters importParameters = (ImportParameters)Mockito.mock(ImportParameters.class);
        ExcelClassificationAttribute excelClassificationAttribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        ClassAttributeAssignmentModel classAttributeAssignmentModel = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        BDDMockito.given(Boolean.valueOf(importParameters.isCellValueNotBlank())).willReturn(Boolean.valueOf(false));
        Mockito.lenient().when(excelClassificationAttribute.getAttributeAssignment()).thenReturn(classAttributeAssignmentModel);
        Mockito.lenient().when(classAttributeAssignmentModel.getAttributeType()).thenReturn(ClassificationAttributeTypeEnum.REFERENCE);
        boolean result = this.excelClassificationGenericReferenceValidator.canHandle(excelClassificationAttribute, importParameters);
        Assertions.assertThat(result).isFalse();
    }


    @Test
    public void shouldNotHandleNonReferenceAttributes()
    {
        ImportParameters importParameters = (ImportParameters)Mockito.mock(ImportParameters.class);
        ExcelClassificationAttribute excelClassificationAttribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        ClassAttributeAssignmentModel classAttributeAssignmentModel = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        BDDMockito.given(Boolean.valueOf(importParameters.isCellValueNotBlank())).willReturn(Boolean.valueOf(true));
        BDDMockito.given(excelClassificationAttribute.getAttributeAssignment()).willReturn(classAttributeAssignmentModel);
        BDDMockito.given(classAttributeAssignmentModel.getAttributeType()).willReturn(ClassificationAttributeTypeEnum.NUMBER);
        boolean result = this.excelClassificationGenericReferenceValidator.canHandle(excelClassificationAttribute, importParameters);
        Assertions.assertThat(result).isFalse();
    }


    @Test
    public void shouldValidateReference()
    {
        ClassAttributeAssignmentModel classAttributeAssignmentModel = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ExcelClassificationAttribute excelClassificationAttribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        ComposedTypeModel referenceType = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        ImportParameters importParameters = (ImportParameters)Mockito.mock(ImportParameters.class);
        RequiredAttribute requiredAttribute = (RequiredAttribute)Mockito.mock(RequiredAttribute.class);
        ExcelValidationResult excelValidationResult = (ExcelValidationResult)Mockito.mock(ExcelValidationResult.class);
        BDDMockito.given(excelClassificationAttribute.getAttributeAssignment()).willReturn(classAttributeAssignmentModel);
        BDDMockito.given(classAttributeAssignmentModel.getReferenceType()).willReturn(referenceType);
        BDDMockito.given(this.mockedRequiredAttributesFactory.create(referenceType)).willReturn(requiredAttribute);
        BDDMockito.given(this.mockedExcelGenericReferenceValidator.validateRequiredAttribute((RequiredAttribute)Matchers.any(), (ImportParameters)Matchers.any(), (Map)Matchers.any()))
                        .willReturn(excelValidationResult);
        ExcelValidationResult result = this.excelClassificationGenericReferenceValidator.validate(excelClassificationAttribute, importParameters, ANY_CONTEXT);
        Assertions.assertThat((Comparable)result).isEqualTo(excelValidationResult);
        ((ExcelGenericReferenceValidator)BDDMockito.then(this.mockedExcelGenericReferenceValidator).should()).validateRequiredAttribute(requiredAttribute, importParameters, ANY_CONTEXT);
    }
}
