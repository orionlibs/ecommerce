package com.hybris.backoffice.excel.validators.classification;

import com.google.common.collect.Maps;
import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

public class ExcelEnumClassificationValidatorTest
{
    private static final Map<String, Object> ANY_CONTEXT = Collections.emptyMap();
    ExcelEnumClassificationValidator excelEnumClassificationValidator = new ExcelEnumClassificationValidator();


    @Test
    public void shouldHandleNonBlankCellValueAndEnumAttributeType()
    {
        ExcelClassificationAttribute excelAttribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        ImportParameters importParameters = (ImportParameters)Mockito.mock(ImportParameters.class);
        ClassAttributeAssignmentModel classAttributeAssignmentModel = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        BDDMockito.given(excelAttribute.getAttributeAssignment()).willReturn(classAttributeAssignmentModel);
        BDDMockito.given(classAttributeAssignmentModel.getAttributeType()).willReturn(ClassificationAttributeTypeEnum.ENUM);
        BDDMockito.given(Boolean.valueOf(importParameters.isCellValueNotBlank())).willReturn(Boolean.valueOf(true));
        boolean result = this.excelEnumClassificationValidator.canHandle(excelAttribute, importParameters);
        Assertions.assertThat(result).isTrue();
    }


    @Test
    public void shouldNotHandleEnumAttributeTypeAsTheCellValueIsBlank()
    {
        ExcelClassificationAttribute excelAttribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        ImportParameters importParameters = (ImportParameters)Mockito.mock(ImportParameters.class);
        ClassAttributeAssignmentModel classAttributeAssignmentModel = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        BDDMockito.given(excelAttribute.getAttributeAssignment()).willReturn(classAttributeAssignmentModel);
        BDDMockito.given(classAttributeAssignmentModel.getAttributeType()).willReturn(ClassificationAttributeTypeEnum.ENUM);
        BDDMockito.given(Boolean.valueOf(importParameters.isCellValueNotBlank())).willReturn(Boolean.valueOf(false));
        boolean result = this.excelEnumClassificationValidator.canHandle(excelAttribute, importParameters);
        Assertions.assertThat(result).isFalse();
    }


    @Test
    public void shouldNotHandleNonBlankCellValueAsAttributeTypeIsNotEnum()
    {
        ExcelClassificationAttribute excelAttribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        ImportParameters importParameters = (ImportParameters)Mockito.mock(ImportParameters.class);
        ClassAttributeAssignmentModel classAttributeAssignmentModel = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        BDDMockito.given(excelAttribute.getAttributeAssignment()).willReturn(classAttributeAssignmentModel);
        BDDMockito.given(classAttributeAssignmentModel.getAttributeType()).willReturn(ClassificationAttributeTypeEnum.NUMBER);
        BDDMockito.given(Boolean.valueOf(importParameters.isCellValueNotBlank())).willReturn(Boolean.valueOf(true));
        boolean result = this.excelEnumClassificationValidator.canHandle(excelAttribute, importParameters);
        Assertions.assertThat(result).isFalse();
    }


    @Test
    public void shouldValidateWithSuccessResult()
    {
        ExcelClassificationAttribute excelAttribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        ImportParameters importParameters = (ImportParameters)Mockito.mock(ImportParameters.class);
        ClassAttributeAssignmentModel classAttributeAssignmentModel = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ClassificationAttributeValueModel attributeValue1 = (ClassificationAttributeValueModel)Mockito.mock(ClassificationAttributeValueModel.class);
        ClassificationAttributeValueModel attributeValue2 = (ClassificationAttributeValueModel)Mockito.mock(ClassificationAttributeValueModel.class);
        BDDMockito.given(excelAttribute.getAttributeAssignment()).willReturn(classAttributeAssignmentModel);
        BDDMockito.given(classAttributeAssignmentModel.getAttributeValues()).willReturn(Arrays.asList(new ClassificationAttributeValueModel[] {attributeValue1, attributeValue2}));
        BDDMockito.given(attributeValue2.getCode()).willReturn("someEnumValue");
        BDDMockito.given(importParameters.getCellValue()).willReturn("someEnumValue");
        ExcelValidationResult result = this.excelEnumClassificationValidator.validate(excelAttribute, importParameters, ANY_CONTEXT);
        Assertions.assertThat((Comparable)result).isEqualTo(ExcelValidationResult.SUCCESS);
    }


    @Test
    public void shouldValidateWithErrorAsTheImportedEnumDoesNotExist()
    {
        ExcelClassificationAttribute excelAttribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        ImportParameters importParameters = (ImportParameters)Mockito.mock(ImportParameters.class);
        ClassAttributeAssignmentModel classAttributeAssignmentModel = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ClassificationAttributeValueModel attributeValue1 = (ClassificationAttributeValueModel)Mockito.mock(ClassificationAttributeValueModel.class);
        ClassificationAttributeValueModel attributeValue2 = (ClassificationAttributeValueModel)Mockito.mock(ClassificationAttributeValueModel.class);
        ClassificationAttributeModel classificationAttributeModel = (ClassificationAttributeModel)Mockito.mock(ClassificationAttributeModel.class);
        BDDMockito.given(excelAttribute.getAttributeAssignment()).willReturn(classAttributeAssignmentModel);
        BDDMockito.given(classAttributeAssignmentModel.getAttributeValues()).willReturn(Arrays.asList(new ClassificationAttributeValueModel[] {attributeValue1, attributeValue2}));
        BDDMockito.given(classAttributeAssignmentModel.getClassificationAttribute()).willReturn(classificationAttributeModel);
        BDDMockito.given(classificationAttributeModel.getCode()).willReturn("typeName");
        BDDMockito.given(attributeValue2.getCode()).willReturn("someEnumValue");
        BDDMockito.given(importParameters.getCellValue()).willReturn("someOtherEnumValue");
        ExcelValidationResult result = this.excelEnumClassificationValidator.validate(excelAttribute, importParameters, ANY_CONTEXT);
        Assertions.assertThat((Comparable)result).isNotNull();
        Assertions.assertThat(result.getValidationErrors())
                        .contains((Object[])new ValidationMessage[] {new ValidationMessage("excel.import.validation.incorrecttype.enum", new Serializable[] {"someOtherEnumValue", "typeName"})});
    }


    @Test
    public void shouldDefaultValuesBeOnlyUsedInCaseOfMissingAttributeValues()
    {
        ExcelClassificationAttribute excelAttribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        ImportParameters importParameters = (ImportParameters)Mockito.mock(ImportParameters.class);
        ClassAttributeAssignmentModel classAttributeAssignmentModel = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ClassificationAttributeValueModel attributeValue1 = (ClassificationAttributeValueModel)Mockito.mock(ClassificationAttributeValueModel.class);
        ClassificationAttributeValueModel attributeValue2 = (ClassificationAttributeValueModel)Mockito.mock(ClassificationAttributeValueModel.class);
        ClassificationAttributeModel classificationAttributeModel = (ClassificationAttributeModel)Mockito.mock(ClassificationAttributeModel.class);
        BDDMockito.given(excelAttribute.getAttributeAssignment()).willReturn(classAttributeAssignmentModel);
        BDDMockito.given(classAttributeAssignmentModel.getAttributeValues()).willReturn(Collections.emptyList());
        BDDMockito.given(attributeValue1.getCode()).willReturn("val1");
        BDDMockito.given(attributeValue2.getCode()).willReturn("val2");
        BDDMockito.given(classificationAttributeModel.getDefaultAttributeValues())
                        .willReturn(Lists.newArrayList((Object[])new ClassificationAttributeValueModel[] {attributeValue1, attributeValue2}));
        BDDMockito.given(classAttributeAssignmentModel.getClassificationAttribute()).willReturn(classificationAttributeModel);
        BDDMockito.given(importParameters.getCellValue()).willReturn("val1");
        ExcelValidationResult result = this.excelEnumClassificationValidator.validate(excelAttribute, importParameters,
                        Maps.newHashMap());
        Assertions.assertThat(result.hasErrors()).isFalse();
    }
}
